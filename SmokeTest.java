import java.io.File;

/** Headless verification of the model + DAO layer (no GUI required). */
public class SmokeTest {

    public static void main(String[] args) throws Exception {
        int failures = run();

        // Remove the store created during the test so the app restarts fresh.
        new File("data.db").delete();
        new File("data.json").delete();

        System.out.println(failures == 0 ? "SMOKE TEST PASSED" : "SMOKE TEST FAILED (" + failures + " failures)");
        System.exit(failures == 0 ? 0 : 1);
    }

    private static int run() {
        int failures = 0;
        AppController c = new AppController();

        // 1. Seeded defaults are usable out of the box.
        Person admin = c.authenticate("admin", "admin123");
        if (admin == null || !(admin instanceof Admin)) {
            System.out.println("[FAIL] seeded admin login missing");
            failures++;
        }

        // 2. Item round-trip.
        c.addItem(new Items("IT-1", "Test Parcel", "desc", 2, 3, 99.5f, 10, 7));
        if (c.getItemBox().findItem("IT-1") == null) {
            System.out.println("[FAIL] item not added");
            failures++;
        }

        // 3. Authentication.
        c.addUser(new Admin("A-9", "Alice", 30, "", "1", "a@x", "alice", "pw"));
        c.addUser(new Driver("D-1", "Dan", 28, "", "2", "d@x", "dan", "pw", "LIC", "VEH"));
        c.addUser(new Seller("S-1", "Sue", 40, "", "3", "s@x", "sue", "pw", "Shop"));
        c.addUser(new Customer("C-1", "Cathy", 22, "", "4", "c@x", "cathy", "pw"));
        if (c.authenticate("alice", "pw") == null) { System.out.println("[FAIL] alice login"); failures++; }
        if (c.authenticate("alice", "bad") != null) { System.out.println("[FAIL] bad password accepted"); failures++; }

        // 4. Shipment lifecycle.
        c.addShipment(new Shipment("SH-1", "TRK001", "C-1", "S-1", "Fragile vase"));
        c.assignDriverToShipment("SH-1", "D-1");
        c.updateShipmentStatus("SH-1", "Delivered");
        if (c.getShipmentsForDriver("D-1").isEmpty()) { System.out.println("[FAIL] driver shipments"); failures++; }
        if (c.getShipmentsForCustomer("C-1").isEmpty()) { System.out.println("[FAIL] customer shipments"); failures++; }

        // 5. Persist + reload.
        if (!c.saveAll()) { System.out.println("[FAIL] saveAll"); failures++; }
        AppController reloaded = new AppController();
        boolean ok = !reloaded.getShipments().isEmpty()
                && "Delivered".equals(reloaded.getShipments().get(0).getStatus())
                && reloaded.authenticate("alice", "pw") != null;
        if (ok) System.out.println("[OK] persisted data reloaded");
        else { System.out.println("[FAIL] reload mismatch"); failures++; }

        return failures;
    }
}
