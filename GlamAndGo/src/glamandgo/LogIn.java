/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package glamandgo;


import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LogIn extends JFrame {

private JTextField usernameField;
private JPasswordField passwordField;

public LoginFrame() {

    setTitle("Glam & Go - Login");
    setSize(450, 550);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);

    // Main Panel
    JPanel mainPanel = new JPanel();
    mainPanel.setBackground(new Color(38, 24, 48));
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(new EmptyBorder(45, 55, 35, 55));

    // Application Title
    JLabel title = new JLabel("GLAM & GO");
    title.setFont(new Font("Serif", Font.BOLD, 32));
    title.setForeground(new Color(225, 173, 213));
    title.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel subtitle = new JLabel("Welcome Back!");
    subtitle.setFont(new Font("SansSerif", Font.PLAIN, 18));
    subtitle.setForeground(Color.WHITE);
    subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Username
    JLabel usernameLabel = new JLabel("Username");
    styleLabel(usernameLabel);

    usernameField = new JTextField();
    styleField(usernameField);

    // Password
    JLabel passwordLabel = new JLabel("Password");
    styleLabel(passwordLabel);

    passwordField = new JPasswordField();
    styleField(passwordField);

    // Login Button
    JButton loginButton = new JButton("LOGIN");
    loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
    loginButton.setBackground(new Color(225, 173, 213));
    loginButton.setForeground(new Color(38, 24, 48));
    loginButton.setFocusPainted(false);
    loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    loginButton.setMaximumSize(new Dimension(340, 45));

    loginButton.addActionListener(e -> login());

    // Sign Up
    JLabel signupLabel = new JLabel("Don't have an account? Sign Up");
    signupLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    signupLabel.setForeground(new Color(225, 173, 213));
    signupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Add Components
    mainPanel.add(title);
    mainPanel.add(Box.createVerticalStrut(10));
    mainPanel.add(subtitle);
    mainPanel.add(Box.createVerticalStrut(40));

    mainPanel.add(usernameLabel);
    mainPanel.add(Box.createVerticalStrut(8));
    mainPanel.add(usernameField);
    mainPanel.add(Box.createVerticalStrut(22));

    mainPanel.add(passwordLabel);
    mainPanel.add(Box.createVerticalStrut(8));
    mainPanel.add(passwordField);
    mainPanel.add(Box.createVerticalStrut(30));

    mainPanel.add(loginButton);
    mainPanel.add(Box.createVerticalStrut(25));
    mainPanel.add(signupLabel);

    add(mainPanel);
}

private void styleLabel(JLabel label) {
    label.setFont(new Font("SansSerif", Font.BOLD, 14));
    label.setForeground(Color.WHITE);
    label.setAlignmentX(Component.LEFT_ALIGNMENT);
}

private void styleField(JTextField field) {
    field.setFont(new Font("SansSerif", Font.PLAIN, 14));
    field.setMaximumSize(new Dimension(340, 40));
    field.setPreferredSize(new Dimension(340, 40));
}

private void login() {
    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());

    if (username.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(
            this, "Please enter username and password."
        );
    } else {
        JOptionPane.showMessageDialog(
            this, "Login button is working!"
        );
    }
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        new LoginFrame().setVisible(true);
    });
}}