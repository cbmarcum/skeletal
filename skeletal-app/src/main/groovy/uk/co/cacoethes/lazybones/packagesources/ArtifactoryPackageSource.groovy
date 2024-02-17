package uk.co.cacoethes.lazybones.packagesources


import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovyjarjarantlr4.v4.runtime.misc.NotNull
import groovyjarjarantlr4.v4.runtime.misc.Nullable
import org.apache.maven.artifact.versioning.ComparableVersion
import org.apache.maven.index.artifact.Gav
import org.apache.maven.index.artifact.M2GavCalculator
import org.jfrog.artifactory.client.Artifactory
import org.jfrog.artifactory.client.model.impl.PackageTypeImpl
import uk.co.cacoethes.lazybones.PackageInfo
import uk.co.cacoethes.lazybones.config.Configuration

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.PosixFileAttributes
import java.time.Duration
import java.time.Instant
import java.util.concurrent.Callable
import java.util.function.Function
import java.util.stream.Collectors


/**
 * Package source for artifactory
 */
class ArtifactoryPackageSource implements PackageSource {


    @NotNull
    private final Artifactory artifactory
    @NotNull
    private final String repo
    @NotNull
    private final String templatePattern
    @Nullable
    private final String groupId
    @NotNull
    private final Configuration config

    private final gavCalc = new M2GavCalculator()
    private final slurper = new JsonSlurper()
    private final File cacheDir
    private final Integer cacheTTLSec
    private final ArtifactoryConfig artifactoryConfig
    private final ArtifactoryClientFactory artifactoryClientFactory


    ArtifactoryPackageSource(@NotNull Configuration config, ArtifactoryClientFactory artifactoryClientFactory) {
        this.config = config
        this.artifactoryConfig = new ArtifactoryConfig(config.getSetting("artifactory") as Map<String, Object>)
        this.artifactoryClientFactory = artifactoryClientFactory
        artifactory = artifactoryClientFactory.createClient(artifactoryConfig)
        this.repo = artifactoryConfig.repository

        def pattern = artifactoryConfig.pattern as String
        if (!pattern) {
            pattern = "*template*zip"
        }
        this.templatePattern = pattern
        this.groupId = artifactoryConfig.groupId as String
        this.cacheDir = config.getSetting("cache.dir") as File
        this.cacheTTLSec = artifactoryConfig.cache.ttlSec ?: 60
    }


    @Override
    List<String> listPackageNames() {

        return loadDefinitions().stream().map {
            it.groupId + "/" + it.artifactId
        }.distinct().collect(Collectors.toList())

    }

    private Set<Gav> loadDefinitions() {
        Path path = Paths.get(cacheDir.path, "artifactoryCache")
        if (!path.toFile().exists() || expired(path)) {
            return loadFromArtifactory(path)
        } else {
            return loadFromCache(path)
        }
    }

    private boolean expired(Path path) {
        def duration = Duration.between(Files.readAttributes(path, PosixFileAttributes).lastModifiedTime().toInstant(),
                Instant.now(),).toSeconds()
        return duration >= cacheTTLSec
    }

    @Override
    PackageInfo fetchPackageInfo(String packageName) {
        def split = packageName.split("/")
        def found = loadDefinitions().stream().filter {
            it.groupId == split[0] && it.artifactId == split[1]
        }.collect(Collectors.toMap(new Function<Gav, ComparableVersion>() {
            @Override
            ComparableVersion apply(Gav t) {
                return new ComparableVersion(t.version)
            }
        }, Function.identity()))

        if (found.isEmpty()) {
            return null
        }
        def sorted = found.keySet().sort()
        def latest = sorted.last()
        def path = gavCalc.gavToPath(found[latest])
        return new PackageInfo(this,
                packageName,
                latest.toString(), sorted.collect { it.toString() },
                "Undefined",
                "Undefined",
                "${artifactory.getUri()}${artifactory.getUri().endsWith("/") ? "" : "/"}${repo}${path}"
        )
    }

    @Override
    Callable<InputStream> getRemoteSource(String pkgName, String version) {
        Set<Gav> set = loadDefinitions()
        Map<String, Gav> map = set.collectEntries({ it ->
            [(it.groupId + ':' + it.artifactId + ':' + it.version): it]
        })

        def key = "${pkgName.replace("/", ":")}:${version}"
        def gav = map[key]
        if (!gav) {
            throw new IllegalArgumentException("Package not found $key")
        }
        def download = artifactory.repository(repo).download(gavCalc.gavToPath(gav))
        return new Callable<InputStream>() {
            @Override
            InputStream call() throws Exception {
                try {
                    return download.doDownload()
                }
                catch (ex) {
                    throw new ConnectException("Unable to download ${pkgName} ${version}").
                            initCause(ex)
                }
            }
        }

    }

    Set<Gav> loadFromArtifactory(Path path) {
        try {
            def repository = artifactory.repositories().repository(repo).get()
            if (repository.repositorySettings.packageType != PackageTypeImpl.maven) {
                System.err.println("Only maven repository supported." + " Repo $repo has ${repository.repositorySettings.packageType.name()} packaging type")
                return Set.of()
            }
        } catch (ex) {
            System.err.println("Unable to obtain repo $repo meta data")
            throw new ConnectException("Unable to obtain artifactory info").
                    initCause(ex)
        }


        def search = artifactory.searches()
                .repositories(repo)
        if (groupId != null) {
            search = search.groupId(groupId)
        }
        def found = search
                .artifactsByName(templatePattern)
                .doSearch()
                .stream()
                .map {
                    gavCalc.pathToGav(it.itemPath)
                }
                .filter { it != null }
                .filter { !it.hash && it.extension == "zip" }
                .collect(Collectors.toSet())
        def json = JsonOutput.toJson(found)
        if (!path.toFile().exists()) {
            Files.createDirectories(path.parent)
            path.toFile().createNewFile()
        }
        Files.writeString(path, json, Charset.defaultCharset())
        return found
    }

    List<Gav> loadFromCache(Path path) {
        def load = slurper.parse(path.toFile()) as List
        return load.collect {
            new Gav(
                    it.groupId,
                    it.artifactId,
                    it.version,
                    null, "zip",
                    null,
                    null,
                    null,
                    false,
                    null,
                    false,
                    null
            )
        }
    }

    static class ArtifactoryConfig {
        String url;
        String username;
        String password;
        String repository;
        String groupId;
        String pattern;
        Cache cache
    }

    static class Cache {
        Integer ttlSec
    }
}
