import javax.swing.*;
import java.awt.*;

/** Read-only detail dialog for a seller. */
public class SellerUI extends PersonDetailUI {
    private static final long serialVersionUID = 1L;

    public SellerUI(Seller s) {
        super(s, 430, 340);
        setTitle("Seller: " + s.getName());
    }

    @Override
    protected void addDetailRows(javax.swing.JPanel panel, Person person) {
        Seller s = (Seller) person;
        addRow(panel, "Gov ID", s.getGovID());
        addRow(panel, "Age", String.valueOf(s.getAge()));
        addRow(panel, "Address", s.getAddress());
        addRow(panel, "Phone", s.getPhoneNumber());
        addRow(panel, "Email", s.getEmailAddress());
        addRow(panel, "Shop", s.getShopName());
    }

    public static void show(Seller s) {
        SwingUtilities.invokeLater(() -> new SellerUI(s));
    }
}

