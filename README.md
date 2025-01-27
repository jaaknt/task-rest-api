# Task Rest-API application

## Description

The project is a simple Java REST API application that provides a set of endpoints to manage tasks and users. 
The application is built with Spring Boot and uses postgresql database to store the data.

## Features

* REST API endpoints to manage tasks and users
* Unit tests for the controller and service layer
* Swagger documentation for the API - http://localhost:8080/swagger-ui.html
* Swagger OpanAPI endpoints can be used to test the API
* Liquibase scripts for database migration

## Installation

To start the Postgresql database, run the following command: 
```
docker-compose up
```
To start the application locally, run the following command:
```
./gradlew bootRun
```
When application starts first time, the database tables will be created and sample data 
can be added/tested using Swagger OpenAPI interface.