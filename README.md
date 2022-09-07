Skeletal Project Creation Tool
===============================
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

Our goal is to get Skeletal updated and distributed on SDKMAN. As that is not the 
case yet, We'll document the manual installation process as well.

The big changes from Lazybones are: 
1. The new project name and the application command are both now 'skeletal'.
2. The user profile directory for configurations and template cache is now $HOME/.skeletal
3. Bintray repositories are no longer available. Eventually this configuration will be removed.
4. A new URL based simple repository has been implemented that supports listing and creating projects from it.
Publishing to the simple repository is not yet implemented but only require copying the created 
published manifest file and the template packages to your URL repository.

Existing Lazybones templates should still work if moved to a URL repository as described below.

The tool is very simple: it allows you to create a new project structure for
any framework or library for which the tool has a template. 

The concept of Skeletal is very similar to Maven archetypes, and what [Yeoman](http://yeoman.io/)
does for web applications. Skeletal also includes a subtemplates feature that
resembles the behavior of Yeoman's sub-generators, allowing you to generate optional
extras (controllers, scaffolding etc.) inside a project.

## Documentation

Skeletal application usage and template development guides are located on our [wiki](https://github.com/cbmarcum/skeletal/wiki).

Information on building the application can be found below.

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


## Building

This project is split into three parts:

1. The [Skeletal command line tool](https://github.com/cbmarcum/skeletal/tree/master/lazybones-app)
2. The [Skeletal Gradle plugin](https://github.com/cbmarcum/skeletal/tree/master/lazybones-gradle-plugin) 
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

### Skeletal Gradle Plugin
The Gradle plugin that was a subproject in Lazybones has been moved into
its own [Skeletal Gradle Plugin](https://github.com/cbmarcum/skeletal-gradle-plugin)
project.

### Lazybones Project Templates

The project templates are simply directory structures with whatever files in
them that you want. Ultimately, the template project directories will be zipped
up and placed in a repository which is a directory at a URL (`file` or `http(s)`) 
that contains a `skeletal-manifest.txt` with entries for each template version 
available. From there, Skeletal downloads the zips on demand and caches them in 
a local user directory (currently `$HOME/.skeletal/templates`).

If you want empty directories to form part of the project template, then simply
add an empty `.retain` file to each one. When the template archive is created,
any `.retain` files are filtered out (but the containing directories are included).

Publishing templates previously included uploading the templates to your 
Bintray account. Since Bintray is no longer available, Skeletal has replaced 
this with creating a manifest file that contains the template metadata needed 
by the command line application for listing template information. This manifest 
file along with the template packages form a Simple URL based repository.

To publish a template contained in a template project created from the 
`lazybones-project` template, simply run this from the directory with the 
`build.gradle` file. Usually right above the `templates` directory that 
contains the templates which are the sub-directories. 

    ./gradlew publishTemplate<TemplateName>

For core templates in this project, run from the Skeletal project root 
directory and include the `lazybones-templates` sub-project:

    ./gradlew :lazybones-templates:publishTemplate<TemplateName>

The name of the project template comes from the containing directory, which is
assumed to be lowercase hyphenated. The template name is the equivalent camel
case form. So the template directory structure in `templates/my-template`
results in a template called 'MyTemplate', which can be published with:

    ./gradlew publishTemplateMyTemplate

The project template archive will be created in the build directory with the
name `<template name>-template-<version>.zip`. See the small section below on
how the template version is derived.

You can also publish all the templates in one fell swoop:

    ./gradlew publishAllTemplates

The manifest file `skeletal-manifest.txt` is just a CSV formatted text file 
with a header and entries for each template version you want to make available 
for Skeletal like:

    name,version,owner,description
    aoo-addin-java-template,0.3.0,"Code Builders, LLC","Apache OpenOffice Add-In Template for Java"
    aoo-addin-template,0.3.0,"Code Builders, LLC","Apache OpenOffice Add-In Template for Groovy"
    aoo-addon-java-template,0.3.0,"Code Builders, LLC","Apache OpenOffice Add-On Template for Java"
    aoo-addon-template,0.3.0,"Code Builders, LLC","Apache OpenOffice Add-On Template for Groovy"
    aoo-client-template,0.3.0,"Code Builders, LLC","Apache OpenOffice Client Template for Groovy"

The template packages and manifest are output to the `build/packages`. Then you need 
to place the manifest and template zip files at the URL you use for your repository.

If you only want to create the packages without the manifest file you can use 
the package tasks

    ./gradlew packageTemplate<TemplateName>
and:

    ./gradlew packageAllTemplates

If you don't want to publish your template at a URL you can install it locally 
using the installTemplate rule.

     ./gradlew installTemplate<TemplateName>

This will install the template to `$HOME/.skeletal/templates` so that you can use 
it without moving it to a URL first. To list them you will need to use the 
`skeletal list --cached` command since they are not in a repository also.

And that's it for the project templates.

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

That's it! The `VERSION` and `DESCRIPTION` files will automatically be excluded from the project
template archive.

## Contributing

Skeletal is written in [Apache Groovy](https://groovy.apache.org/), builds 
with [Gradle](https://gradle.org/), and we use [Spock](https://spockframework.org/) 
for testing. 

While I've done my best to get Lazybones forked and up and running there is 
always more to do.

Contributors are welcome and contributions are appreciated. If you have questions 
about contributing you can drop a question in the [issues](https://github.com/cbmarcum/skeletal/issues) 
or email me at carl dot marcum at codebuilders dot net.
