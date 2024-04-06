package Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class RoomDesignList extends JPanel {

    private JPanel roomListPanel;

    public RoomDesignList() {
        setSize(800, 600);
        setOpaque(false);
        roomListPanel = new JPanel();
        roomListPanel.setLayout(new BoxLayout(roomListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(roomListPanel);
        add(scrollPane);

        loadRoomDesigns();
    }

    private void loadRoomDesigns() {
        roomListPanel.removeAll(); // Clear the panel before loading

        File roomInfoFile = new File("room_info.txt");
        try (Scanner scanner = new Scanner(roomInfoFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Check if the line starts with "Room Number:"
                if (line.startsWith("Room Number:")) {
                    String roomNumber = line.substring(line.indexOf(":") + 2); // Extract room number after ": "
                    JPanel roomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Use FlowLayout for left alignment
                    roomPanel.add(new JLabel("Room Number " + roomNumber + ": admin")); // Assume "admin" as placeholder

                    JButton editButton = new JButton("Edit");
                    editButton.addActionListener(e -> openEditWindow(roomNumber));
                    roomPanel.add(editButton);

                    roomListPanel.add(roomPanel); // Add the panel for each room to the list
                    roomListPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Space between entries
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load room designs.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Revalidate and repaint to update UI
        roomListPanel.revalidate();
        roomListPanel.repaint();
    }



    private void openEditWindow(String roomNumber) {
        EditRoomDesign editRoomDesign = new EditRoomDesign(roomNumber);
        editRoomDesign.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RoomDesignList designList = new RoomDesignList();
            designList.setVisible(true);
        });
    }
}
