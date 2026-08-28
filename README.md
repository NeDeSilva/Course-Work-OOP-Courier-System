## Courier management system

### Introduction

This project implements a courier management system using Object-Oriented Programming (OOP) principles in Java. It provides comprehensive features for managing deliveries, tracking packages, and handling courier operations.

### Features

- Create and manage delivery orders
- Track package status and location
- Manage courier assignments and schedules
- Calculate delivery costs and estimated times
- Generate delivery reports and statistics

### Technologies Used

- Java 17
- JUnit 5 for testing
- Maven for dependency management
- MySQL for data persistence

### ER Diagram

The following Entity-Relationship diagram represents the database schema:

![ER Diagram](er-diagram.png)

### UML class diagram

The following UML class diagram represents the object-oriented design of the system:

![UML Class Diagram](uml-class-diagram.png)

### UML object diagram

The following UML object diagram represents the runtime state of the system:

![UML Object Diagram](uml-object-diagram.png)

### Getting Started

To run this project locally:

1. Clone the repository
2. Ensure you have Java 17 and Maven installed
3. Configure your MySQL database connection in `application.properties`
4. Run `mvn clean install` to build the project
5. Start the application using `java -jar target/courier-system.jar`