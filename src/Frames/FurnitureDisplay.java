package Frames;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FurnitureDisplay extends JFrame {

    public FurnitureDisplay() {
        setTitle("Furniture Display");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout()); // Use BorderLayout for the main frame

        // Add a heading panel with the title
        JLabel headingLabel = new JLabel("Furniture Display", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JPanel headingPanel = new JPanel(new BorderLayout());
        headingPanel.add(headingLabel, BorderLayout.CENTER);
        add(headingPanel, BorderLayout.NORTH);

        // Create a panel for furniture images
        JPanel furniturePanel = new JPanel(new GridLayout(2, 2, 10, 10)); // Adjust the layout as needed

        // Add furniture images to the furniture panel
        addFurniture(furniturePanel, "src/Resources/bed.jpg", "Bed");
        addFurniture(furniturePanel, "src/Resources/cupboard.jpg", "Cupboard");
        addFurniture(furniturePanel, "src/Resources/chair.jpg", "Chair");
        addFurniture(furniturePanel, "src/Resources/bookshelf1.jpg", "Bookshelf");

        // Add the furniture panel to the main frame
        add(furniturePanel, BorderLayout.CENTER);
    }

    private void addFurniture(JPanel panel, String imagePath, String type) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            ImageIcon icon = new ImageIcon(image.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
            JLabel furnitureLabel = new JLabel(icon);
            JLabel nameLabel = new JLabel(type, SwingConstants.CENTER);

            JPanel furniturePanel = new JPanel(new BorderLayout());
            furniturePanel.add(furnitureLabel, BorderLayout.CENTER);
            furniturePanel.add(nameLabel, BorderLayout.SOUTH);
            furniturePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

            panel.add(furniturePanel);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FurnitureDisplay display = new FurnitureDisplay();
            display.setVisible(true);
        });
    }
}
