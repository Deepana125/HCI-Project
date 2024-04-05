package Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePagePanel extends JPanel {

    private Image backgroundImage;

    public HomePagePanel() {
        setLayout(new BorderLayout());
        setOpaque(false); // Make the panel transparent
        // Load background image
        backgroundImage = new ImageIcon("src/Resources/Homepage.jpg").getImage(); // Change path as necessary

        // Heading label
        JLabel headingLabel = createLabel("Home Page", 40, Color.black);
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(headingLabel, BorderLayout.NORTH);

        // Company name label
        JLabel companyNameLabel = createLabel("Cozy Comfort Furniture", 50,new Color(255, 215, 0));
        companyNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(companyNameLabel, BorderLayout.CENTER);

        // Logo panel
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setOpaque(false); // Make transparent
        ImageIcon logoIcon = new ImageIcon("src/Resources/logo.png"); // Change path as necessary
        JLabel logoLabel = new JLabel(logoIcon);
        logoPanel.add(logoLabel);
        add(logoPanel, BorderLayout.WEST);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 20, 20));
        buttonPanel.setOpaque(false); // Make transparent

        // Create room button
        JButton createRoomButton = createButton("Create a Room", "create_room.png");
        createRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open RoomDesigner frame when the button is clicked
                RoomDesigner roomDesigner = new RoomDesigner();
                roomDesigner.setVisible(true);
            }
        });
        buttonPanel.add(createRoomButton);

        // View furniture button
        JButton viewFurnitureButton = createButton("View Furniture", "view_furniture.png");
        viewFurnitureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open FurnitureDisplay frame when the button is clicked
                FurnitureDisplay furnitureDisplay = new FurnitureDisplay();
                furnitureDisplay.setVisible(true);
            }
        });
        buttonPanel.add(viewFurnitureButton);

        // Saved rooms button
        JButton savedRoomsButton = createButton("Saved Rooms", "saved_rooms.png");
        savedRoomsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open SavedDesignViewer frame when the button is clicked
                SavedDesignViewer savedDesignViewer = new SavedDesignViewer();
                savedDesignViewer.setVisible(true);
            }
        });
        buttonPanel.add(savedRoomsButton);

        // Users button
        JButton usersButton = createButton("Users", "users.png");
        usersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open UserDetailDisplay frame when the button is clicked
                UserDetailDisplay userDetailDisplay = new UserDetailDisplay("Enter Name", "Enter EmpNo", "Enter Email", "Enter Age");
                userDetailDisplay.setVisible(true);
            }
        });
        buttonPanel.add(usersButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private JLabel createLabel(String text, int fontSize, Color textColor) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, fontSize)); // Set font
        label.setForeground(textColor); // Set text color
        return label;
    }

    private JButton createButton(String text, String imagePath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20)); // Set font
        button.setForeground(Color.WHITE); // Set text color
        button.setBackground(new Color(96, 125, 139)); // Set background color to a shade of blue-gray
        button.setFocusPainted(false); // Remove focus border
        button.setBorderPainted(false); // Remove button border
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Set cursor on hover

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(69, 90, 100)); // Darker background color on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(96, 125, 139)); // Restore original background color on exit
            }
        });

        // Set icon for the button
        try {
            ImageIcon icon = new ImageIcon("src/Resources/" + imagePath); // Change path as necessary
            button.setIcon(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set preferred button size
        button.setPreferredSize(new Dimension(200, 50));

        return button;
    }
}