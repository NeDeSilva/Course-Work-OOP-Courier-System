
package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import BusinessLogic.Customer;

public class CustomerUI extends CoreUI {
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtAddress;
    private JButton btnSubmit;

    public CustomerUI() {
        // initialize CoreUI parent window frame
        super("Customer Management System");
        initCustomerComponents();
    }

    private void initCustomerComponents() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        panel.add(new JLabel("Customer Name:"));
        txtName = new JTextField();
        panel.add(txtName);

        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);

        panel.add(new JLabel("Phone Number:"));
        txtPhone = new JTextField();
        panel.add(txtPhone);

        panel.add(new JLabel("Delivery Address:"));
        txtAddress = new JTextField();
        panel.add(txtAddress);

        btnSubmit = new JButton("Register / Update Customer");
        panel.add(btnSubmit);

        // action listener for button click
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = txtName.getText();
                String email = txtEmail.getText();
                String phone = txtPhone.getText();
                String address = txtAddress.getText();

                // create customer object
                Customer customer = new Customer(name, email, phone, "CUST-" + System.currentTimeMillis(), address);
                
                JOptionPane.showMessageDialog(null, "Customer Saved Successfully!\nID: " + customer.getCustomerId());
            }
        });

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}










































