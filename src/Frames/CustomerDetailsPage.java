package Frames;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class CustomerDetailsPage extends JFrame {
    private JTextField idField, nameField, designIdField;
    private JButton addButton, viewButton;
    private JTextArea displayArea;

    public CustomerDetailsPage() {
        setTitle("Customer Details");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        // Create labels and text fields
        JLabel idLabel = new JLabel("Customer ID:");
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        idField = new JTextField(10);
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField = new JTextField(20);
        JLabel designIdLabel = new JLabel("Design ID:");
        designIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        designIdField = new JTextField(10);

        // Create buttons
        addButton = new JButton("Add Customer");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewButton = new JButton("View Customers");
        viewButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Create display area for showing customer details
        displayArea = new JTextArea(15, 30);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Add action listeners to buttons
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewCustomers();
            }
        });

        // Create panel to hold components
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 10));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.add(idLabel);
        inputPanel.add(idField);
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(designIdLabel);
        inputPanel.add(designIdField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);

        JPanel displayPanel = new JPanel();
        displayPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        displayPanel.add(scrollPane);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(displayPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    private void addCustomer() {
        String id = idField.getText();
        String name = nameField.getText();
        String designId = designIdField.getText();

        // Validate input
        if (id.isEmpty() || name.isEmpty() || designId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Append customer details to text file
        try (FileWriter writer = new FileWriter("customers.txt", true)) {
            writer.write(id + "," + name + "," + designId + "\n");
            writer.close();
            JOptionPane.showMessageDialog(this, "Customer added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding customer", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Clear input fields
        idField.setText("");
        nameField.setText("");
        designIdField.setText("");
    }

    private void viewCustomers() {
        displayArea.setText(""); // Clear the display area

        // Read customer details from the text file and display them
        try (BufferedReader reader = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] customerDetails = line.split(",");
                displayArea.append("Customer ID: " + customerDetails[0] + ", Name: " + customerDetails[1] + ", Design ID: " + customerDetails[2] + "\n");
                JButton editButton = new JButton("Edit");
                editButton.addActionListener(new EditActionListener(customerDetails[0], customerDetails[1], customerDetails[2]));
                displayArea.add(editButton);
                displayArea.append("\n");
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading customers", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class EditActionListener implements ActionListener {
        private String id;
        private String name;
        private String designId;

        public EditActionListener(String id, String name, String designId) {
            this.id = id;
            this.name = name;
            this.designId = designId;
        }

        public void actionPerformed(ActionEvent e) {
            idField.setText(id);
            nameField.setText(name);
            designIdField.setText(designId);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CustomerDetailsPage().setVisible(true);
            }
        });
    }
}
