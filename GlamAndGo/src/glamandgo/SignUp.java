package glamandgo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.regex.Pattern;

import static glamandgo.Theme.*;

public class SignUp extends JFrame {

    private static final Pattern EMAIL =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField confirmPasswordField = new JPasswordField();

    public SignUp() {
        setTitle("Glam & Go | Sign Up");
        setSize(520, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(PINK_50);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(35, 65, 30, 65));

        JLabel brand = label("GLAM & GO", heading(36f), PINK_950);
        JLabel title = label("Create Your Account", heading(24f), PINK_900);
        JLabel subtitle = label(
                "Join us and discover your beauty.", text(13f), PINK_800
        );

        for (JLabel item : new JLabel[]{brand, title, subtitle}) {
            item.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        JPanel form = new JPanel(new GridLayout(9, 1, 0, 8));
        form.setBackground(PINK_100);
        form.setBorder(new EmptyBorder(22, 24, 22, 24));

        styleField(nameField);
        styleField(emailField);
        styleField(passwordField);
        styleField(confirmPasswordField);

        PillButton signUpButton = new PillButton("CREATE ACCOUNT", 15f, 20, 10)
                .style(PINK_800, Color.WHITE, null, PINK_950);

        signUpButton.addActionListener(e -> createAccount());
        confirmPasswordField.addActionListener(e -> createAccount());

        form.add(formLabel("Full Name"));
        form.add(nameField);
        form.add(formLabel("Email"));
        form.add(emailField);
        form.add(formLabel("Password"));
        form.add(passwordField);
        form.add(formLabel("Confirm Password"));
        form.add(confirmPasswordField);
        form.add(signUpButton);

        JLabel loginPrompt = label(
                "Already have an account? Log In", text(13f), PINK_800
        );

        loginPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPrompt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        loginPrompt.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new LogIn().setVisible(true);
                dispose();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                loginPrompt.setForeground(PINK_500);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                loginPrompt.setForeground(PINK_800);
            }
        });

        JLabel footer = label(
                "GLAM & GO  |  BEAUTY IN EVERY DETAIL", text(10f), PINK_800
        );
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(brand);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(7));
        mainPanel.add(subtitle);
        mainPanel.add(Box.createVerticalStrut(25));
        mainPanel.add(form);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(loginPrompt);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(footer);

        add(mainPanel);
    }

    // Registration
    private void createAccount() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (name.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {
            warn("Please fill in all fields.");
            return;
        }

        if (name.length() < 2) {
            warn("Please enter your full name.");
            return;
        }

        if (!EMAIL.matcher(email).matches()) {
            warn("Please enter a valid email address.");
            return;
        }

        if (password.length() < 6) {
            warn("Password must contain at least 6 characters.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            warn("Passwords do not match.");
            return;
        }

        if (!UserAccount.register(name, email, password)) {
            warn("An account with this email already exists.");
            return;
        }

        showSuccessDialog();
        new LogIn().setVisible(true);
        dispose();
    }

    // Success dialog
    private void showSuccessDialog() {
        JDialog dialog = new JDialog(this, "Welcome to Glam & Go", true);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(PINK_50);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PINK_300),
                new EmptyBorder(28, 36, 26, 36)
        ));

        JLabel title = label("Welcome to Glam & Go", heading(24f), PINK_950);
        JLabel message = label(
                "Your account has been created successfully!", text(14f), PINK_800
        );

        PillButton loginButton = new PillButton("LOG IN", 14f, 28, 10)
                .style(PINK_800, Color.WHITE, null, PINK_950);
        loginButton.addActionListener(e -> dialog.dispose());

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(10));
        card.add(message);
        card.add(Box.createVerticalStrut(22));
        card.add(loginButton);

        dialog.setContentPane(card);
        dialog.getRootPane().setDefaultButton(loginButton);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Form styling
    private JLabel formLabel(String value) {
        return label(value, text(14f), PINK_950);
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

    private void warn(String message) {
        JOptionPane.showMessageDialog(
                this, message, "Sign Up", JOptionPane.WARNING_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUp().setVisible(true));
    }
}