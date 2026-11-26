
package glamandgo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class Employees extends JFrame {

    private final String serviceName;
    private final String category;

    private final Color PINK = new Color(190, 95, 125);
    private final Color DARK = new Color(65, 45, 55);
    private final Color LIGHT = new Color(255, 244, 248);

    private JPanel employeesPanel;

    private class Employee {
        String name;
        String specialty;
        double rating;
        int reviews;
        String description;

        Employee(String name, String specialty,
                 double rating, int reviews, String description) {
            this.name = name;
            this.specialty = specialty;
            this.rating = rating;
            this.reviews = reviews;
            this.description = description;
        }
    }

    public Employees(String serviceName, String category) {
        this.serviceName = serviceName;
        this.category = category;

        setTitle("Glam & Go - Choose Employee");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildPage();
    }

    private void buildPage() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(LIGHT);

        JPanel header = new JPanel();
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(25, 30, 25, 30));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Choose Your Specialist");
        title.setFont(new Font("Serif", Font.BOLD, 34));
        title.setForeground(DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Select an employee for " + serviceName
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setForeground(PINK);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(10));
        header.add(subtitle);

        employeesPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 20, 25)
        );
        employeesPanel.setBackground(LIGHT);
        employeesPanel.setBorder(new EmptyBorder(5, 20, 20, 20));

        loadEmployees();

        JScrollPane scrollPane = new JScrollPane(employeesPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(LIGHT);
        scrollPane.getViewport().setBackground(LIGHT);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        main.add(header, BorderLayout.NORTH);
        main.add(scrollPane, BorderLayout.CENTER);

        setContentPane(main);
    }

    private void loadEmployees() {
        ArrayList<Employee> employees = new ArrayList<>();

        if (category.equalsIgnoreCase("Hair Care")) {
            employees.add(new Employee(
                    "Sarah", "Hair Specialist", 4.9, 120,
                    "Experienced in hair styling and treatments."
            ));
            employees.add(new Employee(
                    "Nora", "Hair Stylist", 4.8, 95,
                    "Specializes in hair care and coloring."
            ));
        }

        if (category.equalsIgnoreCase("Skincare")) {
            employees.add(new Employee(
                    "Lama", "Skincare Specialist", 4.9, 110,
                    "Specializes in facial treatments."
            ));
            employees.add(new Employee(
                    "Maya", "Beauty Therapist", 4.7, 85,
                    "Experienced in deep cleansing."
            ));
        }

        if (category.equalsIgnoreCase("Makeup")) {
            employees.add(new Employee(
                    "Reem", "Makeup Artist", 5.0, 140,
                    "Specializes in classic and evening makeup."
            ));
            employees.add(new Employee(
                    "Hala", "Professional Makeup Artist", 4.8, 100,
                    "Creates elegant makeup looks."
            ));
        }

        if (category.equalsIgnoreCase("Nails")) {
            employees.add(new Employee(
                    "Dana", "Nail Technician", 4.9, 90,
                    "Specializes in manicure and nail art."
            ));
            employees.add(new Employee(
                    "Joud", "Nail Specialist", 4.8, 75,
                    "Experienced in nail care."
            ));
        }

        for (Employee employee : employees) {
            employeesPanel.add(createEmployeeCard(employee));
        }
    }

    private JPanel createEmployeeCard(Employee employee) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);

        card.setPreferredSize(new Dimension(320, 255));
        card.setMinimumSize(new Dimension(320, 255));
        card.setMaximumSize(new Dimension(320, 255));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(240, 220, 228), 1
                ),
                new EmptyBorder(20, 15, 20, 15)
        ));

        JLabel name = new JLabel(employee.name);
        name.setFont(new Font("Serif", Font.BOLD, 24));
        name.setForeground(DARK);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel specialty = new JLabel(employee.specialty);
        specialty.setFont(new Font("SansSerif", Font.PLAIN, 14));
        specialty.setForeground(PINK);
        specialty.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel rating = new JLabel(
                "★ " + employee.rating + "  (" +
                employee.reviews + " reviews)"
        );
        rating.setFont(new Font("SansSerif", Font.BOLD, 14));
        rating.setForeground(new Color(190, 140, 65));
        rating.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel description = new JLabel(
                "<html><div style='text-align:center;width:250px;'>"
                + employee.description + "</div></html>"
        );
        description.setFont(new Font("SansSerif", Font.PLAIN, 13));
        description.setForeground(DARK);
        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton chooseButton = new JButton("Choose Employee");
        chooseButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        chooseButton.setForeground(Color.WHITE);
        chooseButton.setBackground(PINK);
        chooseButton.setFocusPainted(false);
        chooseButton.setBorder(new EmptyBorder(12, 20, 12, 20));
        chooseButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        chooseButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "You selected " + employee.name
                            + "\nService: " + serviceName,
                    "Employee Selected",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        card.add(name);
        card.add(Box.createVerticalStrut(8));
        card.add(specialty);
        card.add(Box.createVerticalStrut(15));
        card.add(rating);
        card.add(Box.createVerticalStrut(15));
        card.add(description);
        card.add(Box.createVerticalStrut(20));
        card.add(chooseButton);

        return card;
    }
}
