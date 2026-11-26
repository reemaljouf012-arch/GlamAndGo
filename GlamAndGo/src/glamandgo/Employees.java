
package glamandgo;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static glamandgo.Theme.*;

public class Employees extends JFrame {

    private final String serviceName;
    private final String category;

    private final JPanel employeesPanel =
            new JPanel(new GridLayout(1, 2, 22, 25));

    public Employees(String serviceName, String category) {

        this.serviceName = serviceName;
        this.category = category;

        setTitle("Glam & Go | Employees");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1280, 820);
        setMinimumSize(new Dimension(1100, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(navbar(this::navigate), BorderLayout.NORTH);

        PageBody body = new PageBody();

        body.add(buildHero());
        body.add(buildEmployeesSection());
        body.add(footer(this::navigate));

        JScrollPane scroll = new JScrollPane(
                body,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(24);

        add(scroll, BorderLayout.CENTER);

        refreshEmployees();
    }

    // Hero Section
    private JComponent buildHero() {

        JPanel hero = new JPanel();

        hero.setLayout(
                new BoxLayout(hero, BoxLayout.Y_AXIS));

        hero.setBackground(PINK_50);

        hero.setBorder(
                new EmptyBorder(65, 20, 65, 20));

        JLabel title = label(
                "Meet Our Beauty Experts",
                heading(48),
                PINK_950
        );

        JLabel subtitle = label(
                "Choose the perfect employee for your beauty journey.",
                text(18),
                PINK_800
        );

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        hero.add(title);
        hero.add(Box.createVerticalStrut(15));
        hero.add(subtitle);

        return hero;
    }

    // Employees Section
    private JComponent buildEmployeesSection() {

        JPanel section = new JPanel(new BorderLayout());

        section.setBackground(Color.WHITE);

        section.setBorder(
                new EmptyBorder(35, 55, 55, 55));

        JPanel headingPanel = new JPanel();

        headingPanel.setLayout(
                new BoxLayout(headingPanel, BoxLayout.Y_AXIS)
        );

        headingPanel.setBackground(Color.WHITE);

        JLabel sectionTitle = label(
                "Our " + category + " Experts",
                heading(30),
                PINK_900
        );

        JLabel sectionSubtitle = label(
                "Selected for " + serviceName,
                text(16),
                PINK_800
        );

        sectionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        sectionSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headingPanel.add(sectionTitle);
        headingPanel.add(Box.createVerticalStrut(10));
        headingPanel.add(sectionSubtitle);
        headingPanel.add(Box.createVerticalStrut(30));

        employeesPanel.setOpaque(false);

        section.add(headingPanel, BorderLayout.NORTH);

        section.add(employeesPanel, BorderLayout.CENTER);

        return section;
    }

    // Display Employees
    private void refreshEmployees() {

        employeesPanel.removeAll();

        ArrayList<Employee> employees = getEmployees();

        for (Employee employee : employees) {

            employeesPanel.add(
                    buildEmployeeCard(employee));
        }

        employeesPanel.revalidate();

        employeesPanel.repaint();
    }

    // Employee Card
    private JPanel buildEmployeeCard(Employee employee) {

        RoundedPanel card = new RoundedPanel(
                22, PINK_50, true);

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setBorder(
                new EmptyBorder(30, 25, 30, 25));

        JLabel name = label(
                employee.name,
                heading(27),
                PINK_950
        );

        JLabel specialty = label(
                employee.specialty,
                text(15),
                PINK_800
        );

        JLabel rating = label(
                "★ " + employee.rating
                        + "  (" + employee.reviews + " reviews)",
                text(15),
                PINK_800
        );

        JTextArea description = new JTextArea(
                employee.description
        );

        description.setFont(text(15));

        description.setForeground(PINK_800);

        description.setLineWrap(true);

        description.setWrapStyleWord(true);

        description.setEditable(false);

        description.setFocusable(false);

        description.setOpaque(false);

        description.setRows(2);

        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        specialty.setAlignmentX(Component.CENTER_ALIGNMENT);

        rating.setAlignmentX(Component.CENTER_ALIGNMENT);

        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Choose Employee Button
        PillButton chooseButton = new PillButton(
                "Choose Employee", 15, 25, 10
        );

        chooseButton.style(
                PINK_800,
                Color.WHITE,
                null,
                PINK_900
        );

        chooseButton.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        // Open Appointment Booking
        chooseButton.addActionListener(e -> {

            new AppointmentBooking(
                    serviceName,
                    employee.name
            ).setVisible(true);

            dispose();
        });

        card.add(name);

        card.add(Box.createVerticalStrut(12));

        card.add(specialty);

        card.add(Box.createVerticalStrut(18));

        card.add(rating);

        card.add(Box.createVerticalStrut(20));

        card.add(description);

        card.add(Box.createVerticalStrut(30));

        card.add(chooseButton);

        return card;
    }

    // Employee Data
    private ArrayList<Employee> getEmployees() {

        ArrayList<Employee> employees = new ArrayList<>();

        switch (category) {

            case "Hair Care":

                employees.add(new Employee(
                        "Sarah",
                        "Hair Specialist",
                        4.9,
                        120,
                        "Professional hair styling and treatments."
                ));

                employees.add(new Employee(
                        "Nora",
                        "Hair Stylist",
                        4.8,
                        95,
                        "Specialized in modern hair styles."
                ));

                break;

            case "Skincare":

                employees.add(new Employee(
                        "Lama",
                        "Skincare Specialist",
                        4.9,
                        110,
                        "Experienced in facial and skincare treatments."
                ));

                employees.add(new Employee(
                        "Maya",
                        "Beauty Specialist",
                        4.8,
                        87,
                        "Focused on skin care and hydration."
                ));

                break;

            case "Makeup":

                employees.add(new Employee(
                        "Reem",
                        "Makeup Artist",
                        4.9,
                        135,
                        "Professional makeup for every occasion."
                ));

                employees.add(new Employee(
                        "Hala",
                        "Makeup Artist",
                        4.8,
                        102,
                        "Specialized in elegant makeup looks."
                ));

                break;

            case "Nails":

                employees.add(new Employee(
                        "Dana",
                        "Nail Technician",
                        4.9,
                        98,
                        "Professional manicure and nail art."
                ));

                employees.add(new Employee(
                        "Joud",
                        "Nail Technician",
                        4.8,
                        90,
                        "Specialized in nail care and designs."
                ));

                break;
        }

        return employees;
    }

    // Navigation
    private void navigate(String page) {

        switch (page) {

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

            // Shopping Cart Navigation
            case "Cart":
            case "Shopping Cart":

                new ShoppingCart().setVisible(true);

                dispose();

                break;

            default:

                JOptionPane.showMessageDialog(
                        this,
                        "\"" + page + "\" page is not connected yet.",
                        "Glam & Go",
                        JOptionPane.INFORMATION_MESSAGE
                );
        }
    }

    // Employee Class
    private static class Employee {

        String name;
        String specialty;
        double rating;
        int reviews;
        String description;

        Employee(
                String name,
                String specialty,
                double rating,
                int reviews,
                String description
        ) {

            this.name = name;
            this.specialty = specialty;
            this.rating = rating;
            this.reviews = reviews;
            this.description = description;
        }
    }
}
