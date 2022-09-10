Simple Java Spock Gradle Application Project
--------------------------------------------

You have just created a simple Java application. 
There is a standard project structure for source code and tests.
Simply add your source files to `app/src/main/java`, your test cases 
to `app/src/test/groovy` and see below for running your application.

In this project you get:

* A Gradle build file
* A standard project structure:
```
   <proj>
      |
      |-- app
      |   |-- src
      |   |   |-- main
      |   |   |   |-- java
      |   |   |   |   `-- ${package_path}
      |   |   |   |       `-- ${project_class_name}.java
      |   |   |   |
      |   |   |   `-- resources
      |   |   |
      |   |   `-- test
      |   |       |-- groovy
      |   |       |   `-- ${package_path}
      |   |       |       `-- ${project_class_name}Spec.groovy
      |   |       |
      |   |       `-- resources
      |   |
      |   `-- build.gradle
      |
      |-- gradle
      |   `-- wrapper
      |       |-- gradle-wrapper.jar
      |       `-- gradle-wrapper.properties
      |
      |-- .gitattributes
      |-- .gitignore
      |-- gradlew
      |-- gradlew.bat
      `-- settings.gradle
```

## Using the project: 
1. Add any dependencies to build.gradle.
2. Add logic to ${project_class_name}.groovy.

## Building the Extension
- Build archives for distribution:
```
./gradlew assemble
```
- Build an install directory with a runnable project unpacked:
```
./gradlew installDist
```
