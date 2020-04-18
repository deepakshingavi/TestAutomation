# TestAutomation
It can use to upload and run your Test Automation suite schedule them and save its result.


This REST application has features like
On Demand execution of Test cases
Schedule execution of Test cases
Group of Test case and execution or scheduling them
Reports of the executed Test cases in HTML,PDF & Excel format


## Getting Started

Steps for starting the project


### Prerequisites

App. en. : JDK1.8+ or JRE 1.8+
Database : MySql Server 5.7+
Browser  : Mozilla firefox , Chrome
OS  	     : Windows, Linux
Set JAVA_HOME and java should be executable command

### Installing

Copy the jar file & the application.properties file

Refer to application.properties for updating the DB + Application related configurations
Refer to the prop description above it.

java -jar -Dspring.config.location=<Directory path>/application.properties <Directory path>/TestAutomation-0.0.1-SNAPSHOT.jar



Start the browser open below URL
http://<hostname>:<port>/<application name>/ui/login.html
hostname - machine's hostname
port - configrable in application's prop file
application name  - Configurable in application's app file


## Running the tests

NA

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
mvn clean install

## Front End Code

* You will get all front end code in \webserver1\src\main\resources\static\ui\ this folder


## Authors

* **Yogesh C.** - *Front end code *

* **Deepak S.** - *Back end code*

## License

NA

1. Copy the jar webserver1-0.0.1-SNAPSHOT.jar to any location
2. Copy the /Users/amrutashingavi/eclipse-workspace/properties/application.properties
3. Change the spring.datasource section in the application.properties file to point to the right mysql
4. Set the default system path properties. Purpose of each and every properties is in the comment above it.
5. Start the mysql server
6. import my dump file
6. Start the application server using below command
java -jar -Dspring.config.location=<app props path>/application.properties <app jar path>/webserver1-0.0.1-SNAPSHOT.jar
