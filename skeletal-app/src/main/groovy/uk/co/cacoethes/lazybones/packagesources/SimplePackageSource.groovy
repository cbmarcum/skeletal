package uk.co.cacoethes.lazybones.packagesources

import groovy.util.logging.Log
import sun.net.www.protocol.https.HttpsURLConnectionImpl
import uk.co.cacoethes.lazybones.NoVersionsFoundException
import uk.co.cacoethes.lazybones.PackageInfo
import uk.co.cacoethes.lazybones.SimplePackageBean

// http://opencsv.sourceforge.net/
// http://opencsv.sourceforge.net/apidocs/index.html
import com.opencsv.CSVReader
import com.opencsv.bean.CsvToBeanBuilder
import com.opencsv.bean.CsvBindByName
import uk.co.cacoethes.util.VersionUtils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/**
 * A simple location for Skeletal packaged templates at a URL.
 * This class requires a CSV file manifest in the template repository for
 * information on what packages are available and to get extra information
 * about them.
 */
@Log
class SimplePackageSource implements PackageSource {

    static final String PACKAGE_SUFFIX = "-template"

    final String repoName

    SimplePackageSource(String repositoryName) {
        repoName = repositoryName

    }


    /**
     * Returns a list of the available packages. If there are no packages, this
     * returns an empty list.
     */
    List<String> listPackageNames() {

        List<String> result = []

        URL url = null
        BufferedReader reader = null

        // create the URLConnection
        // TODO: may need fixed for windows file urls unless we can use forward slash with java like ant
        url = new URL("${repoName}/skeletal-manifest.txt")
        URLConnection connection = url.openConnection() // removed URLConnection cast

        // uncomment this if you want to write output to this url
        //connection.setDoOutput(true)

        // give it 15 seconds to respond
        connection.setReadTimeout(15 * 1000)
        connection.connect()

        // read the output from the server
        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))

        // new escape char since we have some \ characters in text
        List<SimplePackageBean> beans = new CsvToBeanBuilder(reader)
                .withSeparator(',' as char)
                .withQuoteChar('"' as char)
                .withEscapeChar('~' as char)
                .withIgnoreEmptyLine(true)
                .withType(SimplePackageBean.class).build().parse()

        for (bean in beans) {
            // log.info(bean.toString())
            bean.name = bean.name - PACKAGE_SUFFIX
            result << bean.name
        }
        return result
    }

    /**
     * Returns details about a given package. If no package is found with the
     * given name, this returns {@code null}.
     */
    @SuppressWarnings("ReturnNullFromCatchBlock")
    PackageInfo fetchPackageInfo(String pkgName) {

        def pkgNameWithSuffix = pkgName + PACKAGE_SUFFIX
        List<SimplePackageBean> packages = []

        URL url = null
        BufferedReader reader = null

        // create the URLConnection
        // TODO: may need fixed for windows file urls unless we can use forward slash with java like ant
        url = new URL("${repoName}/skeletal-manifest.txt")

        URLConnection connection = url.openConnection() // Test remove URLConnection cast

        // uncomment this if you want to write output to this url
        //connection.setDoOutput(true)

        // only http(s) connections can redirect
        if (connection instanceof HttpURLConnection) {
            connection.setFollowRedirects(true) // our new artifactory repo may have redirects
        }

        // give it 15 seconds to respond
        connection.setReadTimeout(15 * 1000)

        try {
            connection.connect()
        } catch (java.net.ConnectException connectException) {
            throw connectException
        }

        // read the output from the server
        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))

        // new escape char since we have some \ characters in text
        List<SimplePackageBean> beans = new CsvToBeanBuilder(reader)
                .withSeparator(',' as char)
                .withQuoteChar('"' as char)
                .withEscapeChar('~' as char)
                .withIgnoreEmptyLine(true)
                .withType(SimplePackageBean.class).build().parse()

        for (SimplePackageBean bean in beans) {
            bean.name = bean.name - PACKAGE_SUFFIX
            if (bean.name == pkgName) {
                packages << bean
            }
        }

        if (!packages) {
            return null
        }

        PackageInfo pkgInfo

        if (packages.size() == 1) {
            pkgInfo = new PackageInfo(this, packages[0].name, packages[0].version)
            pkgInfo.with {
                versions = [packages[0].version]
                owner = packages[0].owner
                description = packages[0].description
            }
            pkgInfo.url = getTemplateUrl(pkgInfo.name, pkgInfo.versions[0]) // url to latest package

        } else { // more than 1
            pkgInfo = new PackageInfo(this, packages[0].name)
            // make a list of versions sorted low to high
            pkgInfo.versions = VersionUtils.sortVersions(packages*.version)
            // add the highest to latestVersion
            pkgInfo.latestVersion = VersionUtils.mostRecentVersion(pkgInfo.versions)

            // use the highest version package for owner and description
            SimplePackageBean pkg = packages.find { it.version == pkgInfo.latestVersion }
            pkgInfo.owner = pkg.owner
            pkgInfo.description = pkg.description
            pkgInfo.url = getTemplateUrl(pkgInfo.name, pkgInfo.latestVersion) // url to latest package
        }

        return pkgInfo
    }

    /**
     * Returns the URL to download particular package and version from this package source
     */
    String getTemplateUrl(String pkgName, String version) {
        // TODO: may need fixed for windows file urls unless we can use forward slash with java like ant
        String url = "${repoName}/${pkgName}${PACKAGE_SUFFIX}-${version}.zip"
        return url
    }
}
