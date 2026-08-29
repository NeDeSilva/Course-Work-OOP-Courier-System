import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mediates between the GUI and the data layer. Owns the runtime model
 * (items, users, shipments), loads it from persistent storage on startup
 * and exposes the business operations the views call.
 */
public class AppController {
    private final ItemBox itemBox;
    private final List<Person> people;
    private final List<Shipment> shipments;
    private final DAO dao;

    public AppController() {
        this.itemBox = new ItemBox();
        this.people = new ArrayList<>();
        this.shipments = new ArrayList<>();
        this.dao = new DAO("data.json");
        try {
            this.itemBox.items.addAll(dao.loadItemBox().getAllItems());
            this.people.addAll(dao.loadUsers());
            this.shipments.addAll(dao.loadShipments());
        } catch (IOException e) {
            System.err.println("Could not load persisted data: " + e.getMessage());
        }
        seedIfEmpty();
    }

    /**
     * On first run (empty store) populates a default administrator and a few
     * sample items so the application is usable immediately.
     */
    private void seedIfEmpty() {
        if (!people.isEmpty() && !itemBox.getAllItems().isEmpty()) {
            return;
        }
        boolean changed = false;
        if (people.stream().noneMatch(p -> p instanceof Admin)) {
            people.add(new Admin("A-1001", "System Admin", 35, "Main Office", "077-1234567",
                    "admin@courier.com", "admin", "admin123"));
            changed = true;
        }
        if (itemBox.getAllItems().isEmpty()) {
            itemBox.addItem(new Items("IT-100", "Standard Parcel", "Domestic courier parcel", 2, 12, 750f, 10, 25));
            itemBox.addItem(new Items("IT-101", "Express Box", "Priority shipping box", 5, 20, 1300f, 15, 12));
            itemBox.addItem(new Items("IT-102", "Fragile Item", "Glass or electronics packaging", 4, 15, 980f, 8, 18));
            changed = true;
        }
        if (changed) {
            saveAll();
        }
    }

    public ItemBox getItemBox() {
        return itemBox;
    }

    public List<Person> getPeople() {
        return people;
    }

    public List<Shipment> getShipments() {
        return shipments;
    }

    public void addItem(Items item) {
        itemBox.addItem(item);
    }

    public void addUser(Person p) {
        people.add(p);
    }

    public void addShipment(Shipment s) {
        shipments.add(s);
    }

    public boolean assignDriverToShipment(String shipmentId, String driverGovID) {
        for (Shipment s : shipments) {
            if (s.getId().equals(shipmentId)) {
                s.setDriverGovID(driverGovID);
                s.setStatus("Assigned");
                return true;
            }
        }
        return false;
    }

    /**
     * Validates credentials against the in-memory user list and returns the
     * matching {@link Person}, or {@code null} when nothing matched.
     */
    public Person authenticate(String username, String password) {
        for (Person p : people) {
            if (p.userName != null && p.userName.equals(username)
                    && p.password != null && p.password.equals(password)) {
                return p;
            }
        }
        return null;
    }

    public boolean updateShipmentStatus(String shipmentId, String status) {
        for (Shipment s : shipments) {
            if (s.getId().equals(shipmentId)) {
                s.setStatus(status);
                return true;
            }
        }
        return false;
    }

    public List<Shipment> getShipmentsForCustomer(String customerGovID) {
        List<Shipment> out = new ArrayList<>();
        for (Shipment s : shipments) if (customerGovID.equals(s.getCustomerGovID())) out.add(s);
        return out;
    }

    public List<Shipment> getShipmentsForDriver(String driverGovID) {
        List<Shipment> out = new ArrayList<>();
        for (Shipment s : shipments) if (driverGovID.equals(s.getDriverGovID())) out.add(s);
        return out;
    }

    public boolean saveAll() {
        try {
            dao.saveData(itemBox, people);
            dao.saveShipments(shipments);
            return true;
        } catch (IOException e) {
            System.err.println("Save failed: " + e.getMessage());
            return false;
        }
    }
}
