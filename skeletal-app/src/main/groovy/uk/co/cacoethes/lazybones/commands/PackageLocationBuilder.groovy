package uk.co.cacoethes.lazybones.commands

import groovy.util.logging.Log
import org.apache.commons.io.FilenameUtils
import uk.co.cacoethes.lazybones.PackageInfo
import uk.co.cacoethes.lazybones.PackageNotFoundException
import uk.co.cacoethes.lazybones.packagesources.PackageSource
import uk.co.cacoethes.util.UrlUtils

/**
 * Builds a PackageLocation object based on the command info from the
 */
@Log
class PackageLocationBuilder {
    final File cacheDir

    PackageLocationBuilder(File cacheDir) {
        this.cacheDir = cacheDir
    }

    PackageLocation buildPackageLocation(String packageName, String version, List<PackageSource> packageSources) {
        if (UrlUtils.isUrl(packageName)) {
            return buildForUrl(packageName)
        }

        buildForSimple(packageName, version, packageSources)
    }

    private PackageLocation buildForUrl(String url) {
        def packageName = FilenameUtils.getBaseName(new URI(url).path)
        log.fine("entered buildForUrl")
        log.fine("url = ${url}")
        return new PackageLocation(remoteLocation: () -> new URL(url).newInputStream(), cacheLocation: cacheLocationPattern(packageName, null))
    }

    @Deprecated
    private PackageLocation buildForBintray(String packageName, String version, List<PackageSource> packageSources) {
        log.fine("entered buildForBintray")
        if (version) {
            String cacheLocation = cacheLocationPattern(packageName, version)
            File cacheFile = new File(cacheLocation)
            if (cacheFile.exists()) {
                return new PackageLocation(cacheLocation: cacheLocation)
            }
        }

        PackageInfo packageInfo = getPackageInfo(packageName, packageSources)
        String versionToDownload = version ?: packageInfo.latestVersion
        String cacheLocation = cacheLocationPattern(packageName, versionToDownload)

        return new PackageLocation(
                remoteLocation: packageInfo.source.getTemplateUrl(packageInfo.name, versionToDownload),
                cacheLocation: cacheLocation
        )
    }

    private PackageLocation buildForSimple(String packageName, String version, List<PackageSource> packageSources) {
        log.fine("entered buildForSimple")
        if (version) {
            String cacheLocation = cacheLocationPattern(packageName, version)
            File cacheFile = new File(cacheLocation)
            if (cacheFile.exists()) {
                return new PackageLocation(cacheLocation: cacheLocation)
            }
        }

        PackageInfo packageInfo = getPackageInfo(packageName, packageSources)
        String versionToDownload = version ?: packageInfo.latestVersion
        String cacheLocation = cacheLocationPattern(packageName, versionToDownload)
        def remoteSource = packageInfo.source.getRemoteSource(packageInfo.name, versionToDownload)

        return new PackageLocation(remoteLocation: remoteSource, cacheLocation: cacheLocation)
    }

    protected PackageInfo getPackageInfo(String packageName, List<PackageSource> packageSources) {
        for (PackageSource packageSource in packageSources) {
            log.fine "Searching for ${packageName} in ${packageSource}"

            def pkgInfo = packageSource.fetchPackageInfo(packageName)
            if (pkgInfo) {
                log.fine "Found!"
                return pkgInfo
            }
        }

        throw new PackageNotFoundException(packageName)
    }

    private String cacheLocationPattern(String name, String version) {
        cacheDir.absolutePath + '/' + name + (version ? '-' + version : '') + '.zip'
    }
}
