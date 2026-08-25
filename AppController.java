import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Simple controller for prototype: holds model and DAO and provides operations
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
            ItemBox loaded = dao.loadItemBox();
            this.itemBox.items.addAll(loaded.getAllItems());
            this.people.addAll(dao.loadUsers());
            // persist inferred roles back into the legacy JSON so subsequent runs don't need inference
            try {
                dao.persistUsersJson(this.itemBox, this.people);
            } catch (IOException ex) {
                // non-fatal; ignore
            }
            // shipments not yet persisted in DAO; keep empty on load
        } catch (IOException e) {
            // ignore for prototype; UI will show status
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
            // shipments persistence not yet implemented
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}