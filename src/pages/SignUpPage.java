package pages;
import dal.users.usersDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import pages.LoginPage;

public class SignUpPage extends JFrame {
    private final JTextField usernameField;
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JPasswordField passwordField;
    private final JButton signUpButton;

    public SignUpPage() {
        setTitle("Sign Up");
        setSize(400, 350); // Increased height slightly
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.PINK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        // Title label
        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(usernameField, gbc);

        // First Name
        JLabel firstNameLabel = new JLabel("First Name:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(firstNameLabel, gbc);

        firstNameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(firstNameField, gbc);

        // Last Name
        JLabel lastNameLabel = new JLabel("Last Name:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(lastNameLabel, gbc);

        lastNameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(lastNameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(passwordField, gbc);

        // Sign Up Button
        signUpButton = new JButton("Sign Up");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(signUpButton, gbc);

        signUpButton.addActionListener(e -> handleLogin());

        // "Go back to Log In" Button
        JButton backToLoginButton = new JButton("Go back to Log In");
        backToLoginButton.setBackground(Color.LIGHT_GRAY);
        backToLoginButton.addActionListener(e -> {
            dispose(); // Close sign-up page
            new LoginPage().setVisible(true); // Open login page
        });
        gbc.gridy = 6;
        add(backToLoginButton, gbc);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter valid information.");
            return;
        }

        try {
            boolean success = usersDAO.createUser(username, firstName, lastName, password);
            if (success) {
                JOptionPane.showMessageDialog(this, "Sign-up successful!");
                this.dispose();
                new LoginPage(); // Assumes LoginPage extends JFrame
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                JOptionPane.showMessageDialog(this, "Username already exists. Please choose another.");
            } else {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
            }
        }
    }
}
