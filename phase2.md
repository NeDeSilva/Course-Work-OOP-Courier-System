# Phase 2 — Information Architecture & User Journeys

Generated: 2026-08-25

## 1. Sitemap / High-level Information Architecture

- Dashboard: KPIs, quick-actions, recent activity, system health.
- Inventory: Item list, filters, add/edit item panel, stock management.
- Users: List (by role) + detail pane, create/edit/delete users (Customer/Seller/Driver/Admin).
- Orders: Order list, order detail, assign driver, update status.
- Deliveries: Driver-focused view of assigned deliveries and status updates.
- Reports: Exports, sales, shipments, user activity.
- Settings: App prefs, DB path, import/export, backup.

## 2. Primary screens and components

- Top navigation: Tabbed (Dashboard | Inventory | Users | Orders | Reports | Settings)
- Left rail (optional): Quick filters and actions for the selected tab
- Central content: Panels per screen (InventoryPanel, UsersPanel, OrdersPanel, DashboardPanel)
- Global status area: Toasts/status log and Save/Sync controls.

## 3. Data model (essential entities)

- Person (abstract): govID, name, age, address, phoneNumber, emailAddress, userName, password, role
- Customer/Seller/Driver/Admin: specialized subclasses with additional fields (shopName, licenseNumber, vehicleNumber)
- Items: itemID, itemName, description, itemWeight, itemSize, itemPrice, itemDiscount, stockCount
- Orders: orderID, customerID, items[], status, assignedDriverID, timestamps, deliveryAddress, trackingCode

## 4. Navigation model and state

- Primary navigation uses tabs; each tab has a list/detail split view where appropriate.
- Panels are lightweight and swapped into the main content area (CardLayout or JTabbedPane).
- Global AppController routes events and manages persistence (DAO).

## 5. User journeys (detailed)

### A) Admin — Create a Seller and verify persistence

1. Admin logs in (credentials) -> Dashboard visible with quick actions.
2. Click Users tab -> Users list loads (by role).
3. Click 'Add Seller' -> UserFormDialog opens (fields: govID*, name*, age*, address, phone, email, username*, password*, shopName*).
4. Admin fills required fields and clicks Save -> UI validates required fields and numeric fields.
5. AppController calls DAO.saveUser(seller) -> DAO persists to SQLite and writes legacy JSON.
6. On success: close dialog, refresh Users list, show toast 'Seller created' and mark record as saved in status area.
7. Acceptance: Seller appears in Sellers list; app restart preserves the Seller entry.

### B) Seller — Add an Inventory Item

1. Seller logs in or Admin impersonates Seller -> Inventory tab.
2. Click 'Add Item' -> compact inline form appears in InventoryPanel (ID, Name, Description, Weight, Size, Price, Discount, Stock).
3. Seller completes form and clicks Add -> client-side validation ensures numeric fields and required fields.
4. Controller updates ItemBox model, UI shows new item in the table (optimistic update).
5. Optional: auto-save triggers DAO.saveData after a small debounce interval or Save button.
6. Acceptance: Item visible in table, persisted on Save, search and filters find the item.

### C) Driver — Update delivery status

1. Driver logs in -> Deliveries tab (DriverPanel) shows assigned orders sorted by due date.
2. Driver selects an order -> OrderDetail modal shows items, recipient, address, and actions (Mark picked up, In transit, Delivered).
3. Driver taps 'Delivered' -> confirmation modal requires optional notes and signature capture (optional).
4. Controller updates order status and DAO.saveData persists the change; status message appears to driver.
5. Acceptance: Order status updates to 'Delivered' and visible in Reports and Customer tracking.

### D) Customer — Create a shipment

1. Customer selects Create Shipment from Dashboard or Customers panel.
2. Multi-step wizard: Sender info -> Recipient info -> Package details -> Review -> Confirm.
3. Each step validates and allows Back/Next; persistent draft saved between steps unless canceled.
4. On Confirm: create Order with tracking code and show success screen with printable receipt.
5. Acceptance: Order saved and visible to Customer under History; tracking updates when Driver marks delivered.

## 6. Error states and validations (common)

- Missing required fields: show inline validation and focus the first invalid field.
- DB save error: show non-blocking error toast and allow retry; don't discard user input.
- Conflicting updates: if two users edit same item, present 'last write wins' with 'Review changes' option in future iteration.

## 7. Key UI patterns and components to implement

- List/Detail split view with search, filter, and bulk actions (multi-select + bulk delete/export).
- Modal dialogs for Add/Edit with clear cancel/save actions and keyboard shortcuts.
- Toast/status area for background operations and confirmations.
- Compact inline forms for frequent operations (Add Item) to minimize context switching.

## 8. Technical flow example (Add Seller -> Persist)

UI(UserFormDialog).onSave() -> AppController.validateUser() -> DAO.saveData(itemBox, people) -> DB commit -> UI.refresh() -> Toast

## 9. Suggested acceptance tests for Phase 2 scope

- Add/Edit/Delete Seller (UI + persisted)
- Add Item to Inventory (UI + persisted)
- Driver update delivery status (UI + persisted)

---

If you'd like, I can now produce low-fidelity wireframes and flow diagrams for the prioritized screens (Dashboard, Inventory, Users, Create Shipment).