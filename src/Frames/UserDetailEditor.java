package Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserDetailEditor extends JFrame {
    private JTextField nameField, empNumberField, emailField, ageField;
    private JButton saveButton, editButton, viewButton;

    public UserDetailEditor() {
        setTitle("User Details Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300); // Increased size to 500x300
        setLocationRelativeTo(null); // Center the frame on the screen

        // Create text fields
        nameField = new JTextField(20);
        empNumberField = new JTextField(20);
        emailField = new JTextField(20);
        ageField = new JTextField(20);
        ageField.setEditable(false); // Age is not editable

        // Create buttons with icons
        saveButton = new JButton("Save", new ImageIcon("save_icon.png")); // Change path to your icon file
        editButton = new JButton("Edit", new ImageIcon("edit_icon.png")); // Change path to your icon file
        viewButton = new JButton("View Details", new ImageIcon("view_icon.png")); // Change path to your icon file

        // Set button colors and fonts
        saveButton.setBackground(Color.decode("#4CAF50"));
        saveButton.setForeground(Color.black);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));

        editButton.setBackground(Color.decode("#FFA500"));
        editButton.setForeground(Color.black);
        editButton.setFont(new Font("Arial", Font.BOLD, 14));

        viewButton.setBackground(Color.decode("#008CBA"));
        viewButton.setForeground(Color.black);
        viewButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Add action listeners
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveUserDetails();
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editUserDetails();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewUserDetails();
            }
        });

        // Create panel to hold components
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Employee Number:"), gbc);
        gbc.gridx = 1;
        panel.add(empNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        panel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(editButton, gbc);

        gbc.gridy = 5;
        panel.add(saveButton, gbc);

        gbc.gridy = 6;
        panel.add(viewButton, gbc);

        // Add panel to frame
        add(panel);
    }

    private void saveUserDetails() {
        // You can add validation here before saving the details
        // For simplicity, let's just print the details for now
        System.out.println("Name: " + nameField.getText());
        System.out.println("Employee Number: " + empNumberField.getText());
        System.out.println("Email: " + emailField.getText());
        System.out.println("Age: " + ageField.getText());
        System.out.println("Details saved.");
    }

    private void editUserDetails() {
        // Enable the age field for editing
        ageField.setEditable(true);
    }

    private void viewUserDetails() {
        // Open the UserDetailDisplay frame for viewing
        new UserDetailDisplay(nameField.getText(), empNumberField.getText(), emailField.getText(), ageField.getText()).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new UserDetailEditor().setVisible(true);
            }
        });
    }
}
