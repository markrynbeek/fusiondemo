
This project demonstrates a small Spring Boot REST service relying on a Fusion server in the background.
The project was set up using Spring Initializr.

Technology stack:
  - RESTful web services
  - Spring (Boot)
  - Tomcat
  - Fusion server
  - Maven

Library summary (see also Maven pom.xml file for dependency details):
        spring-boot-starter-actuator
        spring-boot-starter-web
        spring-boot-devtools
        spring-boot-starter-test
        springfox-swagger2
        springfox-swagger-ui
        json

Building the service
--------------------
This is a Maven build set up.
 - command line: use "mvn clean install" to build and test the project.
 - IDE: open the project (pom file) and build the project there. Default is for Intellij. 
  
Running the service 
-------------------
  Open the project in your IDE and run class FusionDemoApplication.
  This is a Spring Boot application that will start a service on default port 8080.
  
  
API documentation (Swagger)
---------------------------
   After starting the service 
     See: http://localhost:8080/v2/api-docs  
     See: http://localhost:8080/swagger-ui.html#/product-package-controller


Invoking the service
--------------------
See Swagger for details and API