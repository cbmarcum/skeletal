# Simple Java Spock Gradle Application Project

You have just created a simple Java application.
It provides a standard project structure for source code and tests. It uses Spock for writing test specifications and uses the Gradle build tool.

The project starts as simple Hello World application.

```
<proj>
|
|-- app
|   |-- src
|   |   |-- main
|   |   |   |-- java
|   |   |   |   `-- ${package_path}
|   |   |   |       `-- ${project_class_name}.java
|   |   |   `-- resources
|   |   `-- test
|   |       |-- groovy
|   |       |   `-- ${package_path}
|   |       |       `-- ${project_class_name}Spec.groovy
|   |       `-- resources
|   `-- build.gradle
|
|-- .gitattributes
|-- .gitignore
|-- gradlew
|-- gradlew.bat
`-- settings.gradle
```

## Using the project: 
1. Add any dependencies to build.gradle.
2. Add logic to ${project_class_name}.java.

## Run Tests
You can run tests with:
```
./gradlew check
```
Gradle HTML report is located in app/build/reports/tests.

Run the sample application with Gradle:
```
./gradlew :app:run
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

```
./bin/${project_name} 
```

## Additional Information

- [Skeletal Project](https://github.com/cbmarcum/skeletal)
- [Spock Framework](https://spockframework.org/)
- [Gradle Build Tool](https://gradle.org/)
