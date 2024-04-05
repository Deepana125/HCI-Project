package Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SavedDesignViewer extends JFrame {

    private JComboBox<String> designDropdown;
    private JLabel designImageLabel;
    private JTextArea designDetailsTextArea;
    private JTextField customerNameField;
    private JTextField emailAddressField;
    private JTextField contactNumberField;
    private JButton deleteButton;
    private JButton saveDetailsButton;

    public SavedDesignViewer() {
        setTitle("Saved Designs Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null); // Center the frame on the screen

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        designDropdown = new JComboBox<>();
        designDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSelectedDesign();
            }
        });

        JPanel dropdownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dropdownPanel.add(new JLabel("Select Design: "));
        dropdownPanel.add(designDropdown);

        designImageLabel = new JLabel();
        designImageLabel.setHorizontalAlignment(JLabel.CENTER);

        designDetailsTextArea = new JTextArea(5, 20);
        designDetailsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(designDetailsTextArea);

        JPanel detailsPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        detailsPanel.add(new JLabel("Customer Name: "));
        customerNameField = new JTextField();
        detailsPanel.add(customerNameField);
        detailsPanel.add(new JLabel("Email Address: "));
        emailAddressField = new JTextField();
        detailsPanel.add(emailAddressField);
        detailsPanel.add(new JLabel("Contact Number: "));
        contactNumberField = new JTextField();
        detailsPanel.add(contactNumberField);

        saveDetailsButton = new JButton("Save Details");
        saveDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDesignDetails();
            }
        });
        detailsPanel.add(saveDetailsButton);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedDesign();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(deleteButton);

        panel.add(dropdownPanel, BorderLayout.NORTH);
        panel.add(designImageLabel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.EAST);
        panel.add(detailsPanel, BorderLayout.SOUTH);
        panel.add(buttonPanel, BorderLayout.PAGE_END);

        getContentPane().add(panel);
        refreshDesigns();
    }

    private void refreshDesigns() {
        designDropdown.removeAllItems();

        File directory = new File("C:\\My_use\\NSBM\\NSBM 3rd Year\\HCI\\Final HCI Project\\HCI_Final_Project");
        File[] files = directory.listFiles((dir, name) -> name.endsWith("_coordinates.txt"));

        if (files != null) {
            for (File file : files) {
                designDropdown.addItem(file.getName());
            }
        }
    }

    private void showSelectedDesign() {
        String selectedDesign = (String) designDropdown.getSelectedItem();
        if (selectedDesign != null) {
            try {
                Scanner scanner = new Scanner(new File(selectedDesign));
                StringBuilder details = new StringBuilder();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    details.append(line).append("\n");
                }
                scanner.close();
                designDetailsTextArea.setText(details.toString());

                // Display image
                String imagePath = selectedDesign.replace("_coordinates.txt", ".png");
                ImageIcon icon = new ImageIcon(imagePath);
                designImageLabel.setIcon(icon);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDesignDetails() {
        String selectedDesign = (String) designDropdown.getSelectedItem();
        if (selectedDesign != null) {
            try {
                String customerName = customerNameField.getText().trim();
                String emailAddress = emailAddressField.getText().trim();
                String contactNumber = contactNumberField.getText().trim();

                File designFile = new File(selectedDesign);
                FileWriter writer = new FileWriter(designFile, true);

                if (!customerName.isEmpty()) {
                    writer.write("Customer Name: " + customerName + "\n");
                }
                if (!emailAddress.isEmpty()) {
                    writer.write("Email Address: " + emailAddress + "\n");
                }
                if (!contactNumber.isEmpty()) {
                    writer.write("Contact Number: " + contactNumber + "\n");
                }

                writer.close();
                showSelectedDesign(); // Refresh details shown
                JOptionPane.showMessageDialog(this, "Details saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to save details!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No design selected!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedDesign() {
        String selectedDesign = (String) designDropdown.getSelectedItem();
        if (selectedDesign != null) {
            File designFile = new File(selectedDesign);
            File imageFile = new File(selectedDesign.replace("_info.txt", ".png"));
            if (designFile.delete() && imageFile.delete()) {
                refreshDesigns();
                JOptionPane.showMessageDialog(this, "Design deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete design!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No design selected!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SavedDesignViewer viewer = new SavedDesignViewer();
            viewer.setVisible(true);
        });
    }
}