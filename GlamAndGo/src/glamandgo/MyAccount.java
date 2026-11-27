package glamandgo;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static glamandgo.Theme.*;

public class MyAccount extends JFrame {

    public MyAccount() {
        setTitle("My Account");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 820);
        setMinimumSize(new Dimension(1100, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(navbar(this::navigate), BorderLayout.NORTH);

        PageBody body = new PageBody();
        body.add(buildProfile());
        body.add(buildAccountContent());
        body.add(footer(this::navigate));

        JScrollPane scroll = new JScrollPane(
                body,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        add(scroll, BorderLayout.CENTER);
    }

    private boolean isLoggedIn() {
        String email = UserAccount.getCurrentUserEmail();
        return email != null && !email.trim().isEmpty();
    }

    private void openLogin() {
        new LogIn().setVisible(true);
        dispose();
    }

    // Profile

    private JComponent buildProfile() {
        String currentName = UserAccount.getCurrentUserName();
        String currentEmail = UserAccount.getCurrentUserEmail();

        final String name = currentName == null || currentName.trim().isEmpty()
                ? "Guest" : currentName;

        final String email = currentEmail == null || currentEmail.trim().isEmpty()
                ? "No email available" : currentEmail;

        JPanel content = new JPanel(new BorderLayout(22, 0));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(10, 0, 10, 0));

        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D g = g2(graphics);
                int size = Math.min(getWidth(), getHeight());

                g.setColor(PINK_200);
                g.fillOval(0, 0, size, size);

                g.setColor(PINK_900);
                g.setFont(heading(38f));

                String letter = name.substring(0, 1).toUpperCase();
                FontMetrics fm = g.getFontMetrics();

                g.drawString(
                        letter,
                        (size - fm.stringWidth(letter)) / 2,
                        (size - fm.getHeight()) / 2 + fm.getAscent()
                );

                g.dispose();
            }
        };

        avatar.setPreferredSize(new Dimension(85, 85));
        avatar.setOpaque(false);

        JPanel info = vbox();
        info.add(label(name, heading(30f), PINK_900));
        info.add(Box.createVerticalStrut(5));
        info.add(label(email, text(16f), PINK_700));
        info.add(Box.createVerticalStrut(9));

