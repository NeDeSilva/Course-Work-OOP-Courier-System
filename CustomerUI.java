import javax.swing.*;
import java.awt.*;

/** Read-only detail dialog for a customer. */
public class CustomerUI extends PersonDetailUI {
    private static final long serialVersionUID = 1L;

    public CustomerUI(Customer c) {
        super(c, 430, 330);
        setTitle("Customer: " + c.getName());
    }

    @Override
    protected void addDetailRows(javax.swing.JPanel panel, Person person) {
        Customer c = (Customer) person;
        addRow(panel, "Gov ID", c.getGovID());
        addRow(panel, "Age", String.valueOf(c.getAge()));
        addRow(panel, "Address", c.getAddress());
        addRow(panel, "Phone", c.getPhoneNumber());
        addRow(panel, "Email", c.getEmailAddress());
    }

    public static void show(Customer c) {
        SwingUtilities.invokeLater(() -> new CustomerUI(c));
    }
}

