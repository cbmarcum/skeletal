package uk.co.cacoethes.lazybones.commands

import java.util.concurrent.Callable

/**
 * Data class representing the location of a template package in a remote
 * repository and its corresponding location in the Lazybones cache.
 */
class PackageLocation {
    Callable<InputStream> remoteLocation
    String cacheLocation
}
