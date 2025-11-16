Skeletal Project Creation Tool
===============================

[![Join the chat at https://gitter.im/skeletal-app/community](https://badges.gitter.im/skeletal-app/community.svg)](https://gitter.im/skeletal-app/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

## Quick Links
* [CLI Application Installation](https://cbmarcum.github.io/skeletal/application-users-guide.html#_installation)
* [Applying the Gradle Plugin](https://github.com/cbmarcum/skeletal-gradle-plugin)
* [User Guides](https://cbmarcum.github.io/skeletal/index.html)

## Introduction

**Skeletal is a tool that allows you to create a new project structure for
any framework or library for which the tool has a template.**

The concept of Skeletal is very similar to Maven archetypes, and what [Yeoman](http://yeoman.io/)
does for web applications. Skeletal also includes a subtemplates feature that
resembles the behavior of Yeoman's sub-generators, allowing you to generate optional
extras (controllers, scaffolding etc.) inside a project.

It consists of a command line application, a Gradle plugin for publishing templates, and a Lazybones Project template for template developers and some other core templates for developers.

Skeletal is a fork of the popular [Lazybones](https://github.com/pledbrook/lazybones) 
project created by Peter Ledbrook. Though unmaintained for some time, Lazybones 
continued to be used by other projects as a means to generate project layouts 
from templates and it was distributed on [SDKMAN](https://sdkman.io/) which made it 
more convenient to install and use. This came to an end with the [shutdown of Bintray.](https://jfrog.com/blog/into-the-sunset-bintray-jcenter-gocenter-and-chartcenter/)
 in May 2021.  Since this is where the application binaries and "built-in" templates lived,  SDKMAN stopped distribution as the binaries were no longer available.

We will continue to refer to the templates as Lazybones templates because so many 
projects have been created from them and that is how they are best known. The 
template structure has not been changed, except to add a description file, so 
we see no reason to rebrand what the templates are called.

The big changes from Lazybones are: 
1. The new project name and the application command are both now 'skeletal'.
2. The user profile directory for configurations and template cache is now $HOME/.skeletal
3. Bintray repositories are no longer available. Eventually this configuration will be removed.
4. A new URL based simple repository has been implemented that supports listing and creating projects from it.
Publishing to the simple repository is not yet implemented but only require copying the created 
published manifest file and the template packages to your URL repository.

Existing Lazybones templates should still work if moved to a URL repository as described below.

## Documentation

Skeletal application usage and template development guides are located in our [project documention pages](https://cbmarcum.github.io/skeletal/index.html).

Information on building the application can be found below.



## Building

This project is split into three parts:

1. The [Skeletal command line tool](https://github.com/cbmarcum/skeletal/tree/master/skeletal-app)
2. The [Skeletal Gradle plugin](https://github.com/cbmarcum/skeletal-gradle-plugin) 
3. The [project templates](https://github.com/cbmarcum/skeletal/tree/master/lazybones-templates)

### Command Line Tool

The command line tool is created via Gradle's application plugin. The main
class is `uk.co.cacoethes.lazybones.LazyBonesMain`, which currently implements
all the sub-commands (create, list, etc.) as command classes.

The main class plus everything else under src/main is packaged into a `skeletal-app-<version>.jar` 
that is included in the distribution zip. The Gradle application plugin generates 
a `skeletal` shell script and a `skeletal.bat` script that then runs the main 
class with all required dependencies on the classpath.

To build the distribution, from the skeletal project top level directory simply 
run:

    ./gradlew distZip

You will find the application packaged in `build/distributions/skeletal-<version>.zip`

Unpack the zip file contents to anywhere you keep such applications and add the 
`bin` subdirectory to your system path to get the `skeletal` command.  
The `$HOME/.skeletal` profile directory will be created after the first `create` 
or `config` command is ran.

You can build an "installed" build for manual testing locally before production.

    ./gradlew installDist

You can run it using the path to the application script

    skeletal-app/build/install/skeletal/bin/skeletal <command>

#### Automated Tests
We use [Spock](https://spockframework.org/) for tests (specifications). 
There are approximatly 300 unit or integration tests currently ran during a build of the distribution.

To run the tests alone 

    ./gradlew check

Gradle reports are found in `build/reports/tests`. We have recently added [Athaydes Spock Reports](https://github.com/renatoathaydes/spock-reports) but we haven't done much with the configuration of those yet.  Those reports are found in `build/spock-reports`

### Skeletal Gradle Plugin
The Gradle plugin that was a subproject in Lazybones has been moved into
its own [Skeletal Gradle Plugin](https://github.com/cbmarcum/skeletal-gradle-plugin)
project.

### Lazybones Project Templates

#### Template Versions

You define the version of a template by putting a VERSION file in the root
directory of the template that contains just the version number. For example,
you specify a version of 1.2.8 for the my-template template by adding the file
`templates/my-template/VERSION` with the contents

    1.2.8

#### Template Description

Skeletal requires a `DESCRIPTION` file that contains a description for the
template.

    A project for managing Lazybones project templates.

For core templates in this project, run from the Skeletal project root 
directory and include the `lazybones-templates` sub-project:

    ./gradlew :lazybones-templates:publishTemplate<TemplateName>

You can also publish all the templates in one fell swoop:

    ./gradlew :lazybones-templates:publishAllTemplates

That's it! The `VERSION` and `DESCRIPTION` files will automatically be excluded from the project
template archive.

More complete information about template publishing can be found in the
[project documention pages](https://cbmarcum.github.io/skeletal/index.html) and 
the [Skeletal Gradle Plugin](https://github.com/cbmarcum/skeletal-gradle-plugin)
project.

## Creating a Release
Releases are created in the GitHub repo by creating a tag and a Release based on that tag and a Changelog of major changes and uploading build artifacts and source archives.

This is automated by [JReleaser](https://jreleaser.org/) using the `jreleaser.yml` configuration file and the [JReleaser CLI](https://jreleaser.org/guide/latest/tools/jreleaser-cli.html).

### Pre-Release Checklist
1. App version is correct in `skeletal-app/app.gradle`, `jreleaser.yml`, and `docs/application-users-guide.adoc` files.
2. Recreate `docs/application-users-guide.html` file with IntelliJ AsciiDoc plugin.
3. Build distribution with `./gradlew distZip`
4. Verify all tests passed.

### Steps to Create a Release
1. `export JRELEASER_OUTPUT_DIRECTORY=skeletal-app/build`
2. `jreleaser config`
3. `jreleaser full-release --dry-run`
4. Check `skeletal-app/build/jreleaser/release/CHANGELOG.md` for errors
5. `jreleaser full-release`

## Contributing

Skeletal is written in [Apache Groovy](https://groovy.apache.org/), builds 
with [Gradle](https://gradle.org/), and we use [Spock](https://spockframework.org/) 
for testing.

Contributors are welcome and contributions are appreciated. If you have questions 
about contributing you can drop a question in the [issues](https://github.com/cbmarcum/skeletal/issues) 
or email carl dot marcum at codebuilders dot net.

## Credits

### Skeletal Developer

* [Carl Marcum](https://github.com/cbmarcum)

### Skeletal Contributors

* [Osian Hughes](https://github.com/osh-onstructive)
* [Dónal Murtagh](https://github.com/donalmurtagh)

### Lazybones Developers

* [Peter Ledbrook](https://github.com/pledbrook)
* [Kyle Boon](https://github.com/kyleboon)
* [Tommy Barker](https://github.com/tbarker9)

### Lazybones Contributors

* [Luke Daley](https://github.com/alkemist)
* [Tomas Lin](https://github.com/tomaslin)
* [Russell Hart](https://github.com/rhart)
* [Dave Syer](https://github.com/dsyer)
* [Andy Duncan](https://github.com/andyjduncan)
