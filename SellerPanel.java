import javax.swing.*;
import java.awt.*;

public class SellerPanel extends JPanel {
    private final AppController controller;
    private final DefaultListModel<Items> itemModel = new DefaultListModel<>();
    private final JList<Items> itemList = new JList<>(itemModel);

    public SellerPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(8,8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("Refresh Items");
        JButton addItemBtn = new JButton("Add Item");
        top.add(refreshBtn); top.add(addItemBtn);

        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(itemList);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> refreshItems());
        addItemBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this, "Item ID:");
            if (id == null || id.trim().isEmpty()) return;
            String name = JOptionPane.showInputDialog(this, "Item Name:");
            if (name == null || name.trim().isEmpty()) return;
            String priceStr = JOptionPane.showInputDialog(this, "Price:");
            try {
                float price = Float.parseFloat(priceStr);
                Items it = new Items(id.trim(), name.trim(), "", 0,0, price, 0, 1);
                controller.addItem(it);
                refreshItems();
                JOptionPane.showMessageDialog(this, "Added item: " + name, "Added", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        itemList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Items it = itemList.getSelectedValue();
                    if (it != null) JOptionPane.showMessageDialog(SellerPanel.this, "Item: " + it.itemName + "\nPrice: " + it.itemPrice + "\nStock: " + it.stockCount, "Item detail", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        refreshItems();
    }

    private void refreshItems() {
        itemModel.clear();
        for (Items it : controller.getItemBox().getAllItems()) itemModel.addElement(it);
    }
}
