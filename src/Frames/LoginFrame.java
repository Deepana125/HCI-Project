package Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Login");
        setSize(800, 450); // Increased the size further
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Panel for holding the components
        JPanel panel = new JPanel() {
            // Override paintComponent to draw background image
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load and draw background image
                ImageIcon icon = new ImageIcon("src/Resources/login.png"); // Change "background.jpg" to your image file path
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.decode("#B2BEB5")); // Set background color

        // Label for heading
        JLabel headingLabel = new JLabel("Cozy Comfort Furniture", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 36)); // Increased font size
        headingLabel.setForeground(new Color(96, 125, 139)); // Set text color
        headingLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0)); // Added padding

        // Panel for holding login components
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false); // Make panel transparent

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username label and text field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.black); // Set text color
        JTextField usernameField = new JTextField(20);
        loginPanel.add(usernameLabel, gbc);
        gbc.gridy++;
        loginPanel.add(usernameField, gbc);

        // Password label and password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.black); // Set text color
        gbc.gridy++; // Increment y position to move to the next row
        loginPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridy++;
        loginPanel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(150, 40)); // Increased button size
        gbc.gridy++;
        gbc.gridwidth = 2; // Span two columns
        loginPanel.add(loginButton, gbc);

        // Action listener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (username.equals("admin") && password.equals("admin")) {
                    dispose(); // Close the login frame
                    MainApplicationFrame mainFrame = new MainApplicationFrame(); // Open the main application frame
                    mainFrame.setVisible(true); // Display the main frame
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add components to the main panel
        panel.add(headingLabel, BorderLayout.NORTH);
        panel.add(loginPanel, BorderLayout.CENTER);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
