package uk.co.cacoethes.lazybones

class VersionFunctionalSpec extends AbstractFunctionalSpec {

    def "version command prints out a version"() {
        when: "I run skeletal version"
        def exitCode = runCommand(['version'], baseWorkDir)

        then: "It displays the version"
        exitCode == 0
        output.startsWith("Skeletal v")
        output =~ /Skeletal v[0-9]/
    }

    def "help option before command prints out the command specific help"() {
        when: "I run skeletal with help option before version command"
        def exitCode = runCommand([option, command], baseWorkDir)

        then: "It displays the help"
        exitCode == 0
        output.contains("USAGE")
        output =~ /\s+${command}\s+/
        !(output =~ /Exception/)

        where:
        option  | command
        "-h"    | "version"
        "--help"| "version"

    }

    def "help option after version command prints the command specific help"() {
        when: "I run skeletal with version command and help option"
        def exitCode = runCommand([command, option], baseWorkDir)

        then: "It displays the help"
        exitCode == 0
        output.contains("USAGE")
        output =~ /\s+${command}\s+/ &&
        !(output =~ /Exception/)
        println output

        where:
        option  | command
        "-h"    | "version"
        "--help"| "version"
    }

}
