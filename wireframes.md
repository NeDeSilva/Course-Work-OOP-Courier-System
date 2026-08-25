# Low-Fidelity Wireframes — Courier Management System

Expanded low-fidelity ASCII wireframes for Phase 3 covering the requested screens and dialogs.
Includes accessibility/keyboard flow guidance and notes for developers.

---

DASHBOARD (Top-level)

+---------------------------------------------------------------------------------------------+
| [Logo]      Courier Management System                             [User: Admin ▼]  [Save]  |
+---------------------------------------------------------------------------------------------+
| Tabs: [Dashboard] [Inventory] [Users] [Orders] [Reports] [Settings]                         |
+---------------------------------------------------------------------------------------------+
| KPI ROW:  [Total Orders: 124]  [Pending: 12]  [In Transit: 14]  [Delivered: 98]  [Revenue]   |
+---------------------------------------------------------------------------------------------+
| Quick Actions:  [Create Shipment]  [Add Item]  [Add User]  [Assign Driver]  [Export CSV]      |
+---------------------------------------------------------------------------------------------+
| Activity / Alerts                                                                            |
| - 10:30 Seller A added item IT-200                                                             |
| - 09:45 Driver D marked delivery ORD-102                                                        |
| - ERROR: Failed to export report (Retry)                                                       |
+---------------------------------------------------------------------------------------------+
| Main: Dashboard widgets (left: charts / middle: activity / right: quick metrics & tasks)      |
+---------------------------------------------------------------------------------------------+
| Footer / Status Area: [Saved 2m ago]  [Undo last save]  [Log >>]                              |
+---------------------------------------------------------------------------------------------+

Keyboard & interactions (Dashboard):
- Alt+D opens Dashboard tab. Tab focuses first KPI card.
- Left/Right arrows move between KPI cards. Enter opens detail drill-down.
- Quick actions: access via Alt+Q then number (e.g. Alt+Q,1 = Create Shipment).

Accessibility notes:
- KPI cards use ≥ 4.5:1 contrast and larger font for numbers (≥ 18px). Provide aria-label equivalents (e.g., "Total orders: 124").
- Activity list is keyboard-focusable and supports screen reader output per item.

---

INVENTORY (Table, Filters, Add/Edit)

+---------------------------------------------------------------------------------------------+
| Tabs: [Inventory]  Search: [__________]  Filter: [Category v]  Sort: [Price v]  [Export]     |
+---------------------------------------------------------------------------------------------+
| Inline Add (toggle):  [ + Add Item ]  (press Enter to expand/collapse)                     |
+---------------------------------------------------------------------------------------------+
| | ID     | Name               | Price | Stock | Discount | Weight | Actions                 |
| |--------|--------------------|-------|-------|----------|--------|-------------------------|
| | IT-100 | Standard Parcel     |  750  |  25   |  10%     |  2kg   | [Edit] [Delete]         |
| | IT-101 | Express Box         | 1300  |  12   |  15%     |  5kg   | [Edit] [Delete]         |
+---------------------------------------------------------------------------------------------+
| Inline Add Form (visible when +Add clicked):                                                 |
| [ID*] [Name*] [Description] [Weight*] [Size*] [Price*] [Discount] [Stock*] [Add] [Cancel]    |
+---------------------------------------------------------------------------------------------+
| Item Edit Modal:                                                                              |
| Title: Edit Item — IT-100                                                                       |
| Fields (tab order): ID (readonly), Name*, Description, Weight*, Size*, Price*, Discount, Stock* |
| Actions: [Cancel (Esc)]  [Save (Enter)]                                                         |
+---------------------------------------------------------------------------------------------+

User flows and validation:
- Required fields marked with asterisk (*). On Add/Save, focus jumps to first invalid field and shows inline message.
- Deleting an item triggers a confirmation modal: "Delete item IT-100? [Cancel] [Delete]" with keyboard focus on Delete.
- Bulk actions: multi-select rows (Shift/Ctrl) then apply Delete/Export.

Accessibility & keyboard:
- Table rows are navigable with Up/Down, and Enter opens Edit modal.
- Filters and Search are accessible and announce results count to screen readers.
- Inline Add form fields are reachable via Tab; pressing Enter on Add submits.

---

USERS (Tabbed Users UI with List / Detail + Add/Edit flows)

Layout:
+----------------------------------------------------------+-------------------------+
| Role filter: [All v] [Customers] [Sellers] [Drivers] [Admins] [Add User]            |
+----------------------------------------------------------+-------------------------+
| Left: Users List (search + results)                      | Right: Detail Pane      |
| - Search [_____]                                         | Name: John Doe          |
| - John Doe (Customer)                                    | Role: Customer          |
| - Shop A (Seller)                                        | GovID: C-1001           |
| - Driver D (Driver)                                      | Email: john@example.com |
| - ...                                                   | Phone: 077-xxx-xxxx     |
+----------------------------------------------------------+-------------------------+
| Actions: [Edit] [Delete] [Impersonate] (admin-only)                              |
+-----------------------------------------------------------------------------------+

Add/Edit User Modal (role-specific fields):
Modal header: "Add Seller" / "Edit Customer"
Fields (common): GovID*, Name*, Age*, Address, Phone, Email, Username*, Password*
Seller extras: ShopName*; Driver extras: LicenseNumber*, VehicleNumber*
Actions: [Cancel (Esc)] [Save (Enter)]

Add user flow (keyboard-friendly):
- Press 'Add User' -> dialog opens, focus on GovID.
- Fill fields; Tab moves to next field. Shift+Tab moves backward.
- Press Alt+S to Save or Enter on Save button; on validation failure, focus jumps to first invalid input.

