package uk.co.cacoethes.lazybones.commands

import spock.lang.Specification
import spock.lang.TempDir
import uk.co.cacoethes.lazybones.LazybonesMain
import uk.co.cacoethes.lazybones.LazybonesScript

/**
 * @author Tommy Barker
 */
class InstallationScriptExecuterSpec extends Specification {

    @TempDir
    File testFolder

    void "lazybones gets deleted after running"() {
        given: "a create command instance"
        def cmd = new InstallationScriptExecuter()

        when: "I run lazybones.groovy"
        File file = new File(testFolder, "lazybones.groovy")
        file.write("System.setProperty('ran','true')")
        cmd.runPostInstallScript([], testFolder, testFolder, [:])

        then: "the script is deleted"
        !file.exists()
        'true' == System.getProperty("ran")

        cleanup: "sets the system properties back the way they were"
        System.properties.remove("ran")
    }

    void "lazybones has the projectDir set before running"() {
        given: "a create command instance"
        InstallationScriptExecuter cmd = new InstallationScriptExecuter()

        when: "when I run lazybones.groovy"
        File file = new File(testFolder, "lazybones.groovy")
        file.write("//do nothing")
        LazybonesScript script = cmd.initializeScript([:], [], file, testFolder, testFolder)

        then: "the targetDir is set"
        // script.getProjectDir() == testFolder.root
        script.getProjectDir() == testFolder
    }

    void "if lazybones does not exist, nothing happens"() {
        given: "a create command instance"
        def cmd = new InstallationScriptExecuter()

        when: "I runLazyBonesIfExists and file does not exist, nothing happens"
        File file = new File("foobar")

        then: "nothing happens"
        !file.exists()
        null == cmd.runPostInstallScript([], testFolder, testFolder, [:])
    }
}
