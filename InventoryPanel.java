import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** Inventory management: add, update, delete items backed by the controller. */
public class InventoryPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final AppController controller;
    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JTextField idF = new JTextField(8);
    private final JTextField nameF = new JTextField(14);
    private final JTextField priceF = new JTextField(8);
    private final JTextField stockF = new JTextField(6);
    private final JTextField discountF = new JTextField(6);
    private final JLabel status = new JLabel(" ");

    public InventoryPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BACKGROUND);

        String[] cols = {"ID", "Name", "Description", "Price", "Stock", "Discount"};
        tableModel = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;

            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UITheme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(form(), BorderLayout.NORTH);
        add(statusLine(), BorderLayout.SOUTH);

        refreshTable();
    }

    private JPanel form() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        panel.setOpaque(false);

        panel.add(new JLabel("ID:")); panel.add(idF);
        panel.add(new JLabel("Name:")); panel.add(nameF);
        panel.add(new JLabel("Price:")); panel.add(priceF);
        panel.add(new JLabel("Stock:")); panel.add(stockF);
        panel.add(new JLabel("Discount%:")); panel.add(discountF);

        JButton add = UITheme.primaryButton("Add");
        add.addActionListener(e -> addItem());
        JButton update = UITheme.successButton("Update");
        update.addActionListener(e -> updateItem());
        JButton delete = UITheme.dangerButton("Delete");
        delete.addActionListener(e -> deleteItem());
        panel.add(add);
        panel.add(update);
        panel.add(delete);

        table.getSelectionModel().addListSelectionListener(e -> fillFromSelection());
        return panel;
    }

    private JPanel statusLine() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        status.setForeground(UITheme.TEXT_MUTED);
        status.setBorder(new EmptyBorder(4, 6, 2, 6));
        p.add(status, BorderLayout.WEST);
        return p;
    }

    private void fillFromSelection() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        idF.setText(str(tableModel.getValueAt(row, 0)));
        nameF.setText(str(tableModel.getValueAt(row, 1)));
        priceF.setText(number(tableModel.getValueAt(row, 3)));
        stockF.setText(number(tableModel.getValueAt(row, 4)));
    }

    private void addItem() {
        try {
            String id = idF.getText().trim();
            String name = nameF.getText().trim();
            if (id.isEmpty() || name.isEmpty()) {
                setStatus("ID and Name are required.");
                return;
            }
            if (controller.getItemBox().findItem(id) != null) {
                setStatus("An item with that ID already exists.");
                return;
            }
            float price = parseFloat(priceF.getText(), 0);
            int stock = parseInt(stockF.getText(), 0);
            int discount = parseInt(discountF.getText(), 0);
            if (price < 0 || stock < 0) {
                setStatus("Price and stock cannot be negative.");
                return;
            }
            controller.addItem(new Items(id, name, "", 0, 0, price, discount, stock));
            refreshTable();
            clear();
            setStatus("Added item: " + name);
        } catch (NumberFormatException ex) {
            setStatus("Enter valid numeric values.");
        }
    }

    private void updateItem() {
        Items existing = controller.getItemBox().findItem(idF.getText().trim());
        if (existing == null) {
            setStatus("Select an item to update first.");
            return;
        }
        existing.itemName = nameF.getText().trim();
        existing.itemPrice = parseFloat(priceF.getText(), existing.itemPrice);
        existing.stockCount = parseInt(stockF.getText(), existing.stockCount);
        existing.itemDiscount = parseInt(discountF.getText(), existing.itemDiscount);
        refreshTable();
        setStatus("Updated item: " + existing.itemID);
    }

    private void deleteItem() {
        String id = idF.getText().trim();
        if (id.isEmpty()) {
            setStatus("Enter the item ID to delete.");
            return;
        }
        if (controller.getItemBox().removeItem(id)) {
            refreshTable();
            clear();
            setStatus("Deleted item: " + id);
        } else {
            setStatus("No item with that ID exists.");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Items it : controller.getItemBox().getAllItems()) {
            tableModel.addRow(new Object[]{
                    it.itemID, it.itemName, it.Description,
                    it.itemPrice, it.stockCount, it.itemDiscount + "%"});
        }
        status.setText("Total items: " + controller.getItemBox().getItemCount());
    }

    private void clear() {
        idF.setText(""); nameF.setText(""); priceF.setText("");
        stockF.setText(""); discountF.setText("");
    }

    private void setStatus(String message) {
        status.setForeground(UITheme.PRIMARY);
        status.setText(message);
    }

    private float parseFloat(String text, float fallback) {
        return text == null || text.trim().isEmpty() ? fallback : Float.parseFloat(text.trim());
    }

    private int parseInt(String text, int fallback) {
        return text == null || text.trim().isEmpty() ? fallback : Integer.parseInt(text.trim());
    }

    private String str(Object v) {
        return v == null ? "" : v.toString();
    }

    private String number(Object v) {
        return v == null ? "" : v.toString().replace("%", "");
    }
}
