package pages;

import dal.admins.AdminDAO;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {
    private final AdminDAO adminDao = new AdminDAO();
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton signInButton;

    public LoginPage() {
        setTitle("Admin Login");
        setSize(500, 400); // Increased height to fit the new button
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main background panel
        JPanel bgPanel = new JPanel(new GridBagLayout());
        bgPanel.setBackground(new Color(0, 225, 255));
        add(bgPanel);

        // Login panel
        JPanel loginPanel = new JPanel();
        loginPanel.setPreferredSize(new Dimension(400, 280));
        loginPanel.setBackground(new Color(255, 255, 255, 220));
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(titleLabel);
        loginPanel.add(Box.createVerticalStrut(15));

        // Username
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("Username:");
        userLabel.setPreferredSize(new Dimension(100, 25));
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(250, 30));
        userPanel.add(userLabel);
        userPanel.add(usernameField);
        loginPanel.add(userPanel);

        // Password
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passPanel.setOpaque(false);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setPreferredSize(new Dimension(100, 25));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(250, 30));
        passPanel.add(passLabel);
        passPanel.add(passwordField);
        loginPanel.add(passPanel);

        // Sign In button
        signInButton = new JButton("Sign In");
        signInButton.setFont(new Font("Arial", Font.BOLD, 14));
        signInButton.setBackground(new Color(59, 89, 182));
        signInButton.setForeground(Color.WHITE);
        signInButton.setFocusPainted(false);
        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInButton.addActionListener(e -> handleLogin());
        loginPanel.add(Box.createVerticalStrut(15));
        loginPanel.add(signInButton);

        // "Make a new account" button
        JButton signUpRedirectButton = new JButton("Make a new account");
        signUpRedirectButton.setFont(new Font("Arial", Font.PLAIN, 12));
        signUpRedirectButton.setBackground(Color.LIGHT_GRAY);
        signUpRedirectButton.setFocusPainted(false);
        signUpRedirectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpRedirectButton.addActionListener(e -> {
            dispose(); // Close current LoginPage
            new SignUpPage().setVisible(true); // Open SignUpPage
        });
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(signUpRedirectButton);

        bgPanel.add(loginPanel);
        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.");
            return;
        }

        boolean valid = adminDao.checkIfAdminExists(username, password);
        if (valid) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            new StudentPage();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
        }
    }
}
