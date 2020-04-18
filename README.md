# TestAutomation
It can use to upload and run your Test Automation suite schedule them and save its result.


1. Copy the jar webserver1-0.0.1-SNAPSHOT.jar to any location
2. Copy the /Users/amrutashingavi/eclipse-workspace/properties/application.properties
3. Change the spring.datasource section in the application.properties file to point to the right mysql
4. Set the default system path properties. Purpose of each and every properties is in the comment above it.
5. Start the mysql server
6. import my dump file
6. Start the application server using below command
java -jar -Dspring.config.location=<app props path>/application.properties <app jar path>/webserver1-0.0.1-SNAPSHOT.jar
