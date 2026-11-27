
package glamandgo;

import java.awt.*;
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
            protected void paintComponent(Graphics gr) {
                Graphics2D g = g2(gr);
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
        edit.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                editPersonalInformation();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                edit.setForeground(PINK_900);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                edit.setForeground(PINK_600);
            }
        });

        info.add(edit);

        content.add(avatar, BorderLayout.WEST);
        content.add(info, BorderLayout.CENTER);

        return new Section(PINK_50, 45, 1100, content);
    }

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

        addAccountLink(links, "Personal Information",
                this::editPersonalInformation);

        addDivider(links);

        addAccountLink(links, "My Appointments",
                () -> openPage("My Appointments"));

        addDivider(links);

        addAccountLink(links, "My Orders",
                () -> openPage("My Orders"));

        addDivider(links);

        addAccountLink(links, "Payment Methods",
                () -> openPage("Payment Methods"));

        content.add(links);
        content.add(Box.createVerticalStrut(25));

        PillButton logout = new PillButton(
                "Log Out", 15f, 28, 10
        ).style(Color.WHITE, PINK_800, PINK_300, PINK_100);

        logout.setAlignmentX(Component.LEFT_ALIGNMENT);
        logout.addActionListener(e -> logout());
        content.add(logout);

        return new Section(Color.WHITE, 35, 1100, content);
    }

    private void addAccountLink(
            JPanel parent,
            String title,
            Runnable action) {

        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(17, 0, 17, 0));

        JLabel textLabel = label(title, text(17f), PINK_900);
        JLabel arrow = label("›", heading(27f), PINK_500);

        row.add(textLabel, BorderLayout.CENTER);
        row.add(arrow, BorderLayout.EAST);

        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        row.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                textLabel.setForeground(PINK_500);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
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

    // ===================== Personal Information =====================

    private void editPersonalInformation() {
        String currentName = UserAccount.getCurrentUserName();
        String currentEmail = UserAccount.getCurrentUserEmail();

        if (currentName == null || currentEmail == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please log in first.",
                    "My Account",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        JTextField nameField = new JTextField(currentName, 22);
        JTextField emailField = new JTextField(currentEmail, 22);

        JPanel form = new JPanel(new GridLayout(0, 1, 5, 8));
        form.add(label("Full Name", text(15f), PINK_800));
        form.add(nameField);
        form.add(label("Email Address", text(15f), PINK_800));
        form.add(emailField);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                "Edit Personal Information",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String newName = nameField.getText().trim();
        String newEmail = emailField.getText().trim();

        if (newName.isEmpty() || newEmail.isEmpty()
                || !newEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid name and email.",
                    "Invalid Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        boolean updated = UserAccount.updateCurrentUser(newName, newEmail);

        if (!updated) {
            JOptionPane.showMessageDialog(
                    this,
                    "This email is already used by another account.",
                    "Update Failed",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Your information has been updated.",
                "My Account",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();
        new MyAccount().setVisible(true);
    }

    // ===================== Page Navigation =====================

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
            new LogIn().setVisible(true);
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> new MyAccount().setVisible(true)
        );
    }
}
