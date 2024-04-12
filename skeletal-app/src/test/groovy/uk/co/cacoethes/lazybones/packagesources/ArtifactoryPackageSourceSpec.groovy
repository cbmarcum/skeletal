package uk.co.cacoethes.lazybones.packagesources

import org.jfrog.artifactory.client.Artifactory
import org.jfrog.artifactory.client.Repositories
import org.jfrog.artifactory.client.RepositoryHandle
import org.jfrog.artifactory.client.Searches
import org.jfrog.artifactory.client.model.RepoPath
import org.jfrog.artifactory.client.model.Repository
import org.jfrog.artifactory.client.model.impl.PackageTypeImpl
import org.jfrog.artifactory.client.model.repository.settings.RepositorySettings
import spock.lang.Specification
import uk.co.cacoethes.lazybones.PackageInfo
import uk.co.cacoethes.lazybones.config.Configuration

import java.nio.file.Files
import java.nio.file.Paths

class ArtifactoryPackageSourceSpec extends Specification {
    void 'returns a list of package from the artifactory'() {
        given: 'A properly configured  artifactory source'
        ArtifactoryClientFactory factory = Stub(ArtifactoryClientFactory)
        Repository mvnRepo = Stub(Repository)

        String url = "http://artifactory"
        String username = "userName"
        String password = "password"
        String repository = "repository"
        String pattern = ".*.zip"
        Long ttlSec = 10

        def dir = File.createTempDir("someTestDir")
        dir.deleteOnExit()
        dir.toPath().toString()
        Configuration config = initConfig([
                "cache"      :
                        ["dir": dir.toPath().toString()],
                "artifactory": [
                        "url"       : url,
                        "username"  : username,
                        "password"  : password,
                        "repository": repository,
                        "pattern"   : pattern,
                        "cache"     : [
                                "ttlSec": ttlSec
                        ]
                ]
        ])
        Artifactory artifactory = Stub(Artifactory)
        factory.createClient(_ as ArtifactoryPackageSource.ArtifactoryConfig) >> artifactory
        def packageSource = new ArtifactoryPackageSource(config, factory)

        and: "artifactory returned  maven repository type"
        mvnRepo.repositorySettings >> Stub(RepositorySettings) {
            packageType >> PackageTypeImpl.maven
        }
        artifactory.repositories() >> Stub(Repositories) {
            it.repository(repository) >> Stub(RepositoryHandle) {
                get() >> mvnRepo
            }
        }

        println artifactory.repositories().repository(repository).get()

        and: "artifactory returned  list of packages"
        artifactory.searches() >> Stub(Searches) {
            repositories(repository) >> it
            artifactsByName(pattern) >> it
            doSearch() >> [
                    createPackage(repository, "org.some.group/some-package/1.0/some-package-1.0.zip"),
                    createPackage(repository, "org.some.group/some-package/2.0/some-package-2.0.zip"),
                    createPackage(repository, "org.some.group/some-package2/2.0/some-package2-2.0.zip"),
            ]
        }
        when: "Package list requested "
        def packages = packageSource.listPackageNames().sort()
        then: "Correct list returned"
        [
                "org.some.group/some-package",
                "org.some.group/some-package2",
        ].sort() == packages
    }

    void 'returns a list of package from the cache'() {
        given: 'A properly configured  artifactory source'
        ArtifactoryClientFactory factory = Stub(ArtifactoryClientFactory)

        String url = "http://artifactory"
        String username = "userName"
        String password = "password"
        String repository = "repository"
        String pattern = ".*.zip"
        Long ttlSec = 10

        def dir = File.createTempDir("someTestDir")
        dir.deleteOnExit()
        dir.toPath().toString()
        Configuration config = initConfig([
                "cache"      :
                        ["dir": dir.toPath().toString()],
                "artifactory": [
                        "url"       : url,
                        "username"  : username,
                        "password"  : password,
                        "repository": repository,
                        "pattern"   : pattern,
                        "cache"     : [
                                "ttlSec": ttlSec
                        ]
                ]
        ])
        Artifactory artifactory = Mock(Artifactory)
        factory.createClient(_ as ArtifactoryPackageSource.ArtifactoryConfig) >> artifactory
        def packageSource = new ArtifactoryPackageSource(config, factory)

        and: "there is a cached repository in local fs"
        def cacheFile = Paths.get(dir.path, "artifactoryCache")
        def fileUrl = this.class.classLoader.getResource("artifactoryCache")
        Files.writeString(cacheFile, Files.readString(Paths.get(fileUrl.toURI())))


        when: "Package list requested "
        def packages = packageSource.listPackageNames().sort()
        then: "Correct list returned"
        [
                "org.some.group/some-package",
                "org.some.group/some-package2",
        ].sort() == packages
        0 * artifactory.searches()
    }

    void 'return package info'() {
        given: 'A properly configured  artifactory source'
        ArtifactoryClientFactory factory = Stub(ArtifactoryClientFactory)

        String url = "http://artifactory"
        String username = "userName"
        String password = "password"
        String repository = "repository"
        String pattern = ".*.zip"
        Long ttlSec = 10

        def dir = File.createTempDir("someTestDir")
        dir.deleteOnExit()
        dir.toPath().toString()
        Configuration config = initConfig([
                "cache"      :
                        ["dir": dir.toPath().toString()],
                "artifactory": [
                        "url"       : url,
                        "username"  : username,
                        "password"  : password,
                        "repository": repository,
                        "pattern"   : pattern,
                        "cache"     : [
                                "ttlSec": ttlSec
                        ]
                ]
        ])
        Artifactory artifactory = Mock(Artifactory) {
            it.uri >> url
        }
        factory.createClient(_ as ArtifactoryPackageSource.ArtifactoryConfig) >> artifactory
        def packageSource = new ArtifactoryPackageSource(config, factory)

        and: "there is a cached repository in local fs"
        def cacheFile = Paths.get(dir.path, "artifactoryCache")
        def fileUrl = this.class.classLoader.getResource("artifactoryCache")
        Files.writeString(cacheFile, Files.readString(Paths.get(fileUrl.toURI())))


        when: "Package list requested "
        def pkg = packageSource.fetchPackageInfo("org.some.group/some-package")
        then: "Correct list returned"
        new PackageInfo(
                packageSource,
                "org.some.group/some-package", "2.0", ["1.0", "2.0"],
                "Undefined",
                "Undefined",
                "${url}/repository/org/some/group/some-package/2.0/some-package-2.0.zip") == pkg
        0 * artifactory.searches()
    }


    private RepoPath createPackage(repository, name) {
        new RepoPath() {
            @Override
            String getRepoKey() {
                return repository
            }

            @Override
            String getItemPath() {
                return name
            }
        }
    }

    protected Configuration initConfig(Map settings) {
        return new Configuration(
                new ConfigObject(),
                settings,
                [:],
                Configuration.VALID_OPTIONS,
                new File("delete-me.json"))
    }

}
