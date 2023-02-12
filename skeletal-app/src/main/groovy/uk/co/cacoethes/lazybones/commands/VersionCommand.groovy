package uk.co.cacoethes.lazybones.commands

import groovy.transform.CompileStatic
import groovy.util.logging.Log
import joptsimple.OptionSet
import uk.co.cacoethes.lazybones.config.Configuration

/**
 *
 */
@CompileStatic
@Log
class VersionCommand extends AbstractCommand {
    static final String USAGE = """\
USAGE: version 

  The command displays the application name and version.
"""

    @Override
    String getName() { return "version" }

    @Override
    String getDescription() {
        return "Displays the application name and version."
    }

    @Override
    protected IntRange getParameterRange() {
        return 0..1
    }

    @Override
    protected String getUsage() { return USAGE }

    @Override
    protected int doExecute(OptionSet cmdOptions, Map globalOptions, Configuration config) {
        def cmdArgs = cmdOptions.nonOptionArguments()
        if (cmdArgs) {
            // there should be no args
            println "There should be no arguments with version"
        }
        else {
            showVersion(config)
        }

        return 0
    }

    protected void showVersion(Configuration config) {

        String version = ""
        version = this.class.getPackage().getImplementationVersion()
        println "Skeletal v${version}"
        println ""
    }

}