Detail Pane behaviour:
- Read-only view with Edit button; Edit launches same modal pre-filled.
- Inline quick-actions next to contact info (Call, Email) for convenience.

Accessibility:
- Lists announce item count and the currently focused item.
- Modal traps focus while open and returns focus to the invoking control when closed.

---

USER DETAIL MODAL (Customer / Seller / Driver forms — detailed)

Modal: Add / Edit User

Header: [X] Add Seller

Fields (Suggested tab order and labels):
1. GovID*           [__________]   (aria-label: "Government ID, required")
2. Name*            [__________]
3. Age*             [__]
4. Address          [____________________________]
5. Phone            [__________]
6. Email            [__________]
7. Username*        [__________]
8. Password*        [__________]
9. Role (select)    [Customer v] (if adding role-select in modal)
10. ShopName (Seller only) [__________]
11. License# (Driver only) [__________]
12. Vehicle# (Driver only) [__________]

Footer Actions: [Cancel]  [Save]

Validation rules:
- GovID and Username uniqueness checks (on blur or on Save); show inline suggestion or prevent Save.
- Age: integer between 16 and 120.
- Email: basic RFC-like validation.

Accessibility:
- Provide clear validation messages and aria-describedby linking for each invalid field.
- Ensure contrast and focus ring visibility.

---

CREATE SHIPMENT (Customer flow — multi-step wizard)

Overview:
Step header shows progress: [1 Sender] [2 Recipient] [3 Package] [4 Review]

Step 1 — Sender
- Fields: Sender name*, Contact*, From address*  [Next]
- Keyboard: Enter = Next, Esc = Cancel (confirm discard)

Step 2 — Recipient
- Fields: Recipient name*, Contact*, Delivery address*  [Back] [Next]

Step 3 — Package
- Fields: Item description*, Weight*, Size, Declared value*  [Back] [Next]
- Inline cost estimate displayed (if pricing rules available)

Step 4 — Review
- Summary: Sender, Recipient, Package details, Total charge
- Actions: [Back] [Confirm & Create Shipment]
- After confirm: show tracking code and printable receipt

Error handling and UX:
- Save draft between steps (auto-save every step completion) to prevent data loss.
- On validation failure: focus first invalid field, display inline message.
- On persistence error at confirm: show non-blocking toast with Retry and Keep Draft options.

Accessibility:
- Announce step changes to screen reader; each step has a visible heading.
- Provide a skip-to-review link for power users.

---

DELIVERY WORKFLOW (Driver-focused)

Driver Panel layout:
+--------------------------------------------------------------------------------+
| Filter: [Assigned to me v] [Status: All v] [Today v]  Search [tracking code]    |
+--------------------------------------------------------------------------------+
| List of assigned deliveries (compact list):                                     |
| - ORD-102 | Recipient: Alice | Addr snippet | Due: Today | Status: In Transit       |
| - ORD-113 | Recipient: Bob   | Addr snippet | Due: Tomorrow | Status: Assigned     |
+--------------------------------------------------------------------------------+
| Detail / Quick actions (when a delivery selected):                              |
| Title: ORD-102                                                                  |
| Recipient: Alice                                                                |
| Address: 123 Main St -> [Open map]                                              |
| Items: 2 x Parcel                                                                |
| Actions: [Picked up] [In transit] [Delivered] [Report issue]                    |
+--------------------------------------------------------------------------------+

Quick status update flow:
- Driver selects delivery, presses action (e.g., Delivered) -> Confirm modal "Mark ORD-102 as Delivered?" [Cancel] [Confirm]
- On Confirm: update model, controller.persistStatus(ORD-102, Delivered)
- UI shows toast: "Marked Delivered — notify customer"; status badge changes in list

Offline handling:
- If DB or IO fails, queue status update locally and show banner "Updates queued — will sync when available".

Accessibility & keyboard:
- Actions accessible via keyboard (Tab to action, Enter to trigger). Confirm modal traps focus.
- Use larger touch targets when running on touch-capable devices (≥ 44px recommended).

---

ACCESSIBILITY & KEYBOARD FLOW NOTES (global)

1. Focus management:
- All modal dialogs must trap focus and return it to the invoking control when closed.
- On validation error, focus must move to the first invalid field and announce the error.

2. Keyboard shortcuts and navigation:
- Top-level tabs: Alt + (first letter) — Alt+D: Dashboard, Alt+I: Inventory, Alt+U: Users, Alt+O: Orders, Alt+R: Reports, Alt+S: Settings
- Quick actions: Alt+Q, then number to activate (e.g., Alt+Q,1 = Create Shipment)
- Table navigation: Up/Down to move rows, Enter to open detail, Space to select row for bulk actions.

3. Visual and semantic accessibility:
- Minimum color contrast 4.5:1 for text and 3:1 for large text.
- All icons have accessible labels or tooltips. Use descriptive button text where possible.
- Provide high-contrast theme toggle in Settings.

4. Screen reader support:
- Provide descriptive labels and aria descriptions for complex components (tables, lists, modals).
- Announce background operations (e.g., "Saving...", "Save completed") via live region equivalents.

5. Responsive & scaling:
- Ensure layouts reflow when the window is resized; prioritize content over chrome.
- Support system font size scaling and test at 125% and 150% zoom levels.

---

Developer notes:
- Implement UI as modular panels (InventoryPanel, UsersPanel, DashboardPanel, DriverPanel) and orchestrate via AppController.
- Keep forms small and validate client-side before calling DAO.
- For keyboard-first users, provide explicit focusable controls and visible focus outlines.

---
