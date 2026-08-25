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
                float price = Float.parseFloat(priceF.getText().trim());
                int stock = Integer.parseInt(stockF.getText().trim());
                Items it = new Items(id, name, "", 0,0, price, 0, stock);
                controller.addItem(it);
                refreshTable();
                idF.setText(""); nameF.setText(""); priceF.setText(""); stockF.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numeric values for price and stock", "Input error", JOptionPane.ERROR_MESSAGE);
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