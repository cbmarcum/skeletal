package uk.co.cacoethes.util

import groovy.transform.CompileStatic

/**
 * Based on code contributed by Dónal Murtagh
 */
@CompileStatic
class VersionUtils {

    /**
     * Returns a sorted list of version strings.
     * Requires an integer based semantic versioning scheme using 1 to many parts seperated by decimals.
     * Ex.  ["1", "0.10, "0.1.0", "0.3.0"]
     * @param versions A list of versions
     * @return A sorted list of versions from oldest to most recent by highest integer comparison per part.
     */
    static List<String> sortVersions(List<String> versions) {

        def sorted = versions.sort(false) { String a, String b ->

            List verA = a.tokenize('.')
            List verB = b.tokenize('.')

            def commonIndices = Math.min(verA.size(), verB.size())

            for (int i = 0; i < commonIndices; ++i) {
                def numA = verA[i].toInteger()
                def numB = verB[i].toInteger()
                println "comparing $numA and $numB"

                if (numA != numB) {
                    return numA <=> numB
                }
            }

            // If we got this far then all the common indices are identical, so whichever version is longer must be more recent
            verA.size() <=> verB.size()
        }

        // println "sorted versions: $sorted"
        return sorted
    }


    /**
     * Returns the most recent version from a list of version strings.
     * Requires an integer based semantic versioning scheme using 1 to many parts seperated by decimals.
     * Ex.  ["1", "0.10, "0.1.0", "0.3.0"]
     * @param versions A list of versions
     * @return The most recent version by highest integer comparison per part.
     */
    static String mostRecentVersion(List versions) {

        List<String> sorted = sortVersions(versions)
        // return last element
        sorted[-1]
    }





}
