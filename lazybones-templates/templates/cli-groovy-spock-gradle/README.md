# Command Line Groovy Spock Gradle Application Project

You have just created a skeletal Groovy application. 
It provides a standard project structure, Picocli for command line argument parsing, and Spock for 
writing test specifications and uses the Gradle build tool.

It already has some example code to demonstrate command line options and tests as well as rolling file logging configured using Logback. 

The template provides a sample command line math application that takes three arguments:
1. --add or --subtract
2. Operand 1 (Integer)
3. Operand 2 (Integer)

The application outputs to `System.out` and a log file `logs/${project_name}.log`.

```
<proj>
|
|-- app
|   |-- src
|   |   |-- main
|   |   |   |-- groovy
|   |   |   |   `-- ${package_path}
|   |   |   |       `-- ${project_class_name}.groovy
|   |   |   `-- resources
|   |   |       `-- logback.xml
|   |   `-- test
|   |       |-- groovy
|   |       |   `-- ${package_path}
|   |       |       `-- ${project_class_name}Spec.groovy
|   |       `-- resources
|   |           `-- logback-test.xml
|   `-- build.gradle
|
|-- .gitignore
|-- gradlew
|-- gradlew.bat
|-- README.md
`-- settings.gradle
```

## Using the project:
1. Add any dependencies to `build.gradle`.
2. Add logic to `${project_class_name}.groovy`.
3. Logging is configured in `logback.xml`

## Run Tests
You can run tests with:
```
./gradlew check
```

Gradle HTML report is located in app/build/reports/tests.

Spock Reports are in `app/build/spock-reports`

Run the sample application with Gradle: 
```
./gradlew :app:run --args="--add 2 3"
```

## Building the Application
### Packaged Distribution
To package the application for a distribution to be unpacked later:
```
./gradlew assembleDist
````
 
The distribution archives are found in `app/build/distributions`

### Unpacked Application
You can assemble an "installed" unpacked application with:
```
./gradlew installDist
```

The application is found `app/build/install`

## Running the Application
Run the application commands from the application root directory that contains `bin` and `lib` :

### Display Help
```
./bin/${project_name} --help

Usage: ${project_name} [-hvV] (-a | -s) <operand1> <operand2>
      <operand1>
      <operand2>
  -a, --add        Add values.
  -h, --help       Show this help message and exit.
  -s, --subtract   Subtract values.
  -v, --verbose    Verbose mode. Helpful for troubleshooting.
  -V, --version    Print version information and exit.
```

### Display Version
```
./bin/${project_name} --version

${project_capitalized_name} ${project_version}
```

### Subtract Sub-Command
```
./bin/${project_name} --subtract 5 3

5 - 3 = 2
```    

## Additional Information

- [Skeletal Project](https://github.com/cbmarcum/skeletal)
- [Apache Groovy](http://www.groovy-lang.org/)
- [Picocli CLI](https://picocli.info/)
- [Logback Logging](https://logback.qos.ch/)
- [Spock Framework](https://spockframework.org/)
- [Spock Reports](https://github.com/renatoathaydes/spock-reports)
- [Gradle Build Tool](https://gradle.org/)
