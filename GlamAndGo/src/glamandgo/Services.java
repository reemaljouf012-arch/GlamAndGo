
package glamandgo;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static glamandgo.Theme.*;

public class Services extends JFrame {

    private static class SalonService {
        String name;
        String category;
        String description;
        double price;

        SalonService(String name, String category,
                     String description, double price) {
            this.name = name;
            this.category = category;
            this.description = description;
            this.price = price;
        }
    }

    private final List<SalonService> allServices = new ArrayList<>();
    private final JPanel cardsPanel = new JPanel(new GridLayout(0, 3, 22, 25));
    private final JPanel categoriesPanel =
            new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));

    private String selectedCategory = "All";

    public Services() {
        setTitle("Glam & Go | Services");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 820);
        setMinimumSize(new Dimension(1100, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        loadServices();

        add(navbar(this::navigate), BorderLayout.NORTH);

        PageBody body = new PageBody();
        body.add(buildHero());
        body.add(buildCategories());
        body.add(buildServicesSection());
        body.add(footer(this::navigate));

        JScrollPane scroll = new JScrollPane(body,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(24);

        add(scroll, BorderLayout.CENTER);

        refreshServices();
    }

    private void loadServices() {
        allServices.add(new SalonService(
                "Hair Styling", "Hair Care",
                "Professional styling for your perfect look.", 120));

        allServices.add(new SalonService(
                "Hair Treatment", "Hair Care",
                "Nourishing treatments for healthy hair.", 180));

        allServices.add(new SalonService(
                "Hair Coloring", "Hair Care",
                "Refresh your look with beautiful colors.", 250));

        allServices.add(new SalonService(
                "Facial Treatment", "Skincare",
                "A relaxing treatment for glowing skin.", 150));

        allServices.add(new SalonService(
                "Deep Cleansing", "Skincare",
                "Deep cleansing for fresh and clean skin.", 130));

        allServices.add(new SalonService(
                "Skin Hydration", "Skincare",
                "Restore moisture and softness to your skin.", 170));

        allServices.add(new SalonService(
                "Classic Makeup", "Makeup",
                "Elegant makeup for your special occasions.", 200));

        allServices.add(new SalonService(
                "Evening Makeup", "Makeup",
                "A glamorous look for unforgettable nights.", 280));

        allServices.add(new SalonService(
                "Manicure", "Nails",
                "Beautifully shaped and polished nails.", 90));

        allServices.add(new SalonService(
                "Pedicure", "Nails",
                "Relaxing care for soft and beautiful feet.", 110));

        allServices.add(new SalonService(
                "Nail Art", "Nails",
                "Creative nail designs made just for you.", 140));
    }

    private JComponent buildHero() {
        JPanel hero = new JPanel();
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));
        hero.setBackground(PINK_50);
        hero.setBorder(new EmptyBorder(65, 20, 65, 20));

        JLabel title = label(
                "Our Beauty Services",
                heading(48),
                PINK_950
        );

        JLabel subtitle = label(
                "Discover the perfect service for your beauty journey.",
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

    private JComponent buildCategories() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(new EmptyBorder(30, 20, 20, 20));

        JLabel title = label(
                "Explore Our Services",
                heading(30),
                PINK_900
        );
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        categoriesPanel.setOpaque(false);

        String[] categories = {
            "All", "Hair Care", "Skincare", "Makeup", "Nails"
        };

        for (String category : categories) {
            PillButton button = new PillButton(
                    category, 15, 20, 9
            );

            styleCategoryButton(button, category);

            button.addActionListener(e -> {
                selectedCategory = category;
                updateCategoryButtons();
                refreshServices();
            });

            categoriesPanel.add(button);
        }

        section.add(title);
        section.add(Box.createVerticalStrut(20));
        section.add(categoriesPanel);

        return section;
    }

    private void styleCategoryButton(PillButton button, String category) {
        boolean selected = category.equals(selectedCategory);

        if (selected) {
            button.style(PINK_800, Color.WHITE, null, PINK_900);
        } else {
            button.style(Color.WHITE, PINK_800, PINK_300, PINK_50);
        }
    }

    private void updateCategoryButtons() {
        for (Component component : categoriesPanel.getComponents()) {
            if (component instanceof PillButton) {
                PillButton button = (PillButton) component;
                styleCategoryButton(button, button.getText());
            }
        }
    }

    private JComponent buildServicesSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        section.setBorder(new EmptyBorder(20, 55, 55, 55));

        cardsPanel.setOpaque(false);

        section.add(cardsPanel, BorderLayout.CENTER);

        return section;
    }

    private void refreshServices() {
        cardsPanel.removeAll();

        int count = 0;

        for (SalonService service : allServices) {
            if (!selectedCategory.equals("All")
                    && !selectedCategory.equals(service.category)) {
                continue;
            }

            cardsPanel.add(buildServiceCard(service));
            count++;
        }

        while (count < 3) {
            JPanel empty = new JPanel();
            empty.setOpaque(false);
            cardsPanel.add(empty);
            count++;
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel buildServiceCard(SalonService service) {
        RoundedPanel card = new RoundedPanel(22, PINK_50, true);

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(22, 20, 22, 20));

        JLabel category = label(
                service.category.toUpperCase(),
                text(12),
                PINK_500
        );

        JLabel name = label(
                service.name,
                heading(23),
                PINK_950
        );

        JTextArea description = new JTextArea(service.description);
        description.setFont(text(15));
        description.setForeground(PINK_800);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setOpaque(false);
        description.setRows(2);

        JLabel price = new JLabel(
                String.format(java.util.Locale.US, "SAR %.2f", service.price)
        );
        price.setFont(new Font("Serif", Font.PLAIN, 22));
        price.setForeground(PINK_800);

        PillButton bookButton = new PillButton(
                "Book Now", 15, 20, 9
        );

        bookButton.style(PINK_800, Color.WHITE, null, PINK_900);

        bookButton.addActionListener(e -> {
            new Employees(service.name, service.category).setVisible(true);
            dispose();
        });

        for (JComponent component : new JComponent[]{
                category, name, description, price, bookButton
        }) {
            component.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        card.add(category);
        card.add(Box.createVerticalStrut(12));
        card.add(name);
        card.add(Box.createVerticalStrut(10));
        card.add(description);
        card.add(Box.createVerticalStrut(18));
        card.add(price);
        card.add(Box.createVerticalStrut(15));
        card.add(bookButton);

        return card;
    }

    private void navigate(String page) {
        switch (page) {
            case "Home":
                new Home().setVisible(true);
                dispose();
                break;

            case "Services":
                break;

            case "Products":
                new Products().setVisible(true);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new Services().setVisible(true)
        );
    }
}
