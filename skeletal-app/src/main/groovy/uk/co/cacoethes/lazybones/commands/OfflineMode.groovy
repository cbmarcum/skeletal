package uk.co.cacoethes.lazybones.commands

import java.net.ConnectException

import java.util.logging.Level
import java.util.logging.Logger

/**
 * A collection of static methods to ensure consistency between all the commands
 * when Skeletal is run offline.
 */
class OfflineMode {
    static boolean isOffline(ex) {

        if (ex instanceof ConnectException) {
            return true
        } else {
            return false
        }
    }

    @SuppressWarnings("ParameterReassignment")
    static void printlnOfflineMessage(Throwable ex, Logger log, boolean stacktrace) {

        println "(Offline mode - run with -v or --stacktrace to find out why)"
        log.fine "(Error message: ${ex.class.simpleName} - ${ex.message})"
        if (stacktrace) log.log Level.WARNING, "", ex
        println()
    }
}
