# Low-Fidelity Wireframes — Courier Management System

Generated ASCII wireframes for key screens: Dashboard, Inventory, Users, Create Shipment.

---

DASHBOARD (Top-level)

+--------------------------------------------------------------------------+
| [Logo]      Courier Management System                [User] [Save] [#]    |
+--------------------------------------------------------------------------+
| Tabs: [Dashboard] [Inventory] [Users] [Orders] [Reports] [Settings]      |
+--------------------------------------------------------------------------+
| KPI ROW:  [Total Orders: 124]  [Pending: 12]  [Delivered: 98]  [Revenue]  |
+--------------------------------------------------------------------------+
| Quick Actions: [Create Shipment] [Add Item] [Add User] [Assign Driver]    |
+--------------------------------------------------------------------------+
| Recent Activity / Notifications                                            |
| - 2026-08-25 10:30: Seller A added item IT-200                              |
| - 2026-08-25 09:45: Driver D marked delivery for ORD-102                   |
+--------------------------------------------------------------------------+
| Footer / Status Area: Save status, last saved timestamp, small log area    |
+--------------------------------------------------------------------------+

---

INVENTORY

+--------------------------------------------------------------------------+
| Tabs: ... [Inventory] ...                                                  |
+--------------------------------------------------------------------------+
| Search: [___________] [Filter v] [Sort v]   [Add Item (inline)]           |
+--------------------------------------------------------------------------+
| | ID   | Name           | Price | Stock | Discount | Actions (edit/delete) |
| |------|----------------|-------|-------|----------|-----------------------|
| | IT-100 | Standard Pkg | 750   | 25    | 10%      | [Edit] [Delete]      |
| | IT-101 | Express Box  | 1300  | 12    | 15%      | [Edit] [Delete]      |
+--------------------------------------------------------------------------+
| Inline Add Item (collapsed by default)                                    |
| [ID] [Name] [Desc] [Weight] [Size] [Price] [Discount] [Stock] [Add Btn]   |
+--------------------------------------------------------------------------+
| Item Detail Modal (on Edit):                                              |
| Title: Edit Item IT-100                                                    |
| Fields: ID (disabled), Name, Desc, Weight, Size, Price, Discount, Stock    |
| Actions: [Cancel] [Save]                                                   |
+--------------------------------------------------------------------------+

---

USERS (List / Detail)

+--------------------------------------------------------------------------+
| Tabs: ... [Users] ...                                                      |
+--------------------------------------------------------------------------+
| Role filter: [All v] [Customers] [Sellers] [Drivers] [Admins]  [Add User]  |
+--------------------------------------------------------------------------+
| | Left: List (search + list) | Right: Detail pane / selected user details |
| |---------------------------|--------------------------------------------|
| | Search: [________]        | Name: John Doe                             |
| | - John Doe (Customer)     | GovID: C-1001                              |
| | - Shop A (Seller)        ->| Role: Seller                                |
| | - Driver D (Driver)      | Shop: Shop A                                |
| |                          | Contact: 077-xxxxxxx                        |
+--------------------------------------------------------------------------+
| Actions: [Edit] [Delete] [Impersonate] (if Admin)                         |
| Add/Edit User Modal: fields vary by role (Seller -> includes shopName)     |
+--------------------------------------------------------------------------+

---

CREATE SHIPMENT (Wizard)

Step Header: [1 Sender] [2 Recipient] [3 Package] [4 Review]

Step 1 — Sender
[Sender name] [Contact] [Address lines... ] [Next]

Step 2 — Recipient
[Recipient name] [Contact] [Address lines... ] [Back] [Next]

Step 3 — Package
[Item description] [Weight] [Size] [Declared value] [Next]

Step 4 — Review
[Summary block] [Confirm & Create Shipment] [Back]

Error states: inline validation with messages, focus on first invalid field; on DB save failure show non-blocking toast and allow retry.

---

Notes:
- Keyboard accessibility: focus order should follow left-to-right/top-to-bottom; Enter triggers primary action; Esc cancels modals.
- Modal patterns: primary action on the right (Save/Confirm), secondary on the left (Cancel/Back).

---
