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

### 2.1 The Problem & Application Domain
In modern e-commerce, poor package management and manual tracking quickly lead to missed delivery deadlines, incorrect status updates, and miscalculated shipping fees. 

To fix these everyday headaches, this project focuses on the **Logistics and Courier Management** domain. We developed a standalone Java desktop application called the **Courier Management System**. It automates the messy parts of delivery—handling order creation, courier schedules, live tracking, cost estimates, and reports—so both sellers and system admins can keep things running smoothly.

---

### 2.2 Real-World Entities in the Code
To map the physical courier world into Java objects, we created classes for the main "players" in the system:

* **Delivery Order / Package:** The physical box being shipped. It holds details like tracking IDs, sender/recipient info, package weight, current status, and total shipping cost.
* **Courier / Driver:** The person delivering the package. Tracks their ID, name, assigned schedule, vehicle capacity, and active deliveries.
* **Seller:** The shop owner or user setting up shipments through the `SellerUI`. Tracks their ID, business info, past orders, and current shipments.
* **Delivery Schedule & Reports:** The organizational data that keeps track of routes, dispatch times, estimated arrival times, and daily performance metrics.

---

### 2.3 System Requirements & Features

#### **What the System Does (Functional Requirements):**
1. **Manage Orders:** Let sellers easily create, update, and manage delivery orders.
2. **Track Packages:** Provide real-time status updates (like *Pending*, *Dispatched*, or *Delivered*).
3. **Assign Couriers:** Match packages to available couriers based on their schedules and workload.
4. **Calculate Costs & ETAs:** Automatically figure out delivery fees and estimated arrival times based on package weight and distance.
5. **Generate Reports:** Show admins helpful statistics on deliveries, active couriers, and revenue.

#### **System Quality Standards (Non-Functional Requirements):**
1. **Reliable Storage:** Save all order and user data securely using a MySQL database so nothing gets lost when the app closes.
2. **Simple Interface:** Provide clean, easy-to-use screens (`SellerUI` and main admin dashboard) that don't require training to navigate.
3. **Solid Performance & Testing:** Ensure the code is reliable and bug-free using JUnit 5 tests and modular Java architecture.

---

### 2.4 Why Choose This Domain?

* **Great for Learning OOP:** A courier system is naturally built out of distinct, interacting parts (Packages, Couriers, Sellers, Reports). This makes it perfect for practicing core Object-Oriented Programming concepts like **Encapsulation**, **Inheritance**, **Polymorphism**, and **Abstraction**.
* **Real-World Practicality:** Calculating fees, updating package statuses, and storing data relational-style mirror how actual enterprise logistics software works.
* **Solves a Real Problem:** Automating delivery management cuts down on human error and makes package shipping much easier for growing businesses.
