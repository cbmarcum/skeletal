package uk.co.cacoethes.lazybones

import co.freeside.betamax.Betamax
import co.freeside.betamax.Recorder
import io.netty.handler.codec.http.HttpRequest
import org.junit.Rule
import org.littleshoot.proxy.ChainedProxy
import org.littleshoot.proxy.ChainedProxyAdapter
import org.littleshoot.proxy.ChainedProxyManager
import org.littleshoot.proxy.HttpProxyServer
import org.littleshoot.proxy.ProxyAuthenticator
import org.littleshoot.proxy.impl.DefaultHttpProxyServer

class ProxyFunctionalSpec extends AbstractFunctionalSpec {

    HttpProxyServer proxy

    void setup() {
        proxy = DefaultHttpProxyServer.bootstrap().
                        withPort(0).
                withProxyAuthenticator(new ProxyAuthenticator() {
                    @Override
                    boolean authenticate(String userName, String password) {
                        return userName == "dummy" && password == "password"
                    }
                }).start()
        initProxy(proxy.listenAddress)

        filesToDelete << new File(cacheDirPath, "ratpack-0.1.zip")
        filesToDelete << new File(cacheDirPath, "aoo-addin-0.3.0.zip")
    }

    void cleanup() {
        proxy.stop()
    }

    def "Create command works with correct proxy credentials"() {
        given: "Correct proxy credentials"
        env["JAVA_OPTS"] += " -Dlazybones.systemProp.http.proxyUser=dummy"
        env["JAVA_OPTS"] += " -Dlazybones.systemProp.http.proxyPassword=password"

        when: "I run skeletal with the create command using a full URL for the aoo-addin template"
        def packageUrl = "https://codebuilders.jfrog.io/artifactory/generic/skeletal-templates/aoo-addin-template-0.3.0.zip"
        def exitCode = runCommand(["--verbose", "create", packageUrl, "test-addin", "-Pgroup=org.example", "-PartifactId=test-addin", "-Pversion=0.1.0", "-Ppackage=org.example", "-PclassName=TestAddin"], baseWorkDir)

        then: "It unpacks the template, retaining file permissions"
        exitCode == 0

        def appDir = new File(baseWorkDir, "test-addin")
        appDir.exists()
        new File(appDir, "gradlew").canExecute()
        new File(appDir, "src/main/groovy").isDirectory()
        new File(appDir, "src/main/groovy/org/example/TestAddinImpl.groovy").isFile()

        and: "It says that the latest version of the package is being installed in the target directory"
        output =~ /Creating project from template https:\/\/codebuilders.jfrog.io\/artifactory\/generic\/skeletal-templates\/aoo-addin-template-0.3.0.zip \(latest\) in 'test-addin'/

    }

    def "Create command triggers 407 if invalid proxy credentials provided"() {
        given: "Incorrect proxy credentials"
        env["JAVA_OPTS"] += " -Dlazybones.systemProp.http.proxyUser=dilbert"
        env["JAVA_OPTS"] += " -Dlazybones.systemProp.http.proxyPassword=password"

        when: "I run skeletal with the create command for the ratpack template"
        println "Env $env"
        println "Props ${System.properties}"
        def exitCode = runCommand(["create", "ratpack", "0.1", "ratapp"], baseWorkDir)

        then: "The command reports a 407 error"
        exitCode != 0
        output =~ /Proxy returns "HTTP\/1.1 407 Proxy Authentication Required"/
    }

    def "Create command triggers 407 if no proxy credentials provided"() {
        given: "a proxy that allows Basic authentication"

        when: "I run skeletal with the create command for the ratpack template"
        println "Env $env"
        println "Props ${System.properties}"
        def exitCode = runCommand(["create", "ratpack", "0.1", "ratapp"], baseWorkDir)

        then: "The command reports a 407 error"
        exitCode != 0
        output =~ /Proxy returns "HTTP\/1.1 407 Proxy Authentication Required"/
    }
}
