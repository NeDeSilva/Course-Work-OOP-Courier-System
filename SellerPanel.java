import javax.swing.*;
import java.awt.*;

/** Seller workspace: browse the items they sell through the courier. */
public class SellerPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final AppController controller;
    private final DefaultListModel<Items> itemModel = new DefaultListModel<>();
    private final JList<Items> itemList = new JList<>(itemModel);

    public SellerPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BACKGROUND);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        top.setOpaque(false);
        JButton refreshBtn = UITheme.primaryButton("Refresh Items");
        JButton addItemBtn = UITheme.successButton("Add Item");
        top.add(refreshBtn);
        top.add(addItemBtn);

        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.setFont(UITheme.FONT_SUB);
        itemList.setCellRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value,
                                                                   int index, boolean isSelected, boolean f) {
                super.getListCellRendererComponent(list, value, index, isSelected, f);
                if (value instanceof Items it) {
                    setText(it.itemName + "  —  $" + String.format(java.util.Locale.US, "%.2f", it.itemPrice)
                            + "  (stock " + it.stockCount + ")");
                }
                return this;
            }
        });
        JScrollPane scroll = new JScrollPane(itemList);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> refreshItems());
        addItemBtn.addActionListener(e -> addItem());

        itemList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Items it = itemList.getSelectedValue();
                    if (it != null) {
                        JOptionPane.showMessageDialog(SellerPanel.this,
                                "Item: " + it.itemName + "\nPrice: $" + String.format(java.util.Locale.US, "%.2f", it.itemPrice)
                                        + "\nStock: " + it.stockCount + "\nDiscount: " + it.itemDiscount + "%",
                                "Item detail", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        refreshItems();
    }

    private void addItem() {
        String id = JOptionPane.showInputDialog(this, "Item ID:");
        if (id == null || id.trim().isEmpty()) return;
        if (controller.getItemBox().findItem(id.trim()) != null) {
            JOptionPane.showMessageDialog(this, "That item ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String name = JOptionPane.showInputDialog(this, "Item Name:");
        if (name == null || name.trim().isEmpty()) return;
        String priceStr = JOptionPane.showInputDialog(this, "Price:");
        try {
            float price = Float.parseFloat(priceStr);
            controller.addItem(new Items(id.trim(), name.trim(), "", 0, 0, price, 0, 1));
            refreshItems();
            JOptionPane.showMessageDialog(this, "Added item: " + name, "Added", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshItems() {
        itemModel.clear();
        for (Items it : controller.getItemBox().getAllItems()) itemModel.addElement(it);
    }
}
