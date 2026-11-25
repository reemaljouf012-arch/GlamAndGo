/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package glamandgo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LogIn extends JFrame {

    // Glam & Go Color Palette
    private final Color PINK_50  = Color.decode("#FFF3F7");
    private final Color PINK_100 = Color.decode("#FFE4EC");
    private final Color PINK_200 = Color.decode("#F9CEDB");
    private final Color PINK_300 = Color.decode("#EAB3C4");
    private final Color PINK_500 = Color.decode("#C17690");
    private final Color PINK_800 = Color.decode("#693146");
    private final Color PINK_900 = Color.decode("#4F1D31");
    private final Color PINK_950 = Color.decode("#390C1F");

    // Fonts
    private final String HEADING_FONT = "Recia";
    private final String TEXT_FONT = "Sentient";

    public LogIn() {

        setTitle("Glam & Go | Login");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main background
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(PINK_50);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(55, 65, 45, 65));

        // Brand name
        JLabel brandLabel = new JLabel("GLAM & GO");
        brandLabel.setFont(new Font(HEADING_FONT, Font.PLAIN, 38));
        brandLabel.setForeground(PINK_950);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(new Font(HEADING_FONT, Font.PLAIN, 23));
        welcomeLabel.setForeground(PINK_900);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Your beauty journey starts here.");
        subtitleLabel.setFont(new Font(TEXT_FONT, Font.PLAIN, 13));
        subtitleLabel.setForeground(PINK_800);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 1, 0, 10));
        formPanel.setBackground(PINK_100);
        formPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Username
        JLabel usernameLabel = new JLabel("Username");
        styleLabel(usernameLabel);

        JTextField usernameField = new JTextField();
        styleField(usernameField);

        // Password
        JLabel passwordLabel = new JLabel("Password");
        styleLabel(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        styleField(passwordField);

        // Login button
        JButton loginButton = new JButton("LOG IN");
        loginButton.setFont(new Font(TEXT_FONT, Font.BOLD, 14));
        loginButton.setForeground(PINK_50);
        loginButton.setBackground(PINK_500);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Login action
        loginButton.addActionListener(e -> {

            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please enter your username and password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Welcome to Glam & Go!",
                    "Login",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        // Add form components
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(loginButton);

        // Footer
        JLabel footerLabel = new JLabel("GLAM & GO  |  BEAUTY IN EVERY DETAIL");
        footerLabel.setFont(new Font(TEXT_FONT, Font.PLAIN, 10));
        footerLabel.setForeground(PINK_800);
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Build page
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

    // Label styling
    private void styleLabel(JLabel label) {
        label.setFont(new Font(TEXT_FONT, Font.PLAIN, 14));
        label.setForeground(PINK_950);
    }

    // Text field styling
    private void styleField(JTextField field) {
        field.setFont(new Font(TEXT_FONT, Font.PLAIN, 14));
        field.setForeground(PINK_950);
        field.setBackground(PINK_50);
        field.setCaretColor(PINK_900);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PINK_300, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LogIn().setVisible(true);
        });
    }
}