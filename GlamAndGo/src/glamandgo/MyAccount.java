
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

    // ===================== Profile =====================

    private JComponent buildProfile() {

        String currentName = UserAccount.getCurrentUserName();
        String currentEmail = UserAccount.getCurrentUserEmail();

        final String name =
                (currentName == null || currentName.trim().isEmpty())
                ? "Guest" : currentName;

        final String email =
                (currentEmail == null || currentEmail.trim().isEmpty())
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

        JLabel userName = label(
                name,
                heading(30f),
                PINK_900
        );

        JLabel userEmail = label(
                email,
                text(16f),
                PINK_700
        );

        JLabel editProfile = label(
                "Edit Profile",
                text(14f),
                PINK_600
        );

        editProfile.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        editProfile.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showPersonalInformation();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                editProfile.setForeground(PINK_900);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                editProfile.setForeground(PINK_600);
            }
        });

        info.add(userName);
        info.add(Box.createVerticalStrut(5));
        info.add(userEmail);
        info.add(Box.createVerticalStrut(9));
        info.add(editProfile);

        content.add(avatar, BorderLayout.WEST);
        content.add(info, BorderLayout.CENTER);

        return new Section(PINK_50, 45, 1100, content);
    }

    // ===================== Account Content =====================

    private JComponent buildAccountContent() {

        JPanel content = vbox();

        JLabel title = label(
                "My Account",
                heading(27f),
                PINK_900
        );

        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(title);
        content.add(Box.createVerticalStrut(18));

        JPanel links = vbox();

        links.setBackground(Color.WHITE);
        links.setOpaque(true);
        links.setBorder(new EmptyBorder(8, 25, 8, 25));

        addAccountLink(
                links,
                "Personal Information",
                this::showPersonalInformation
        );

        addDivider(links);

        addAccountLink(
                links,
                "My Appointments",
                () -> openPage("My Appointments")
        );

        addDivider(links);

        addAccountLink(
                links,
                "My Orders",
                () -> showUnavailable("My Orders")
        );

        addDivider(links);

        addAccountLink(
                links,
                "Payment Methods",
                () -> showUnavailable("Payment Methods")
        );

        content.add(links);

        content.add(Box.createVerticalStrut(25));

        PillButton logout = new PillButton(
                "Log Out", 15f, 28, 10)
                .style(
                        Color.WHITE,
                        PINK_800,
                        PINK_300,
                        PINK_100
                );

        logout.setAlignmentX(Component.LEFT_ALIGNMENT);
        logout.addActionListener(e -> logout());

        content.add(logout);

        return new Section(Color.WHITE, 35, 1100, content);
    }

    // ===================== Account Links =====================

    private void addAccountLink(
            JPanel parent,
            String title,
            Runnable action) {

        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(17, 0, 17, 0));

        JLabel textLabel = label(
                title,
                text(17f),
                PINK_900
        );

        JLabel arrow = label(
                "›",
                heading(27f),
                PINK_500
        );

        row.add(textLabel, BorderLayout.CENTER);
        row.add(arrow, BorderLayout.EAST);

        row.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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

    private void showPersonalInformation() {

        String name = UserAccount.getCurrentUserName();
        String email = UserAccount.getCurrentUserEmail();

        JOptionPane.showMessageDialog(
                this,
                "Name: " + (name == null ? "" : name)
                + "\nEmail: " + (email == null ? "" : email),
                "Personal Information",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ===================== Navigation =====================

    private void openPage(String page) {

        switch (page) {

            case "My Appointments":
            case "Appointments":
                new MyAppointments().setVisible(true);
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

        switch (page) {

            case "My Account":
                // Already on the My Account page.
                break;

            case "Home":
            case "Services":
            case "Products":
            case "Cart":
            case "Shopping Cart":
            case "My Appointments":
            case "Appointments":
                openPage(page);
                break;

            default:
                showUnavailable(page);
        }
    }

    // ===================== Unavailable Sections =====================

    private void showUnavailable(String page) {

        JOptionPane.showMessageDialog(
                this,
                page + " section is not connected yet.",
                "My Account",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ===================== Logout =====================

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

    // ===================== Main =====================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new MyAccount().setVisible(true)
        );
    }
}
