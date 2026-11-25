package glamandgo;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Home extends JFrame {

    // Glam & Go color palette
    private final Color PINK_50  = Color.decode("#FFF3F7");
    private final Color PINK_100 = Color.decode("#FFE4EC");
    private final Color PINK_200 = Color.decode("#F9CEDB");
    private final Color PINK_300 = Color.decode("#EAB3C4");
    private final Color PINK_500 = Color.decode("#C17690");
    private final Color PINK_600 = Color.decode("#A05C74");
    private final Color PINK_700 = Color.decode("#83455C");
    private final Color PINK_800 = Color.decode("#693146");
    private final Color PINK_900 = Color.decode("#4F1D31");
    private final Color PINK_950 = Color.decode("#390C1F");

    private static final Font headingFont = loadFont("/fonts/Recia-Medium.otf", 26f);
    private static final Font textFont = loadFont("/fonts/Sentient-Regular.otf", 15f);

    private static Font loadFont(String path, float size) {
        try (InputStream stream = Home.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalStateException("Font file not found: " + path);
            }
            return Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(size);
        } catch (FontFormatException | IOException e) {
            throw new IllegalStateException("Cannot load font: " + path, e);
        }
    }

    public Home() {
        setTitle("Glam & Go");
        setSize(1100, 750);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Navigation bar
        JPanel navigation = new JPanel(new BorderLayout());
        navigation.setBackground(PINK_900);
        navigation.setBorder(new EmptyBorder(18, 35, 18, 35));

        JLabel logo = new JLabel("Glam & Go");
        logo.setFont(headingFont.deriveFont(27f));
        logo.setForeground(PINK_50);

        JPanel menu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 5));
        menu.setOpaque(false);

        String[] menuItems = {
            "Home", "Services", "Products", "Appointments", "My Account"
        };

        for (String item : menuItems) {
            JButton button = new JButton(item);
            button.setForeground(Color.WHITE);
            button.setFont(textFont);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            menu.add(button);
        }

        navigation.add(logo, BorderLayout.WEST);
        navigation.add(menu, BorderLayout.EAST);

        // Main scrollable content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);

        // Section 1: Hero
        JPanel hero = new JPanel(new GridLayout(1, 2, 25, 10));
        hero.setBackground(PINK_50);
        hero.setBorder(new EmptyBorder(65, 55, 65, 55));
        hero.setPreferredSize(new Dimension(1000, 330));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 330));

        JPanel heroText = new JPanel();
        heroText.setLayout(new BoxLayout(heroText, BoxLayout.Y_AXIS));
        heroText.setOpaque(false);

        JLabel welcome = new JLabel("Welcome to Glam & Go");
        welcome.setFont(headingFont);
        welcome.setForeground(PINK_900);

        JLabel slogan = new JLabel("<html>Your Beauty,<br>Your Way</html>");
        slogan.setFont(headingFont.deriveFont(36f));
        slogan.setForeground(PINK_600);

        JLabel description = new JLabel(
                "<html>Discover your beauty with our professional "
                + "salon services and carefully selected products.</html>");
        description.setFont(textFont);
        description.setForeground(PINK_800);

        JButton explore = createButton("Explore Services");

        heroText.add(Box.createVerticalGlue());
        heroText.add(welcome);
        heroText.add(Box.createVerticalStrut(15));
        heroText.add(slogan);
        heroText.add(Box.createVerticalStrut(15));
        heroText.add(description);
        heroText.add(Box.createVerticalStrut(20));
        heroText.add(explore);
        heroText.add(Box.createVerticalGlue());

        JPanel heroPlaceholder = createPlaceholder("Your Beauty Starts Here");
        hero.add(heroText);
        hero.add(heroPlaceholder);
        content.add(hero);

        // Section 2: Beauty experience
        content.add(sectionTitle(
                "The Glam & Go Experience",
                "Everything you need for your beauty journey"));

        JPanel experience = new JPanel(new GridLayout(1, 3, 20, 15));
        experience.setBackground(Color.WHITE);
        experience.setBorder(new EmptyBorder(15, 40, 35, 40));
        experience.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        experience.add(infoCard("Beauty Services", "Professional salon treatments"));
        experience.add(infoCard("Expert Team", "Choose your favorite employee"));
        experience.add(infoCard("Beauty Products", "Shop your favorite products"));

        content.add(experience);

        // Section 3: Categories
        content.add(sectionTitle(
                "Explore Our Categories",
                "Find the perfect service for you"));

        JPanel categories = new JPanel(new GridLayout(1, 3, 20, 15));
        categories.setBackground(Color.WHITE);
        categories.setBorder(new EmptyBorder(15, 40, 35, 40));
        categories.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        categories.add(categoryCard("Hair Care"));
        categories.add(categoryCard("Skin Care"));
        categories.add(categoryCard("Nail Care"));

        content.add(categories);

        // Section 4: Appointment booking
        JPanel booking = new JPanel(new GridLayout(1, 2, 25, 10));
        booking.setBackground(PINK_100);
        booking.setBorder(new EmptyBorder(35, 45, 35, 45));
        booking.setMaximumSize(new Dimension(Integer.MAX_VALUE, 290));

        JPanel bookingText = new JPanel();
        bookingText.setOpaque(false);
        bookingText.setLayout(new BoxLayout(bookingText, BoxLayout.Y_AXIS));

        JLabel bookingTitle = new JLabel("<html>Your Beauty<br>Appointment</html>");
        bookingTitle.setFont(headingFont);
        bookingTitle.setForeground(PINK_900);

        JLabel bookingDescription = new JLabel(
                "<html>Ready for a beauty refresh?<br><br>"
                + "Choose your service, select your employee, "
                + "and book your preferred date and time.</html>");
        bookingDescription.setFont(textFont);
        bookingDescription.setForeground(PINK_800);

        JButton bookButton = createButton("Book Your Appointment");

        bookingText.add(Box.createVerticalGlue());
        bookingText.add(bookingTitle);
        bookingText.add(Box.createVerticalStrut(15));
        bookingText.add(bookingDescription);
        bookingText.add(Box.createVerticalStrut(20));
        bookingText.add(bookButton);
        bookingText.add(Box.createVerticalGlue());

        booking.add(bookingText);
        booking.add(createPlaceholder("Appointment Booking"));

        content.add(Box.createVerticalStrut(25));
        content.add(booking);

        // Section 5: Products
        content.add(sectionTitle(
                "Our Beauty Collection",
                "Discover our carefully selected products"));

        JPanel products = new JPanel(new GridLayout(1, 3, 20, 15));
        products.setBackground(Color.WHITE);
        products.setBorder(new EmptyBorder(15, 40, 35, 40));
        products.setMaximumSize(new Dimension(Integer.MAX_VALUE, 230));

        products.add(productCard("Beauty Product 1", "48 SAR"));
        products.add(productCard("Beauty Product 2", "62 SAR"));
        products.add(productCard("Beauty Product 3", "56 SAR"));

        content.add(products);

        // Section 6: Customer reviews
        content.add(sectionTitle(
                "Loved by Our Customers",
                "A little love from our beautiful customers"));

        JPanel reviews = new JPanel(new GridLayout(1, 3, 20, 15));
        reviews.setBackground(PINK_50);
        reviews.setBorder(new EmptyBorder(25, 40, 35, 40));
        reviews.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        reviews.add(reviewCard("Amazing service and lovely staff!"));
        reviews.add(reviewCard("I had a wonderful beauty experience."));
        reviews.add(reviewCard("Beautiful products and great quality!"));

        content.add(reviews);

        // Footer
        JPanel footer = new JPanel();
        footer.setBackground(PINK_900);
        footer.setBorder(new EmptyBorder(25, 10, 25, 10));

        JLabel footerText = new JLabel("Glam & Go | Your Beauty, Your Way");
        footerText.setForeground(Color.WHITE);
        footerText.setFont(textFont);

        footer.add(footerText);
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        content.add(footer);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(navigation, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    // Section heading
    private JPanel sectionTitle(String title, String subtitle) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(35, 40, 10, 40));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(headingFont);
        titleLabel.setForeground(PINK_900);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(textFont);
        subtitleLabel.setForeground(PINK_600);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(subtitleLabel);

        return panel;
    }

    // Information card
    private JPanel infoCard(String title, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(PINK_50);
        card.setBorder(new EmptyBorder(25, 18, 25, 18));

        JLabel name = new JLabel(title);
        name.setFont(headingFont.deriveFont(19f));
        name.setForeground(PINK_900);

        JLabel desc = new JLabel(description);
        desc.setFont(textFont);
        desc.setForeground(PINK_700);

        card.add(name);
        card.add(Box.createVerticalStrut(12));
        card.add(desc);

        return card;
    }

    // Category card
    private JPanel categoryCard(String name) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(PINK_100);
        card.setBorder(new EmptyBorder(40, 15, 40, 15));

        JLabel label = new JLabel(name);
        label.setFont(headingFont.deriveFont(22f));
        label.setForeground(PINK_900);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(label, BorderLayout.CENTER);

        return card;
    }

    // Product card
    private JPanel productCard(String name, String price) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(PINK_200));

        JPanel placeholder = createPlaceholder("Product");

        JLabel productName = new JLabel(name);
        productName.setFont(headingFont.deriveFont(18f));
        productName.setForeground(PINK_900);
        productName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel productPrice = new JLabel(price);
        productPrice.setFont(textFont);
        productPrice.setForeground(PINK_700);
        productPrice.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(placeholder);
        card.add(Box.createVerticalStrut(10));
        card.add(productName);
        card.add(Box.createVerticalStrut(5));
        card.add(productPrice);

        return card;
    }

    // Review card
    private JPanel reviewCard(String review) {
        JPanel card = new JPanel(new BorderLayout(5, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel stars = new JLabel("★★★★★");
        stars.setFont(new Font("Serif", Font.BOLD, 20));
        stars.setForeground(PINK_500);

        JLabel reviewText = new JLabel("<html>" + review + "</html>");
        reviewText.setFont(textFont);
        reviewText.setForeground(PINK_900);

        card.add(stars, BorderLayout.NORTH);
        card.add(reviewText, BorderLayout.CENTER);

        return card;
    }

    // Temporary image placeholder
    private JPanel createPlaceholder(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(PINK_200);
        panel.setBorder(BorderFactory.createLineBorder(PINK_300));
        panel.setPreferredSize(new Dimension(350, 180));

        JLabel label = new JLabel(text);
        label.setFont(headingFont);
        label.setForeground(PINK_800);

        panel.add(label);

        return panel;
    }

    // Styled button
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(PINK_600);
        button.setForeground(Color.WHITE);
        button.setFont(textFont);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Home().setVisible(true);
        });
    }
}