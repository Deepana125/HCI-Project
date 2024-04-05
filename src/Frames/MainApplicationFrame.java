package Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainApplicationFrame extends JFrame {
    private JPanel sidebarPanel;
    private JPanel contentPanel;

    public MainApplicationFrame() {
        setTitle("Main Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        // Initialize sidebar panel
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));

        // Add sidebar items
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHomePage();
            }
        });
        sidebarPanel.add(homeButton);

        JButton roomDesignerButton = new JButton("Room Designer");
        roomDesignerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRoomDesigner();
            }
        });
        sidebarPanel.add(roomDesignerButton);

        // Initialize content panel
        contentPanel = new JPanel(new BorderLayout());

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




    private JPanel createHomePagePanel() {
        JPanel homePagePanel = new JPanel(new BorderLayout());

        // Add the contents of the home page to homePagePanel
        // For example:
        JLabel headingLabel = new JLabel("Home Page", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 36));
        homePagePanel.add(headingLabel, BorderLayout.NORTH);

        // Add more components as needed

        return homePagePanel;
    }

    private void showRoomDesigner() {
        // Create a new instance of RoomDesigner
        RoomDesigner roomDesigner = new RoomDesigner();

        // Display the RoomDesigner frame
        roomDesigner.setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainApplicationFrame mainFrame = new MainApplicationFrame();
                mainFrame.setVisible(true);
            }
        });
    }
}
