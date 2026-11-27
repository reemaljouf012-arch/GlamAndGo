package glamandgo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.regex.Pattern;
import static glamandgo.Theme.*;

/**
 * Login page.
 * The role comes from the account itself (not from a dropdown):
 *   admin account  -> Admin Dashboard
 *   any other      -> customer pages (Home)
 */
public class LogIn extends JFrame {

    private enum Role { ADMIN, CUSTOMER }

    private static final Pattern EMAIL =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();

    public LogIn() {
        setTitle("Glam & Go | Login");
        setSize(500, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(PINK_50);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(55, 65, 45, 65));

        JLabel brandLabel = label("GLAM & GO", heading(38f), PINK_950);
        JLabel welcomeLabel = label("Welcome Back", heading(23f), PINK_900);
        JLabel subtitleLabel = label("Your beauty journey starts here.", text(13f), PINK_800);
        for (JLabel l : new JLabel[]{brandLabel, welcomeLabel, subtitleLabel}) {
            l.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        // Form
        JPanel formPanel = new JPanel(new GridLayout(5, 1, 0, 10));
        formPanel.setBackground(PINK_100);
        formPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        styleField(emailField);
        styleField(passwordField);

        // Our own button (Mac's default button look ignores colors, so it looked invisible)
        PillButton loginButton = new PillButton("LOG IN", 15f, 20, 10)
                .style(PINK_800, Color.WHITE, null, PINK_950);
        loginButton.addActionListener(e -> doLogin());
        passwordField.addActionListener(e -> doLogin());   // Enter key
        emailField.addActionListener(e -> passwordField.requestFocusInWindow());

        formPanel.add(formLabel("Email"));
        formPanel.add(emailField);
        formPanel.add(formLabel("Password"));
        formPanel.add(passwordField);
        formPanel.add(loginButton);

        JLabel footerLabel = label("GLAM & GO  |  BEAUTY IN EVERY DETAIL", text(10f), PINK_800);
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(brandLabel);
        mainPanel.add(Box.createVerticalStrut(18));
        mainPanel.add(welcomeLabel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(40));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(footerLabel);

        add(mainPanel);
    }

    // ===================== Login logic =====================

    private void doLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            warn("Please enter your email and password.");
            return;
        }
        if (!EMAIL.matcher(email).matches()) {
            warn("Please enter a valid email address.");
            return;
        }

        Role role = authenticate(email, password);
        if (role == null) {
            warn("Incorrect email or password.");
            return;
        }

        if (role == Role.ADMIN) {
            // TODO: replace with  new AdminDashboard().setVisible(true);  when that page is ready
            JOptionPane.showMessageDialog(this, "Admin Dashboard is coming soon.",
                    "Glam & Go", JOptionPane.INFORMATION_MESSAGE);
        } else {
            new Home().setVisible(true);
            dispose();
        }
    }

    /**
     * TEMPORARY check until the database is ready.
     * Later: query the Users table by email, compare the password,
     * and read the account's role (admin / customer) from the "role" column.
     */
    private Role authenticate(String email, String password) {
        if (email.equalsIgnoreCase("admin@glamandgo.com")) {
            return password.equals("admin123") ? Role.ADMIN : null;
        }
        return Role.CUSTOMER;   // any other valid email enters as a customer (testing only)
    }

    // ===================== Styling =====================

    private void warn(String message) {
        JOptionPane.showMessageDialog(this, message, "Login", JOptionPane.WARNING_MESSAGE);
    }

    private JLabel formLabel(String textValue) {
        return label(textValue, text(14f), PINK_950);
    }

    private void styleField(JTextField field) {
        field.setFont(text(14f));
        field.setForeground(PINK_950);
        field.setBackground(PINK_50);
        field.setCaretColor(PINK_900);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PINK_300, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LogIn().setVisible(true));
    }
}
