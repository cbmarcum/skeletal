package uk.co.cacoethes.lazybones.commands

import spock.lang.Specification
import spock.lang.TempDir
import uk.co.cacoethes.lazybones.config.Configuration

/**
 * Created by tbarker on 12/18/13.
 */
class CreateCommandSpec extends Specification {

    @TempDir
    File testFolder

    File testCacheDir

    void setup() {
        testCacheDir = new File(testFolder, "templates")
    }

    void "package name is replaced with url"() {
        given:
        def config = initConfig(
                templates: [mappings: [foo: "http://bar.com"]],
                // cache: [dir: new TemporaryFolder().newFolder().absolutePath])
                cache: [dir: testCacheDir.absolutePath])

        when:
        def createInfo = new CreateCommand(config).getCreateInfoFromArgs(["foo", "bar"])

        then:
        "http://bar.com" == createInfo.packageArg.templateName
    }

    protected Configuration initConfig(Map settings) {
        return new Configuration(
                new ConfigObject(),
                settings,
                [:],
                ["templates.mappings.*": URI,
                 "cache.dir": String],
                new File("delete-me.json"))
    }
}
