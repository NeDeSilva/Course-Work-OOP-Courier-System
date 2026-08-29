package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import model.Seller;

/**
 * SwinGUI to manage sellers: main menu + seller management screen.
 * Backend logic lives in {@link model.Seller}.
 */
public class SellerUI extends JFrame {

    private static final Color HEADER_BG = new Color(0x1B4965);
    private static final Color BUTTON_BG = new Color(0x3F7CAC);
    private static final Color BUTTON_HOVER = new Color(0x2E86AB);
    private static final Color DELETE_BG = new Color(0xC0392B);
    private static final Color BUTTON_TEXT = Color.WHITE;
    private static final Color PANEL_BG = new Color(0xF2F6FA);
    private static final Color SUCCESS = new Color(0x1E8449);
    private static final Color ERROR = new Color(0xC0392B);

    private final List<Seller> sellers = new ArrayList<>();
    private int sequence = 1;

    private final DefaultTableModel model;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final JTable table;
    private final JLabel status = new JLabel(" ");
    private final JLabel stats = new JLabel(" ");

    private final JPanel sellerPanel;

    public SellerUI() {
        super("Seller Management");
        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Shop", "Area", "Parcels", "Wallet", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        sorter = new TableRowSorter<>(model);
        table = new JTable(model);
        table.setRowSorter(sorter);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        sellerPanel = buildSellerScreen();

        setContentPane(sellerPanel);
        setSize(880, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /* ---------------- Seller management ---------------- */

    private JPanel buildSellerScreen() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PANEL_BG);
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(HEADER_BG);
        top.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JLabel title = new JLabel("Seller Management");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        top.add(title, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setBackground(HEADER_BG);
        JTextField search = new JTextField(16);
        search.setToolTipText("Search by name, shop or area");
        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter(search.getText());
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter(search.getText());
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter(search.getText());
            }
        });
        right.add(search);
        top.add(right, BorderLayout.EAST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        buttons.setBackground(HEADER_BG);
        buttons.add(actionButton("Register seller", BUTTON_BG, this::register));
        buttons.add(actionButton("Edit seller", BUTTON_BG, this::edit));
        buttons.add(actionButton("Verify", BUTTON_BG, this::verify));
        buttons.add(actionButton("Add parcel", BUTTON_BG, this::addParcel));
        buttons.add(actionButton("Credit COD", BUTTON_BG, this::creditCod));
        buttons.add(actionButton("Withdraw", BUTTON_BG, this::withdraw));
        buttons.add(actionButton("List parcels", BUTTON_BG, this::listParcels));
        buttons.add(actionButton("Remove parcel", BUTTON_BG, this::removeParcel));
        buttons.add(actionButton("Delete seller", DELETE_BG, this::delete));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.add(top, BorderLayout.NORTH);
        header.add(buttons, BorderLayout.SOUTH);

        root.add(header, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(PANEL_BG);
        stats.setForeground(HEADER_BG);
        stats.setBorder(BorderFactory.createEmptyBorder(8, 4, 0, 4));
        bottom.add(stats, BorderLayout.NORTH);
        status.setBorder(BorderFactory.createEmptyBorder(4, 4, 0, 4));
        bottom.add(status, BorderLayout.SOUTH);
        root.add(bottom, BorderLayout.PAGE_END);

        return root;
    }

    private JButton actionButton(String text, Color bg, Runnable action) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(BUTTON_TEXT);
        b.setFocusPainted(false);
        b.addActionListener(e -> action.run());
        b.addMouseListener(hover(b, bg));
        return b;
    }

    private java.awt.event.MouseAdapter hover(JButton b, Color base) {
        return new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(base.equals(DELETE_BG) ? new Color(0xE74C3C) : BUTTON_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(base);
            }
        };
    }

    private void applyFilter(String text) {
        String query = text == null ? "" : text.trim();
        if (query.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }
        String lower = query.toLowerCase();
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                for (int col : new int[]{1, 2, 3}) {
                    Object v = entry.getValue(col);
                    if (v != null && v.toString().toLowerCase().contains(lower)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private Seller selected() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            setStatus("Select a seller in the table first.", false);
            return null;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        String id = (String) model.getValueAt(modelRow, 0);
        return sellers.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Seller s : sellers) {
            model.addRow(new Object[]{
                    s.getId(), s.getName(), s.getShopName(), s.getPickupArea(),
                    s.getParcelCount(),
                    String.format(Locale.US, "%.2f", s.getWalletBalance()),
                    s.isVerified() ? "VERIFIED" : "PENDING"});
        }
        long verified = sellers.stream().filter(Seller::isVerified).count();
        long pending = sellers.size() - verified;
        stats.setText(String.format(Locale.US,
                "Total: %d   |   Verified: %d   |   Pending: %d", sellers.size(), verified, pending));
    }

    private String nextId() {
        return String.format(Locale.US, "SL-%03d", sequence);
    }

    /* ---------------- Actions ---------------- */

    private void register() {
        JTextField id = new JTextField(nextId(), 6);
        id.setEditable(false);
        id.setBackground(new Color(0xE3E9EF));
        JTextField name = new JTextField(14);
        JTextField email = new JTextField(14);
        JTextField phone = new JTextField(14);
        JTextField shop = new JTextField(14);
        JTextField type = new JTextField(14);
        JTextField area = new JTextField(14);
        JPanel form = new JPanel(new GridLayout(7, 2, 6, 6));
        form.add(new JLabel("ID:")); form.add(id);
        form.add(new JLabel("Name:")); form.add(name);
        form.add(new JLabel("Email:")); form.add(email);
        form.add(new JLabel("Phone:")); form.add(phone);
        form.add(new JLabel("Shop name:")); form.add(shop);
        form.add(new JLabel("Business type:")); form.add(type);
        form.add(new JLabel("Pickup area:")); form.add(area);

        int result = JOptionPane.showConfirmDialog(this, form, "Register seller",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        Seller seller = new Seller(id.getText().trim(), name.getText().trim(), email.getText().trim(),
                phone.getText().trim(), "", shop.getText().trim(), type.getText().trim(),
                area.getText().trim());
        if (!seller.isValid()) {
            setStatus("Invalid seller. Name, shop name and pickup area are required.", false);
            return;
        }
        sellers.add(seller);
        sequence++;
        refreshTable();
        setStatus("Registered seller " + seller.getId(), true);
    }

    private void edit() {
        Seller seller = selected();
        if (seller == null) {
            return;
        }
        JTextField shop = new JTextField(nz(seller.getShopName()), 14);
        JTextField type = new JTextField(nz(seller.getBusinessType()), 14);
        JTextField area = new JTextField(nz(seller.getPickupArea()), 14);
        JPanel form = new JPanel(new GridLayout(3, 2, 6, 6));
        form.add(new JLabel("Shop name:")); form.add(shop);
        form.add(new JLabel("Business type:")); form.add(type);
        form.add(new JLabel("Pickup area:")); form.add(area);

        int result = JOptionPane.showConfirmDialog(this, form, "Edit seller " + seller.getId(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        seller.setShopName(shop.getText().trim());
        seller.setBusinessType(type.getText().trim());
        seller.setPickupArea(area.getText().trim());
        refreshTable();
        setStatus("Seller updated: " + seller.getId(), true);
    }

    private void verify() {
        Seller seller = selected();
        if (seller == null) {
            return;
        }
        boolean now = !seller.isVerified();
        seller.setVerified(now);
        refreshTable();
        setStatus(seller.getName() + " is now " + (now ? "VERIFIED" : "PENDING") + ".", true);
    }

    private void addParcel() {
        Seller seller = selected();
        if (seller == null) {
            return;
        }
        String id = JOptionPane.showInputDialog(this, "Parcel id:");
        if (id == null) {
            return;
        }
        boolean ok = seller.addParcel(id);
        refreshTable();
        setStatus(ok ? "Parcel attached. Total: " + seller.getParcelCount()
                : "Parcel not added (blank or duplicate).", ok);
    }

    private void listParcels() {
        Seller seller = selected();
        if (seller == null) {
            return;
        }
        List<String> ids = seller.getParcelIds();
        String body = ids.isEmpty() ? "No parcels attached." : "<html>" + ids.stream()
                .collect(java.util.stream.Collectors.joining("<br>&bull; ", "&bull; ", "")) + "</html>";
        JLabel label = new JLabel(body, SwingConstants.CENTER);
        label.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JOptionPane.showMessageDialog(this, label,
                "Parcels of " + seller.getId() + " (" + ids.size() + ")", JOptionPane.INFORMATION_MESSAGE);
    }

    private void removeParcel() {
        Seller seller = selected();
        if (seller == null) {
            return;
        }
        String id = JOptionPane.showInputDialog(this, "Parcel id to remove:");
        if (id == null) {
            return;
        }
        boolean ok = seller.removeParcel(id.trim());
        refreshTable();
        setStatus(ok ? "Parcel removed. Total: " + seller.getParcelCount()
                : "Parcel not found or blank.", ok);
    }

    private void creditCod() {
        Seller seller = selected();
        if (seller == null) {
            return;
        }
        String raw = JOptionPane.showInputDialog(this, "COD amount:");
        if (raw == null) {
            return;
        }
        try {
            double amount = Double.parseDouble(raw.trim());
            boolean ok = seller.creditCod(amount);
            refreshTable();
            setStatus(ok ? "Credited. Wallet: " + String.format(Locale.US, "%.2f", seller.getWalletBalance())
                    : "Amount must be greater than zero.", ok);
        } catch (NumberFormatException e) {
            setStatus("Invalid amount.", false);
        }
    }

    private void withdraw() {
        Seller seller = selected();
        if (seller == null) {
            return;
        }
        String raw = JOptionPane.showInputDialog(this, "Withdraw amount:");
        if (raw == null) {
            return;
        }
        try {
            double amount = Double.parseDouble(raw.trim());
            boolean ok = seller.withdraw(amount);
            refreshTable();
            setStatus(ok ? "Withdrawn. Wallet: " + String.format(Locale.US, "%.2f", seller.getWalletBalance())
                    : String.format(Locale.US, "Cannot withdraw %.2f. Balance: %.2f",
                            amount, seller.getWalletBalance()), ok);
        } catch (NumberFormatException e) {
            setStatus("Invalid amount.", false);
        }
    }

    private void delete() {
        Seller seller = selected();
        if (seller == null) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete seller " + seller.getId() + " (" + seller.getName() + ")?",
                "Confirm delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        sellers.remove(seller);
        refreshTable();
        setStatus("Seller removed: " + seller.getId(), true);
    }

    private void setStatus(String message, boolean ok) {
        status.setForeground(ok ? SUCCESS : ERROR);
        status.setText(message);
    }

    private static String nz(String value) {
        return value == null ? "" : value;
    }

    public static void main(String[] args) {
        new SellerUI().setVisible(true);
    }
}
