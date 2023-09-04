package uk.co.cacoethes.lazybones

import co.freeside.betamax.Betamax
import org.apache.commons.io.FileUtils
import spock.lang.Ignore
import spock.lang.Unroll

class OfflineFunctionalSpec extends AbstractFunctionalSpec {

    def setup() {
        // Create dummy proxy to ensure the app can't access the internet.
        initProxy(new InetSocketAddress("localhost", 61431))
    }

    // @Betamax(tape="create-tape")
    @Unroll
    def "list command prints local packages and mappings when offline #showsExceptionLabel#extraOutputLabel"() {
        given: "A template in the cache matching a remote one"
        def ratpackPackage = new File(cacheDirPath, "ratpack-0.1.zip")
        if (!ratpackPackage.exists()) {
            FileUtils.touch(ratpackPackage)
            filesToDelete << ratpackPackage
        }

        when: "I run skeletal with the list command"
        def exitCode = runCommand(otherOptions + ["list"], baseWorkDir)

        then: "It displays only the template mappings and cached packages"
        exitCode == 0
        output =~ /(?m)^Available mappings\s+/ +
                /\s+customRatpack   -> http:\/\/dl.dropboxusercontent.com\/u\/29802534\/custom-ratpack.zip\s+/ +
                /\s+doesNotExist    -> file:\/\/\/does\/not\/exist\s+/ +
                /\s+customAooAddin  -> https:\/\/skeletal.s3.us-east-2.amazonaws.com\/openoffice-templates\/aoo-addin-template-0.3.0.zip\s+/

        output =~ /(?m)^Cached templates\s+/ +
                /Oops-stuff                    1.0.4\s+/ +
                /ratpack                       0.1\s+/ +
                /subtemplates-tmpl             0.1\s+/ +
                /test-handlebars               0.1.1\s+/ +
                /test-handlebars-default       0.1\s+/ +
                /test-no-default-engine        0.1\s+/ +
                /test-tmpl                     0.2\s+/ +
                /test-tmpl-subscripts          0.2\s+/

        and: "It displays an offline message, with optional explanation and stacktrace"
        output =~ /(?m)\(Offline mode - run with -v or --stacktrace to find out why\)\s+/ +
                extraOutput + (hidesException ? "" : /java\.net\.ConnectException.*/)

        where:
            otherOptions    |   hidesException  |   extraOutput
                []          |       true        |       ""
              ["-v"]        |       true        |   /\(Error message: ConnectException - Connection refused.*\)\s+/
          ["--stacktrace"]  |       false       |       ""

        showsExceptionLabel = hidesException ? "without stacktrace" : "with stacktrace"
        extraOutputLabel = extraOutput ? " and with the error message" : ""
    }

    @Unroll
    def "info command displays error when offline #showsExceptionLabel#extraOutputLabel"() {
        when: "I run lazybones with the info command"
        def exitCode = runCommand(otherOptions + ["info", "afterburnerfx"], baseWorkDir)

        then: "It errors out"
        exitCode != 0

        and: "It displays an offline message, with optional explanation and stacktrace"
        output =~ /(?m)\(Offline mode - run with -v or --stacktrace to find out why\)\s+/ +
                extraOutput + (hidesException ? "" : /.*^java\.net\.ConnectException.*/)
        output =~ /(?m)^Cannot fetch package info/

        where:
            otherOptions    |   hidesException  |   extraOutput
                []          |       true        |       ""
              ["-v"]        |       true        |   /\(Error message: ConnectException - Connection refused.*\)\s+/
          ["--stacktrace"]  |       false       |       ""

        showsExceptionLabel = hidesException ? "without stacktrace" : "with stacktrace"
        extraOutputLabel = extraOutput ? " and with the error message" : ""
    }

    @Unroll
    def "create command fails gracefully when offline and it needs internet #showsExceptionLabel#extraOutputLabel"() {
        when: "I run the create command without a package version"
        def exitCode = runCommand(otherOptions + ["create", "aoo-addin", "0.3.0", "test-addin", "-Pgroup=org.example", "-PartifactId=test-addin", "-Pversion=0.1.0", "-Ppackage=org.example", "-PclassName=TestAddin"], baseWorkDir)

        then: "It errors out"
        exitCode != 0

        and: "It displays an offline message, with optional explanation and stacktrace"
        output =~ /(?m)\(Offline mode - run with -v or --stacktrace to find out why\)\s+/ +
                extraOutput + (hidesException ? "" : /.*^java\.net\.ConnectException.*/)
        output =~ /(?m)^Cannot create a new project when the template isn't locally cached or no version is specified/

        where:
            otherOptions    |   hidesException  |   extraOutput
                []          |       true        |       ""
              ["-v"]        |       true        |   /\(Error message: ConnectException - Connection refused.*\)\s+/
          ["--stacktrace"]  |       false       |       ""

        showsExceptionLabel = hidesException ? "without stacktrace" : "with stacktrace"
        extraOutputLabel = extraOutput ? " and with the error message" : ""
    }
}
