Command Line Groovy Spock Gradle Project Template
--------------------------------------------------

You have just created a skeletal Groovy application. It provides a standard 
project structure, Picocli for command line argument parsing, and Spock for 
writing test specifications.

It already has some example code to demonstrate command line options and tests.

```
<proj>
|
|-- app
|   |-- src
|   |   |-- main
|   |   |   |-- groovy
|   |   |   |   `-- org
|   |   |   |       `-- example
|   |   |   |           `-- App.groovy
|   |   |   `-- resources
|   |   `-- test
|   |       |-- groovy
|   |       |   `-- org
|   |       |       `-- example
|   |       |           `-- AppSpec.groovy
|   |       `-- resources
|   `-- build.gradle
|
|-- .gitignore
|-- gradlew
|-- gradlew.bat
|-- README.md
`-- settings.gradle
```

That's it! You can build / test the application with gradle

    ./gradlew <test | groovyCompile | etc>

Run tests with:

    ./gradlew check

Run application with Gradle: 

    ./gradlew :app:run --args="--add 2 3"

output:
    operand1 = 2
    operand2 = 3
    2 + 3 = 5

Assemble application for distribution: 

    ./gradlew assemble

Unpack the distribution archive found in app/build/distributions

Run the application from the new folder:

    ./bin/app --subtract 3 1

output:

    operand1 = 3
    operand2 = 1
    3 - 1 = 2


