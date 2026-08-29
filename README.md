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

## 2. Problem Identification and Analysis

### 2.1 Problem Statement & Application Domain
In the modern e-commerce landscape, inefficient package handling and manual order tracking frequently cause delivery delays, incorrect status updates, and inaccurate cost calculations. 

To address these operational challenges, this project targets the **Logistics and Delivery Management Domain** by building a standalone Java desktop application: the **Courier Management System**. The application automates core workflow processes—from package tracking and courier assignment to delivery cost estimation and statistical reporting—providing seamless operational management for sellers and system administrators.

---

### 2.2 Real-World Objects & System Representations
The application models key physical and operational entities within the courier ecosystem as Object-Oriented Programming (OOP) classes:

* **Delivery Order / Package:** Represents physical packages being shipped through the system. Attributes include order ID, sender/recipient information, package weight, delivery status, and calculated delivery fees.
* **Courier / Delivery Agent:** Represents the personnel tasked with package transport. Attributes include courier ID, name, assigned schedule, capacity, and active deliveries.
* **Seller:** Represents the business entity or user creating delivery orders via the `SellerUI`. Attributes include seller ID, business details, order history, and active order statuses.
* **Delivery Schedule & Report:** Operational data models representing assigned routes, dispatch timelines, estimated delivery times, and summary performance metrics.

---

### 2.3 Requirements and System Functionalities

#### **Functional Requirements:**
1. **Order Management:** Enable sellers to create, update, and manage delivery orders with automatic order processing.
2. **Package & Status Tracking:** Provide real-time tracking of package locations and status transitions (e.g., *Pending*, *Dispatched*, *Delivered*).
3. **Courier Assignment & Scheduling:** Automatically or manually assign packages to couriers based on availability and delivery schedules.
4. **Cost & Delivery Time Calculation:** Compute precise delivery costs and estimated delivery times based on package metrics and destination distance.
5. **Reports & Analytics:** Generate summary statistics and delivery reports for administrative monitoring and operational review.

#### **Non-Functional Requirements:**
1. **Data Persistence:** Maintain transactional integrity and persistent storage across sessions using a MySQL relational database.
2. **User Interface Accessibility:** Provide a clean, intuitive Graphical User Interface (`SellerUI` and main UI) for administrative and seller tasks.
3. **Reliability & Testability:** Ensure code robustness through unit testing (JUnit 5) and modular object-oriented component architecture.

---

### 2.4 Domain Selection Justification & Relevance

* **Academic Relevance (OOP Principles):** The courier domain inherently involves interacting entities (Orders, Couriers, Sellers, Reports), offering a practical domain to demonstrate core Object-Oriented Programming concepts such as **Encapsulation**, **Inheritance**, **Polymorphism**, and **Abstraction**.
* **Practical Utility:** Managing deliveries requires deterministic calculation logic, real-time status updates, and relational persistence. Building this system in Java 17 backed by MySQL mirrors real-world enterprise software patterns.
* **Market Relevance:** Automated courier management directly resolves pain points in modern e-commerce logistics, reducing manual overhead and increasing order fulfillment transparency.
