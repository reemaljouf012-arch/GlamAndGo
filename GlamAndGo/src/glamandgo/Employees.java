
package glamandgo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class Employees extends JFrame {

    private final String serviceName;
    private final String category;

    private final Color PINK = new Color(220, 145, 165);
    private final Color DARK_PINK = new Color(170, 90, 115);
    private final Color LIGHT_PINK = new Color(255, 245, 248);
    private final Color TEXT_COLOR = new Color(65, 55, 60);

    public Employees(String serviceName, String category) {
        this.serviceName = serviceName;
        this.category = category;

        setTitle("Choose Employee - Glam & Go");
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        buildPage();
    }

    private void buildPage() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(35, 40, 35, 40));

        JLabel title = new JLabel(
                "Choose Your Employee",
                SwingConstants.CENTER
        );
        title.setFont(new Font("Serif", Font.BOLD, 34));
        title.setForeground(DARK_PINK);

        JLabel subtitle = new JLabel(
                "Select an employee for " + serviceName,
                SwingConstants.CENTER
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setForeground(TEXT_COLOR);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Color.WHITE);

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(12));
        header.add(subtitle);
        header.add(Box.createVerticalStrut(35));

        mainPanel.add(header, BorderLayout.NORTH);

        JPanel employeesPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 25, 20)
        );
        employeesPanel.setBackground(Color.WHITE);

        ArrayList<Employee> employees = getEmployees();

        for (Employee employee : employees) {
            employeesPanel.add(createEmployeeCard(employee));
        }

        JScrollPane scrollPane = new JScrollPane(employeesPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createEmployeeCard(Employee employee) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(LIGHT_PINK);
        card.setOpaque(true);
        card.setBorder(new EmptyBorder(25, 20, 20, 20));

        card.setPreferredSize(new Dimension(320, 310));
        card.setMinimumSize(new Dimension(320, 310));
        card.setMaximumSize(new Dimension(320, 310));

        // Employee information
        JPanel detailsPanel = new JPanel(
                new GridBagLayout()
        );
        detailsPanel.setBackground(LIGHT_PINK);
        detailsPanel.setOpaque(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 10, 0);

        JLabel nameLabel = new JLabel(
                employee.name,
                SwingConstants.CENTER
        );
        nameLabel.setFont(new Font("Serif", Font.BOLD, 25));
        nameLabel.setForeground(DARK_PINK);

        gbc.gridy = 0;
        detailsPanel.add(nameLabel, gbc);

        JLabel specialtyLabel = new JLabel(
                employee.specialty,
                SwingConstants.CENTER
        );
        specialtyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        specialtyLabel.setForeground(TEXT_COLOR);

        gbc.gridy = 1;
        detailsPanel.add(specialtyLabel, gbc);

        JLabel ratingLabel = new JLabel(
                "★ " + employee.rating + "  (" +
                employee.reviews + " reviews)",
                SwingConstants.CENTER
        );
        ratingLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        ratingLabel.setForeground(DARK_PINK);

        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 15, 0);
        detailsPanel.add(ratingLabel, gbc);

        JLabel descriptionLabel = new JLabel(
                "<html><div style='text-align:center; width:230px;'>"
                + employee.description
                + "</div></html>",
                SwingConstants.CENTER
        );
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descriptionLabel.setForeground(TEXT_COLOR);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        detailsPanel.add(descriptionLabel, gbc);

        // Choose Employee button
        JButton chooseButton = new JButton("Choose Employee");
        chooseButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        chooseButton.setForeground(Color.WHITE);
        chooseButton.setBackground(DARK_PINK);
        chooseButton.setOpaque(true);
        chooseButton.setFocusPainted(false);
        chooseButton.setBorder(new EmptyBorder(12, 20, 12, 20));
        chooseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        chooseButton.addActionListener(e -> {
            new AppointmentBooking(
                    serviceName,
                    employee.name
            ).setVisible(true);

            dispose();
        });

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER)
        );
        buttonPanel.setBackground(LIGHT_PINK);
        buttonPanel.setOpaque(true);
        buttonPanel.add(chooseButton);

        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private ArrayList<Employee> getEmployees() {

        ArrayList<Employee> employees = new ArrayList<>();

        switch (category) {

            case "Hair Care":
                employees.add(new Employee(
                        "Sarah", "Hair Specialist",
                        4.9, 120,
                        "Professional hair styling and treatments."
                ));
                employees.add(new Employee(
                        "Nora", "Hair Stylist",
                        4.8, 95,
                        "Specialized in modern hair styles."
                ));
                break;

            case "Skincare":
                employees.add(new Employee(
                        "Lama", "Skincare Specialist",
                        4.9, 110,
                        "Experienced in facial and skincare treatments."
                ));
                employees.add(new Employee(
                        "Maya", "Beauty Specialist",
                        4.8, 87,
                        "Focused on skin care and hydration."
                ));
                break;

            case "Makeup":
                employees.add(new Employee(
                        "Reem", "Makeup Artist",
                        4.9, 135,
                        "Professional makeup for every occasion."
                ));
                employees.add(new Employee(
                        "Hala", "Makeup Artist",
                        4.8, 102,
                        "Specialized in elegant makeup looks."
                ));
                break;

            case "Nails":
                employees.add(new Employee(
                        "Dana", "Nail Technician",
                        4.9, 98,
                        "Professional manicure and nail art."
                ));
                employees.add(new Employee(
                        "Joud", "Nail Technician",
                        4.8, 90,
                        "Specialized in nail care and designs."
                ));
                break;
        }

        return employees;
    }

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
