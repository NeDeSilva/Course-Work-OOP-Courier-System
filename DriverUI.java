import javax.swing.*;
import java.awt.*;

/** Read-only detail dialog for a driver. */
public class DriverUI extends PersonDetailUI {
    private static final long serialVersionUID = 1L;

    public DriverUI(Driver d) {
        super(d, 430, 370);
        setTitle("Driver: " + d.getName());
    }

    @Override
    protected void addDetailRows(javax.swing.JPanel panel, Person person) {
        Driver d = (Driver) person;
        addRow(panel, "Gov ID", d.getGovID());
        addRow(panel, "Age", String.valueOf(d.getAge()));
        addRow(panel, "Address", d.getAddress());
        addRow(panel, "Phone", d.getPhoneNumber());
        addRow(panel, "Email", d.getEmailAddress());
        addRow(panel, "License", d.getLicenseNumber());
        addRow(panel, "Vehicle", d.getVehicleNumber());
    }

    public static void show(Driver d) {
        SwingUtilities.invokeLater(() -> new DriverUI(d));
    }
}