        JLabel edit = label("Edit Profile", text(14f), PINK_600);
        edit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        edit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                editPersonalInformation();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                edit.setForeground(PINK_900);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                edit.setForeground(PINK_600);
            }
        });

        info.add(edit);

        content.add(avatar, BorderLayout.WEST);
        content.add(info, BorderLayout.CENTER);

        return new Section(PINK_50, 45, 1100, content);
    }

    // Account links

    private JComponent buildAccountContent() {
        JPanel content = vbox();

        JLabel title = label("My Account", heading(27f), PINK_900);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(title);
        content.add(Box.createVerticalStrut(18));

        JPanel links = vbox();
        links.setBackground(Color.WHITE);
        links.setOpaque(true);
        links.setBorder(new EmptyBorder(8, 25, 8, 25));

        addAccountLink(links, "Personal Information", this::editPersonalInformation);
        addDivider(links);

        addAccountLink(links, "My Appointments", () -> openPage("My Appointments"));
        addDivider(links);

        addAccountLink(links, "My Orders", () -> openPage("My Orders"));
        addDivider(links);

        addAccountLink(links, "Payment Methods", () -> openPage("Payment Methods"));

        content.add(links);
        content.add(Box.createVerticalStrut(25));

        boolean loggedIn = isLoggedIn();
        String buttonText = loggedIn ? "Log Out" : "Log In";

        PillButton accountButton = new PillButton(buttonText, 15f, 28, 10)
                .style(Color.WHITE, PINK_800, PINK_300, PINK_100);

        accountButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (loggedIn) {
            accountButton.addActionListener(e -> logout());
        } else {
            accountButton.addActionListener(e -> openLogin());
        }

        content.add(accountButton);

        return new Section(Color.WHITE, 35, 1100, content);
    }

    private void addAccountLink(JPanel parent, String title, Runnable action) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(17, 0, 17, 0));
        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel textLabel = label(title, text(17f), PINK_900);
        JLabel arrow = label("›", heading(27f), PINK_500);

        row.add(textLabel, BorderLayout.CENTER);
        row.add(arrow, BorderLayout.EAST);

        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                textLabel.setForeground(PINK_500);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                textLabel.setForeground(PINK_900);
            }
        });

        parent.add(row);
    }

    private void addDivider(JPanel parent) {
        JSeparator line = new JSeparator();
        line.setForeground(PINK_100);
        parent.add(line);
    }

    // Guest login dialog

    private void showLoginRequiredDialog() {
        JDialog dialog = new JDialog(this, "Log in to edit your profile", true);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(PINK_50);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PINK_300),
                new EmptyBorder(24, 28, 22, 28)
        ));

        JLabel title = label(
                "Log in to edit your profile", heading(24f), PINK_900
        );

        JLabel message = label(
                "Log in to update your name and email address.",
                text(15f),
                PINK_800
        );

        PillButton cancel = new PillButton("Cancel", 14f, 20, 8)
                .style(Color.WHITE, PINK_800, PINK_300, PINK_100);
        cancel.addActionListener(e -> dialog.dispose());

        PillButton login = new PillButton("Log In", 14f, 20, 8)
                .style(PINK_800, Color.WHITE, null, PINK_950);

        login.addActionListener(e -> {
            dialog.dispose();
            openLogin();
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttons.add(cancel);
        buttons.add(login);

        card.add(title, BorderLayout.NORTH);
        card.add(message, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.SOUTH);

        dialog.setContentPane(card);
        dialog.getRootPane().setDefaultButton(login);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Personal information

    private void editPersonalInformation() {
        String currentName = UserAccount.getCurrentUserName();
        String currentEmail = UserAccount.getCurrentUserEmail();

        if (!isLoggedIn() || currentName == null) {
            showLoginRequiredDialog();
            return;
        }

        JDialog dialog = new JDialog(this, "Edit Personal Information", true);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel card = new JPanel(new BorderLayout(0, 18));
        card.setBackground(PINK_50);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PINK_300),
                new EmptyBorder(24, 28, 22, 28)
        ));

        JLabel title = label("Edit Personal Information", heading(24f), PINK_900);
        card.add(title, BorderLayout.NORTH);

        JTextField nameField = new JTextField(currentName, 24);
        JTextField emailField = new JTextField(currentEmail, 24);

        for (JTextField field : new JTextField[]{nameField, emailField}) {
            field.setFont(text(15f));
            field.setForeground(PINK_950);
            field.setBackground(Color.WHITE);
            field.setCaretColor(PINK_900);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PINK_300),
                    new EmptyBorder(8, 10, 8, 10)
            ));
        }

        JPanel fields = new JPanel(new GridLayout(0, 1, 0, 5));
        fields.setOpaque(false);
        fields.add(label("Full Name", text(14f), PINK_800));
        fields.add(nameField);
        fields.add(label("Email Address", text(14f), PINK_800));
        fields.add(emailField);

        JLabel feedback = label(" ", text(13f), PINK_900);

        JPanel form = new JPanel(new BorderLayout(0, 8));
        form.setOpaque(false);
        form.add(fields, BorderLayout.CENTER);
        form.add(feedback, BorderLayout.SOUTH);
        card.add(form, BorderLayout.CENTER);

        PillButton cancel = new PillButton("Cancel", 14f, 18, 8)
                .style(Color.WHITE, PINK_800, PINK_300, PINK_100);
        cancel.addActionListener(e -> dialog.dispose());

        PillButton save = new PillButton("Save Changes", 14f, 18, 8)
                .style(PINK_800, Color.WHITE, null, PINK_950);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(cancel);
        buttons.add(save);
        card.add(buttons, BorderLayout.SOUTH);

        save.addActionListener(e -> {
            String newName = nameField.getText().trim();
            String newEmail = emailField.getText().trim();

            // Ignore surrounding spaces and email letter case.
            if (newName.equals(currentName.trim())
                    && newEmail.equalsIgnoreCase(currentEmail.trim())) {
                dialog.dispose();
                return;
            }

            if (newName.isEmpty()) {
                feedback.setText("Please enter your full name.");
                nameField.requestFocusInWindow();
                return;
            }

            if (!newEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                feedback.setText("Please enter a valid email address.");
                emailField.requestFocusInWindow();
                return;
            }

            if (!UserAccount.updateCurrentUser(newName, newEmail)) {
                feedback.setText("Unable to save. This email may already be in use.");
                emailField.requestFocusInWindow();
                return;
            }

            title.setText("Personal Information");
            card.remove(form);
            card.add(
                    label("Your information has been updated.", text(15f), PINK_800),
                    BorderLayout.CENTER
            );

            PillButton done = new PillButton("Done", 14f, 20, 8)
                    .style(PINK_800, Color.WHITE, null, PINK_950);
            done.addActionListener(event -> dialog.dispose());

            buttons.removeAll();
            buttons.add(done);
            dialog.getRootPane().setDefaultButton(done);

            card.revalidate();
            card.repaint();
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            done.requestFocusInWindow();
        });

        dialog.setContentPane(card);
        dialog.getRootPane().setDefaultButton(save);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        if (!currentName.equals(UserAccount.getCurrentUserName())
                || !currentEmail.equals(UserAccount.getCurrentUserEmail())) {
            dispose();
            new MyAccount().setVisible(true);
        }
    }

    // Navigation

    private void openPage(String page) {
        switch (page) {
            case "My Appointments":
            case "Appointments":
                new MyAppointments().setVisible(true);
                dispose();
                break;

            case "My Orders":
            case "Orders":
                new MyOrders().setVisible(true);
                dispose();
                break;

            case "Payment Methods":
                new PaymentMethods().setVisible(true);
                dispose();
                break;

            case "Home":
                new Home().setVisible(true);
                dispose();
                break;

            case "Services":
                new Services().setVisible(true);
                dispose();
                break;

            case "Products":
                new Products().setVisible(true);
                dispose();
                break;

            case "Cart":
            case "Shopping Cart":
                new ShoppingCart().setVisible(true);
                dispose();
                break;

            default:
                showUnavailable(page);
        }
    }

    private void navigate(String page) {
        if ("My Account".equals(page)) {
            return;
        }
        openPage(page);
    }

    private void showUnavailable(String page) {
        JOptionPane.showMessageDialog(
                this,
                page + " section is not connected yet.",
                "My Account",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Log Out",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            UserAccount.logout();
            openLogin();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MyAccount().setVisible(true));
    }
}