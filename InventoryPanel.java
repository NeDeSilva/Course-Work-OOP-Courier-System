import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryPanel extends JPanel {
    private final AppController controller;
    private final DefaultTableModel tableModel;

    public InventoryPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(8,8));

        String[] cols = {"ID","Name","Price","Stock","Discount"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);

        // Inline add form
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField idF = new JTextField(8);
        JTextField nameF = new JTextField(14);
        JTextField priceF = new JTextField(8);
        JTextField stockF = new JTextField(6);
        JButton addBtn = new JButton("Add");
        addPanel.add(new JLabel("ID:")); addPanel.add(idF);
        addPanel.add(new JLabel("Name:")); addPanel.add(nameF);
        addPanel.add(new JLabel("Price:")); addPanel.add(priceF);
        addPanel.add(new JLabel("Stock:")); addPanel.add(stockF);
        addPanel.add(addBtn);

        add(scroll, BorderLayout.CENTER);
        add(addPanel, BorderLayout.NORTH);

        refreshTable();

        addBtn.addActionListener(e -> {
            try {
                String id = idF.getText().trim();
                String name = nameF.getText().trim();
                if (id.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "ID and Name are required.", "Input error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                float price = Float.parseFloat(priceF.getText().trim());
                int stock = Integer.parseInt(stockF.getText().trim());
                if (price < 0 || stock < 0) {
                    JOptionPane.showMessageDialog(this, "Price and stock cannot be negative.", "Input error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String summary = String.format("Add item?\nID: %s\nName: %s\nPrice: %.2f\nStock: %d", id, name, price, stock);
                int confirm = JOptionPane.showConfirmDialog(this, summary, "Confirm add", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;

                Items it = new Items(id, name, "", 0,0, price, 0, stock);
                controller.addItem(it);
                refreshTable();
                idF.setText(""); nameF.setText(""); priceF.setText(""); stockF.setText("");
                JOptionPane.showMessageDialog(this, "Item added: " + name, "Added", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numeric values for price and stock", "Input error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Bind Enter to add action on form fields for faster entry
        idF.addActionListener(a -> addBtn.doClick());
        nameF.addActionListener(a -> addBtn.doClick());
        priceF.addActionListener(a -> addBtn.doClick());
        stockF.addActionListener(a -> addBtn.doClick());

        // Global save shortcut (Ctrl+S) when this panel or its window is focused
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke("control S"), "save");
        this.getActionMap().put("save", new javax.swing.AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                // try to find a top-level frame and simulate a save action if available
                Window w = SwingUtilities.getWindowAncestor(InventoryPanel.this);
                if (w instanceof javax.swing.JFrame) {
                    // prefer controller save if available
                    boolean ok = controller.saveAll();
                    if (ok) javax.swing.JOptionPane.showMessageDialog(InventoryPanel.this, "Saved", "Save", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    else javax.swing.JOptionPane.showMessageDialog(InventoryPanel.this, "Save failed", "Save", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Items> items = controller.getItemBox().getAllItems();
        for (Items it : items) {
            tableModel.addRow(new Object[]{it.itemID, it.itemName, it.itemPrice, it.stockCount, it.itemDiscount + "%"});
        }
    }
}