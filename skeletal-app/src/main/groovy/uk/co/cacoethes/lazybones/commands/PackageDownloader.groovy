package uk.co.cacoethes.lazybones.commands

import groovy.transform.CompileStatic
import groovy.util.logging.Log
import uk.co.cacoethes.lazybones.PackageNotFoundException

/**
 * Handles the retrieval of template packages from supported repositories.
 */
@CompileStatic
@Log
class PackageDownloader {

    File downloadPackage(PackageLocation packageLocation, String packageName, String version) {
        def packageFile = new File(packageLocation.cacheLocation)

        if (!packageFile.exists()) {
            packageFile.parentFile.mkdirs()

            // The package info may not have been requested yet. It depends on
            // whether the user specified a specific version or not. Hence we
            // try to fetch the package info first and only throw an exception
            // if it's still null.
            //
            // There is an argument for having getPackageInfo() throw the exception
            // itself. May still do that.
            log.fine "${packageLocation.cacheLocation} is not cached locally. Searching the repositories for it."

            try {
                packageFile.withOutputStream { OutputStream out ->
                    packageLocation.remoteLocation.call().withCloseable { input ->
                        out << input
                    }
                }
            }
            catch (FileNotFoundException ex) {
                packageFile.deleteOnExit()
                throw new PackageNotFoundException(packageName, version, ex)
            }
            // amazon s3 throws IOException HTTP response 403
            catch (IOException ex) {
                packageFile.deleteOnExit()
                throw new PackageNotFoundException(packageName, version, ex)
            }
            catch (all) {
                packageFile.deleteOnExit()
                throw all
            }
        }

        return packageFile

    }
}
