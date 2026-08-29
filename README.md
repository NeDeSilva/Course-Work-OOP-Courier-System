# Courier Management System

A desktop courier management application built with **Java 17+**, **Swing** for the
user interface and **SQLite** (via JDBC) for persistence. The whole project applies
core Object-Oriented Programming principles so it stays simple, extensible and
maintainable.

## Features

- **Role-based login** (Admin, Seller, Driver, Customer) with a modern dashboard
  that adapts to the logged-in role.
- **Inventory management** — add, update, delete and search items.
- **User management** — register customers, sellers and drivers.
- **Shipment lifecycle** — create shipments, assign drivers, and advance status
  (`Created → Picked up → In transit → Delivered`).
- **Persistence** — items, users and shipments are saved to SQLite every time you
  hit *Save*, and reloaded on startup. A default admin account is created on
  first run so the app is usable immediately.

## Default login

| Role     | Username | Password |
|----------|----------|----------|
| Admin    | `admin`  | `admin123` |

## OOP design

- **Abstraction & inheritance** — `Person` is the base class; `Admin`, `Customer`,
  `Seller` and `Driver` extend it, each adding role-specific fields.
- **Encapsulation** — domain objects expose behaviour (e.g. `ItemBox.findItem`,
  `Item.getItemPriceAfterDiscount`) instead of free-floating logic.
- **Polymorphism** — the login flow and detail dialogs operate on `Person` and
  dispatch by concrete type.
- **Single responsibility / DRY** — the GUI uses a shared `UITheme` and an abstract
  `PersonDetailUI` base, removing duplicated styling and layout code.

Layered architecture:

```
Model          Person, Admin, Customer, Seller, Driver, Items, ItemBox, Shipment, Session
Persistence    DAO  (SQLite + JSON fallback)
Controller     AppController  (mediates between views and the data layer)
View           CoreUI, LoginPanel, InventoryPanel, UsersPanel, ShipmentsPanel,
               CustomerPanel, DriverPanel, SellerPanel, AccountDialog, *-UI dialogs
Utility        UITheme
```

## Build & run (Windows)

Make sure a JDK (17+) is installed (`java` / `javac` available):

```
build.bat
run.bat
```

Or directly:

```
javac -encoding UTF-8 -cp "libs\sqlite-jdbc-3.46.1.0.jar;libs\slf4j-api-2.0.16.jar;libs\slf4j-simple-2.0.16.jar" -d out *.java
java -cp "out;libs\sqlite-jdbc-3.46.1.0.jar;libs\slf4j-api-2.0.16.jar;libs\slf4j-simple-2.0.16.jar" App
```

## Tests

`SmokeTest` verifies the data layer headlessly (no GUI needed):

```
javac -encoding UTF-8 -cp "libs\sqlite-jdbc-3.46.1.0.jar" -d out *.java
java -cp "out;libs\sqlite-jdbc-3.46.1.0.jar;libs\slf4j-api-2.0.16.jar;libs\slf4j-simple-2.0.16.jar" SmokeTest
```
