package Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserDetailDisplay extends JFrame {
    private JLabel nameLabel, empNumberLabel, emailLabel, ageLabel;
    private JButton editButton;

    public UserDetailDisplay(String name, String empNumber, String email, String age) {
        setTitle("User Details");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 350); // Increased size to 600x350
        setLocationRelativeTo(null); // Center the frame on the screen

        // Create labels to display user details
        nameLabel = new JLabel("Name: " + name);
        empNumberLabel = new JLabel("Employee Number: " + empNumber);
        emailLabel = new JLabel("Email: " + email);
        ageLabel = new JLabel("Age: " + age);

        // Set fonts and colors for labels
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        nameLabel.setFont(labelFont);
        empNumberLabel.setFont(labelFont);
        emailLabel.setFont(labelFont);
        ageLabel.setFont(labelFont);

        // Create edit button
        editButton = new JButton("Edit");
        editButton.setBackground(Color.decode("#007bff")); // Blue color
        editButton.setForeground(Color.black);
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editUserDetails();
            }
        });

        // Set icon for edit button
        ImageIcon editIcon = new ImageIcon("edit_icon.png"); // Change path to your icon file
        editButton.setIcon(editIcon);
        editButton.setHorizontalTextPosition(SwingConstants.LEFT); // Place text to the right of the icon

        // Create panel to hold components
        JPanel panel = new JPanel(new GridLayout(5, 1));
        panel.setBackground(Color.LIGHT_GRAY); // Set background color
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Add labels and button to the panel
        panel.add(nameLabel);
        panel.add(empNumberLabel);
        panel.add(emailLabel);
        panel.add(ageLabel);
        panel.add(editButton);

        // Add panel to frame
        add(panel);
    }

    private void editUserDetails() {
        // Open the UserDetailEditor frame for editing
        new UserDetailEditor().setVisible(true);
        dispose(); // Close the current frame
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Provide sample data
                new UserDetailDisplay("John Doe", "123456", "john@example.com", "30").setVisible(true);
            }
        });
    }
}