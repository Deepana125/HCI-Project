package Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainApplicationFrame extends JFrame {
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private RoomDesignList roomDesignList;
    private RoomDesigner roomDesigner;

    public MainApplicationFrame() {
        setTitle("Main Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        // Initialize sidebar panel
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));

        // Initialize content panel
        contentPanel = new JPanel(new BorderLayout());

        // Initialize RoomDesignList
        roomDesignList = new RoomDesignList(); // Initialize RoomDesignList
        roomDesignList.setOpaque(false); // Make the panel transparent

        // Add sidebar items
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> showHomePage());
        sidebarPanel.add(homeButton);

        JButton roomDesignerButton = new JButton("Room Designer");
        roomDesignerButton.addActionListener(e -> showRoomDesignerPanel());
        sidebarPanel.add(roomDesignerButton);

        JButton savedRoomDesignerButton = new JButton("Saved Rooms");
        savedRoomDesignerButton.addActionListener(e -> showSavedRoomDesigner());
        sidebarPanel.add(savedRoomDesignerButton);

        // Initially show the home page
        showHomePage();

        // Add sidebar and content panel to main frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebarPanel, BorderLayout.WEST);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
    }

    private void showHomePage() {
        contentPanel.removeAll();
        HomePagePanel homePagePanel = new HomePagePanel();
        contentPanel.add(homePagePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showRoomDesignerPanel() {
        contentPanel.removeAll();
        roomDesigner = new RoomDesigner();
        contentPanel.add(roomDesigner, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }



    private void showSavedRoomDesigner() {
        contentPanel.removeAll();
        contentPanel.add(roomDesignList, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame mainFrame = new MainApplicationFrame();
            mainFrame.setVisible(true);
        });
    }
}
