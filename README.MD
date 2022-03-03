# Simple Rest Service

## Prerequisites
Java 11

For example, Oracle JDK:
https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html

#### For simplicity reasons the embedded H2 database was used, so database configurations are not required

## Installation
### Clone project into folder using command in terminal
<code>git clone https://github.com/Jevgenijs92/customer-debt-service.git </code>

### To start the server, navigate to project folder and run command
<code>./mvnw spring-boot:run </code>

### You can now access local server, by default at:
http://localhost:8080/api-docs

### All endpoints (except API docs) are secured with basic authentication
<code>username: user
password: 1234 </code>






