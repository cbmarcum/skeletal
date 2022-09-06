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

## Skeletal Gradle Plugin
The Gradle plugin that was a subproject in Lazybones has been moved into 
its own [Skeletal Gradle Plugin](https://github.com/cbmarcum/skeletal-gradle-plugin) 
project.

## Skeletal Developer

* [Carl Marcum](https://github.com/cbmarcum)

## Skeletal Contributors

* [Osian Hughes](https://github.com/osh-onstructive)
* [Dónal Murtagh](https://github.com/donalmurtagh)

## Lazybones Developers

* [Peter Ledbrook](https://github.com/pledbrook)
* [Kyle Boon](https://github.com/kyleboon)
* [Tommy Barker](https://github.com/tbarker9)

## Lazybones Contributors

* [Luke Daley](https://github.com/alkemist)
* [Tomas Lin](https://github.com/tomaslin)
* [Russell Hart](https://github.com/rhart)
* [Dave Syer](https://github.com/dsyer)
* [Andy Duncan](https://github.com/andyjduncan)


## Running Skeletal

### Installation

Grab the distribution [from our JFrog repo](https://codebuilders.jfrog.io/artifactory/generic/skeletal-app) or use curl:

    curl -o skeletal-<version>.zip -L https://codebuilders.jfrog.io/artifactory/generic/skeletal-app/<version>/skeletal-<version>.zip

Unpack it to a local directory, and then add its 'bin' directory to your `PATH`
environment variable.

### Finding Available Templates

To see what templates you can install, run

    skeletal list
 
This will list all aliases and remote templates. If you want to see what
templates you have cached locally, run

    skeletal list --cached

In fact, `--cached` is implied if Lazybones can't connect to the internet.

Example

    skeletal list

    Available templates in https://codebuilders.jfrog.io/artifactory/generic/skeletal-templates

    aoo-addin
    aoo-addin-java
    aoo-addon
    aoo-addon-java
    aoo-client

You can also find out more about a template through the `info` command:

    skeletal info <template name>

This will print a description of the template and what versions are available
for it. If you're offline, this will simply display an error message.

Example
    
    skeletal info aoo-client

    Fetching package information for 'aoo-client' from repo
    Name:        aoo-client
    Latest:      0.3.0
    Description: Apache OpenOffice Client Template for Groovy
    Owner:       Code Builders, LLC
    Versions:    0.3.0

    More information at https://codebuilders.jfrog.io/artifactory/generic/skeletal-templates


### Creating Projects

To create a new project, run

    skeletal create <template name> <template version> <target directory>

So if you wanted to create a skeleton Apache OpenOffice Client project in a new 'my-aoo-client-app'
directory you would run

    skeletal create aoo-client 0.3.0 my-aoo-client-app
    
The version is optional and if you leave it out, Skeletal will install the
latest version of the template it can find. 

The default templates are listed from Code Builders, LLC's Artifactory account hosted by JFrog at 
https://codebuilders.jfrog.io/artifactory/generic/skeletal-templates. Skeletal searches for
templates at this URL by default, but you can use other URL repositories by adding some configuration. 
See the Custom Repositories section under Configuration later in this document.

You're not limited to only the default repository as you can install templates directly from 
a URL also:

    skeletal create https://codebuilders.jfrog.io/artifactory/generic/skeletal-templates/aoo-client-0.3.0.zip my-aoo-client-app

Of course, it can be pretty laborious copying and pasting URLs around, so Skeletal
allows you to configure aliases for URLs that you use frequently. By adding the
following configuration to your Skeletal settings file, `~/.skeletal/config.groovy`
(see below for more details on this), you can install the template by name:

    templates {
        mappings {
            myTmpl = "https://codebuilders.jfrog.io/artifactory/generic/skeletal-templates/aoo-client-0.3.0.zip"
        }
    }

In other words, you could now run

    skeletal create myTmpl my-app

Note that when using the URL option, there is no need to specify a version. You
should also be aware that mappings take precedence, i.e. if a mapping has the
same name as an existing template, the mapping is used. This essentially creates
a simple override mechanism.

There is just one more thing to say about the `create` command: by default it
creates the specified directory and puts the initial project in there. If you
want to unpack a template in the current directory instead, for example if you
have already created the project directory, then just pass '.' as the directory:

    skeletal create ratpack .

Once you have created a new project from a template, you may notice that the
project directory contains a .lazybones sub-directory. You may delete this, but
then you won't be able to use the `generate` command (see next section) if the
project template has support for it.

Many project templates request information from you, such as a project name, a
group ID, a default package, etc. If this is the umpteenth time you have created
a project from a given template, then answering the questions can become tedious.
There is also the problem of scripting and automation when you want to create
a project without user intervention. The solution to both these issues is to
pass the values on the command line:

    skeletal create ratpack 1.2.0 ratapp -Pgroup=org.example -Ppackage=org.example.myapp

The `-P` option allows you to pass property values into the project templates
without user intervention. The key is to know what the property names are, and
that comes down to the project template. At the moment, the best way to find out
what those properties are is to look at the post-install script itself.

The last option to mention is `--with-git` which will automatically create a
new git repository in the project directory. The only requirement is that you
have the `git` command on your path.

### Subtemplates

Project templates can incorporate subtemplates.
Imagine that you have just created a new web application project from a template
and that template documents that you can create new controllers using a sub-
template named `controller`. To use it, just `cd` into the project directory
and run

    skeletal generate controller
    
This will probably ask you for the name of the controller and its package before
generating the corresponding controller file in your project. You can reuse the
command to create as many controllers as you need.

As with the `create` command, you can also pass in property values on the command
line if the subtemplate is parameterised:

    skeletal generate controller -Ppackage=org.example.myapp -Pclass=Book
    
The last option available to you as a user is template qualifiers. These only
work if the subtemplate supports them, but they allow you to pass additional
information in a concise way:

    skeletal generate artifact::controller
    
In this case, the template name is `artifact`, but we have qualified it with
an extra `controller`. You can pass in as many qualifiers as you want, you just
separate them with `::`.

Note that you do not specify a version with the `generate` command. This is
because the subtemplates are embedded directly in the project template, and
so there can only be one version available to you.

## Configuration

Skeletal will run out of the box without any extra configuration, but the tool
does allow you to override the default behaviour via a fixed set of configuration
options. These options can be provided in a number of ways following a set order
of precedence:

1.   System properties of the form `lazybones.*`, which can be passed into the app
via either `JAVA_OPTS` or `LAZYBONES_OPTS` environment variables. For example:

    env JAVA_OPTS="-Dlazybones.config.file=/path/to/my-custom-default-config.groovy" lazybones ...

Highest precedence, i.e. it overrides all other sources of setting data.

2.   User configuration file in `$USER_HOME/.skeletal/config.groovy`. This is parsed
using Groovy's `ConfigSlurper`, so if you're familiar with that syntax you'll be
right at home. Otherwise, just see the examples below.

3.   A JSON configuration file in `$USER_HOME/.skeletal/managed-config.groovy`
that is used by the `config` commands. You can edit it this as well.

4.   A Groovy-based default configuration file that is provided by the application
itself, but you can specify an alternative file via the `lazybones.config.file`
system property.

Skeletal also provides a convenient mechanism for setting and removing options
via the command line: the `config` command.

### Command Line Configuration

The `config` command provides several sub-commands that allow you to interact with
the persisted Skeletal configuration; specifically, the JSON config file. You
run a sub-command via

    skeletal config <sub-cmd> <args>

where `<sub-cmd>` is one of:

*   `set <option> <value> [<value> ...]`

    Allows you to change the value of a configuration setting. Multiple values are
    treated as a single array/list value. The new value replaces any existing one.

*   `add <option> <value>`

    Appends an extra value to an existing array/list setting. Reports an error if
    the setting doesn't accept multiple values. If the setting doesn't already have
    a value, this command will initialise it with an array containing the given
    value.

*   `clear <option>`

    Removes a setting from the configuration, effectively reverting it to whatever
    the internal default is.

*   `show [--all] <option>`

    Shows the current value of a setting. You can use the `--all` argument (without
    a setting name) to display all the current settings and their values.

*   `list`

    Displays all the configuration settings supported by Skeletal.

So what configuration settings are you likely to customise?

### Custom Repositories

Skeletal will by default download the templates from a specific repository as 
mentioned in the Creating Projects section. If you want to host template packages 
in a different repository you can add it to Skeletal's search path via the `simpleRepositories`
setting as a comma seperated list in $HOME/.skeletal/config.groovy:

    simpleRepositories = [
      "https://your.domain.tld/repo-dir"
    ]

Or in $HOME/.skeletal/managed-config.json:

    {
        "simpleRepositories": [
            "https://your.domain.tld/repo-dir"
        ]
    }

To add a simple repository listing to the managed configuration file:

    skeletal config add simpleRepositories "https://your.domain.tld/repo-dir"

This will also create the file if it doesn't exist yet.

If a template exists in more than one repository, it will be downloaded from the
first repository in the list that it appears in.

### Repository Manifest

Where Lazybones used web services to list and create projects from templates stored 
on Bintray, Skeletal uses a simple `skeletal-manifest.txt` file located in the 
repository to provide the necessary information. This file is in the CSV format.

    name,version,owner,description
    aoo-addin-java-template,0.3.0,"Code Builders, LLC","Apache OpenOffice Add-In Template for Java"
    aoo-addin-template,0.3.0,"Code Builders, LLC","Apache OpenOffice Add-In Template for Groovy"
    aoo-addon-java-template,0.3.0,"Code Builders, LLC","Apache OpenOffice Add-On Template for Java"
    aoo-addon-template,0.3.0,"Code Builders, LLC","Apache OpenOffice Add-On Template for Groovy"
    aoo-client-template,0.3.0,"Code Builders, LLC","Apache OpenOffice Client Template for Groovy"

Note that the template name has a `-template` suffix. The zip file packages have a 
similar format of `<name>-template-<version>.zip`

When listing or creating projects from templates the `-template` is omitted. It is 
also removed from the zip file name when it is copied into the local cache directory 
when first used.

### Package Aliases

If you regularly use a template at a specific URL rather than from the default or 
configured repository, then you will want to alias that URL to a name. 
That's where template mappings (or aliases) come in. The aliases are defined as 
normal settings of the form

    templates.mappings.<alias> = <url>

In a Groovy configuration file, you can define multiple aliases in a block:

    templates {
        mappings {
            test = "http://dl.dropboxusercontent.com/u/29802534/custom-ratpack.zip"
            after = "file:///var/tmp/afterburnerfx-2.0.0.zip"
        }
    }

Alternatively, add them from the command line like this:

    skeletal config set templates.mappings.after file:///var/tmp/afterburnerfx-2.0.0.zip

The aliases will always be available to you until you remove them from the persisted
configuration.

### Setting a Proxy (and other system properties)

Many people have to work behind a proxy, one way to do it is to add the relevant system
properties to a `JAVA_OPTS` environment variable. There is also another option.

Skeletal has borrowed the idea of having a special form of configuration option for
system properties from Gradle. So if you define a property with a `systemProp.`
prefix, it will be added as a system property internally. So to configure an HTTP
proxy, you only need to add the following to your Skeletal configuration:

    systemProp {
        http {
            proxyHost = "localhost"
            proxyPort = 8181
        }
        https {
            proxyHost = "localhost"
            proxyPort = 8181
        }
    }
    
To avoid potential configuration issues, use the same proxy settings for HTTP and
HTTPS if possible.

If your proxy requires authentication, you will need to add a couple of extra
properties:

    systemProp {
        http {
            proxyUser = "johndoe"
            proxyPassword = "mypassword"
        }
    }

As with the host and port, there are `https` variants of the username and password
as well.

### General Options

These are miscellaneous options that can be overridden on the command line:

    // <-- This starts a line comment
    // Set logging level - overridden by command line args
    options.logLevel = "SEVERE"

The logging level can either be overridden using the same `logLevel` setting:

    skeletal --logLevel SEVERE info aoo-addin

or via `--verbose`, `--quiet`, and `--info` options:

    skeletal --verbose info aoo-addin

The logging level can be one of:

* OFF
* SEVERE
* WARNING
* INFO
* FINE
* FINEST
* ALL

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

The [Skeletal Gradle Plugin](https://github.com/cbmarcum/skeletal-gradle-plugin)
has been separated into its own project.

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
