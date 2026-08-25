import java.io.PrintStream;
import java.time.LocalDateTime;

/**
 * Phase 2: Information Architecture & User Journeys
 * Runnable document that prints the IA, sitemap, screen list, data model, and detailed user journeys.
 */
public class Phase2 {
    public static void main(String[] args) {
        printReport(System.out);
    }

    public static void printReport(PrintStream out) {
        out.println("Phase 2 — Information Architecture & User Journeys\nGenerated: " + LocalDateTime.now());
        out.println("================================================================\n");

        out.println("1. Sitemap / High-level Information Architecture\n");
        out.println("- Dashboard: KPIs, quick-actions, recent activity, system health.");
        out.println("- Inventory: Item list, filters, add/edit item panel, stock management.");
        out.println("- Users: List (by role) + detail pane, create/edit/delete users (Customer/Seller/Driver/Admin).");
        out.println("- Orders: Order list, order detail, assign driver, update status.");
        out.println("- Deliveries: Driver-focused view of assigned deliveries and status updates.");
        out.println("- Reports: Exports, sales, shipments, user activity.");
        out.println("- Settings: App prefs, DB path, import/export, backup.");
        out.println();

        out.println("2. Primary screens and components\n");
        out.println("- Top navigation: Tabbed (Dashboard | Inventory | Users | Orders | Reports | Settings)");
        out.println("- Left rail (optional): Quick filters and actions for the selected tab");
        out.println("- Central content: Panels per screen (InventoryPanel, UsersPanel, OrdersPanel, DashboardPanel)");
        out.println("- Global status area: Toasts/status log and Save/Sync controls.");
        out.println();

        out.println("3. Data model (essential entities)\n");
        out.println("- Person (abstract): govID, name, age, address, phoneNumber, emailAddress, userName, password, role");
        out.println("- Customer/Seller/Driver/Admin: specialized subclasses with additional fields (shopName, licenseNumber, vehicleNumber)");
        out.println("- Items: itemID, itemName, description, itemWeight, itemSize, itemPrice, itemDiscount, stockCount");
        out.println("- Orders: orderID, customerID, items[], status, assignedDriverID, timestamps, deliveryAddress, trackingCode");
        out.println();

        out.println("4. Navigation model and state\n");
        out.println("- Primary navigation uses tabs; each tab has a list/detail split view where appropriate.");
        out.println("- Panels are lightweight and swapped into the main content area (CardLayout or JTabbedPane).");
        out.println("- Global AppController routes events and manages persistence (DAO).");
        out.println();

        out.println("5. User journeys (detailed)\n");

        out.println("A) Admin — Create a Seller and verify persistence:\n");
        out.println("1. Admin logs in (credentials) -> Dashboard visible with quick actions.");
        out.println("2. Click Users tab -> Users list loads (by role)." );
        out.println("3. Click 'Add Seller' -> UserFormDialog opens (fields: govID*, name*, age*, address, phone, email, username*, password*, shopName*).");
        out.println("4. Admin fills required fields and clicks Save -> UI validates required fields and numeric fields.");
        out.println("5. AppController calls DAO.saveUser(seller) -> DAO persists to SQLite and writes legacy JSON.");
        out.println("6. On success: close dialog, refresh Users list, show toast 'Seller created' and mark record as saved in status area.");
        out.println("7. Acceptance: Seller appears in Sellers list; app restart preserves the Seller entry.");
        out.println();

        out.println("B) Seller — Add an Inventory Item:\n");
        out.println("1. Seller logs in or Admin impersonates Seller -> Inventory tab.");
        out.println("2. Click 'Add Item' -> compact inline form appears in InventoryPanel (ID, Name, Description, Weight, Size, Price, Discount, Stock).");
        out.println("3. Seller completes form and clicks Add -> client-side validation ensures numeric fields and required fields.");
        out.println("4. Controller updates ItemBox model, UI shows new item in the table (optimistic update)." );
        out.println("5. Optional: auto-save triggers DAO.saveData after a small debounce interval or Save button.");
        out.println("6. Acceptance: Item visible in table, persisted on Save, search and filters find the item.");
        out.println();

        out.println("C) Driver — Update delivery status:\n");
        out.println("1. Driver logs in -> Deliveries tab (DriverPanel) shows assigned orders sorted by due date.");
        out.println("2. Driver selects an order -> OrderDetail modal shows items, recipient, address, and actions (Mark picked up, In transit, Delivered).");
        out.println("3. Driver taps 'Delivered' -> confirmation modal requires optional notes and signature capture (optional)." );
        out.println("4. Controller updates order status and DAO.saveData persists the change; status message appears to driver.");
        out.println("5. Acceptance: Order status updates to 'Delivered' and visible in Reports and Customer tracking.");
        out.println();

        out.println("D) Customer — Create a shipment:\n");
        out.println("1. Customer selects Create Shipment from Dashboard or Customers panel.");
        out.println("2. Multi-step wizard: Sender info -> Recipient info -> Package details -> Review -> Confirm.");
        out.println("3. Each step validates and allows Back/Next; persistent draft saved between steps unless canceled.");
        out.println("4. On Confirm: create Order with tracking code and show success screen with printable receipt.");
        out.println("5. Acceptance: Order saved and visible to Customer under History; tracking updates when Driver marks delivered.");
        out.println();

        out.println("6. Error states and validations (common):\n");
        out.println("- Missing required fields: show inline validation and focus the first invalid field.");
        out.println("- DB save error: show non-blocking error toast and allow retry; don't discard user input.");
        out.println("- Conflicting updates: if two users edit same item, present 'last write wins' with 'Review changes' option in future iteration.");
        out.println();

        out.println("7. Key UI patterns and components to implement:\n");
        out.println("- List/Detail split view with search, filter, and bulk actions (multi-select + bulk delete/export).");
        out.println("- Modal dialogs for Add/Edit with clear cancel/save actions and keyboard shortcuts.");
        out.println("- Toast/status area for background operations and confirmations.");
        out.println("- Compact inline forms for frequent operations (Add Item) to minimize context switching.");
        out.println();

        out.println("8. Tracing a typical technical flow for Add Seller -> Persist:\n");
        out.println("UI(UserFormDialog).onSave() -> AppController.validateUser() -> DAO.saveData(itemBox, people) -> DB commit -> UI.refresh() -> Toast");
        out.println();

        out.println("9. Suggested acceptance tests for Phase 2 scope:\n");
        out.println("- Add/Edit/Delete Seller (UI + persisted)\n- Add Item to Inventory (UI + persisted)\n- Driver update delivery status (UI + persisted)");
        out.println();

        out.println("--- End of Phase 2 (IA & Journeys) ---\n");
    }
}