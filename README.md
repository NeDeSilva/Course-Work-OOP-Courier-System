# Courier Management System

This Java project models a simple courier management system built around OOP classes for people, items, inventory, and sessions.

## Features
- Manage courier items and stock
- Track shipment inventory in `ItemBox`
- Persist data using SQLite through `DAO`
- Keep a JSON backup for compatibility
- Admin login and simple Swing dashboard UI
- Support for `Person`, `Admin`, `Customer`, `Items`, `Session`, and `CoreUI`

## Run on Windows
```powershell
javac -cp ".;sqlite-jdbc-3.46.1.0.jar" *.java
java -cp ".;sqlite-jdbc-3.46.1.0.jar" App
```

The app stores data in `data.db` and writes a legacy `data.json` backup.