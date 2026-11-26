package glamandgo;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import static glamandgo.Theme.*;

/**
 * Glam & Go - Home page.
 *
 * Uses Theme.java for colors, fonts, buttons, navbar and footer.
 * Fonts  : /fonts/Recia-Medium.otf (headings) + /fonts/Sentient-Regular.otf (text)
 * Images : put them in a source package called "images" (same way as "fonts").
 *          If an image is missing, a pink placeholder is drawn, so the app always runs.
 *
 *   /images/hero.jpg        hero background
 *   /images/cat_hair.jpg    /images/cat_nails.jpg    /images/cat_skincare.jpg    /images/cat_makeup.jpg
 *   /images/booking.jpg     booking banner (right side)
 *   /images/product1.jpg ... product8.jpg   (white or transparent background works best)
 */
public class Home extends JFrame {

    // ===================== Page-specific components =====================
    private static class FeatureIcon extends JComponent {
        FeatureIcon() { setPreferredSize(new Dimension(60, 60)); }

        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = g2(gr);
            g.setColor(PINK_200);
            g.fill(new Ellipse2D.Float(0, 0, 59, 59));
            g.setColor(PINK_800);
            g.fill(starShape(29.5, 29.5, 16, 5, 4));
            g.dispose();
        }
    }

    private static class CheckBadge extends JComponent {
        CheckBadge() { setPreferredSize(new Dimension(30, 30)); }

        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = g2(gr);
            g.setColor(PINK_200);
            g.fill(new Ellipse2D.Float(0, 0, 29, 29));
            g.setColor(PINK_800);
            g.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            Path2D p = new Path2D.Float();
            p.moveTo(8, 15); p.lineTo(13, 20); p.lineTo(21, 10);
            g.draw(p);
            g.dispose();
        }
    }

    private static class Avatar extends JComponent {
        private final String letter;

        Avatar(String name) {
            this.letter = name.substring(0, 1).toUpperCase();
            setPreferredSize(new Dimension(52, 52));
        }

        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = g2(gr);
            g.setColor(PINK_300);
            g.fill(new Ellipse2D.Float(0, 0, 51, 51));
            g.setColor(PINK_900);
            g.setFont(heading(24f));
            FontMetrics fm = g.getFontMetrics();
            g.drawString(letter, (52 - fm.stringWidth(letter)) / 2, (52 - fm.getHeight()) / 2 + fm.getAscent());
            g.dispose();
        }
    }

    /** Hero: full-width background image (washed with soft pink) or a pink gradient. */
    private static class HeroPanel extends FullWidth {
        private final String path;

        HeroPanel(String path, int height) {
            this.path = path;
            setPreferredSize(new Dimension(100, height));
        }

        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = g2(gr);
            int w = getWidth(), h = getHeight();
            BufferedImage img = loadImage(path);
            if (img != null) {
                drawCover(g, img, 0, 0, w, h);
                g.setColor(new Color(255, 243, 247, 175));
                g.fillRect(0, 0, w, h);
            } else {
                g.setPaint(new GradientPaint(0, 0, PINK_50, w, h, PINK_200));
                g.fillRect(0, 0, w, h);
            }
            g.dispose();
        }
    }

    /** Image on the right side of the booking card (only the right corners are rounded). */
    private static class PhotoPanel extends JComponent {
        private final String path;
        private final int arc;

        PhotoPanel(String path, int arc) {
            this.path = path;
            this.arc = arc;
        }

        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = g2(gr);
            int w = getWidth(), h = getHeight();
            g.clip(new RoundRectangle2D.Float(-arc, 0, w + arc, h, arc, arc));
            BufferedImage img = loadImage(path);
            if (img != null) drawCover(g, img, 0, 0, w, h);
            else placeholder(g, w, h, path);
            g.dispose();
        }
    }

    /** Arch-shaped category image (like the AURA design). */
    private static class ArchImage extends JComponent {
        private final String path;
        private boolean hover;

        ArchImage(String path, Runnable onClick) {
            this.path = path;
            setPreferredSize(new Dimension(260, 340));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 340));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
                @Override public void mouseClicked(MouseEvent e) { onClick.run(); }
            });
        }

        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = g2(gr);
            int w = getWidth() - 2, h = getHeight() - 2;
            g.translate(1, 1);
            float r = w / 2f;
            Path2D arch = new Path2D.Float();
            arch.moveTo(0, h);
            arch.lineTo(0, r);
            arch.append(new Arc2D.Float(0, 0, w, w, 180, -180, Arc2D.OPEN), true);
            arch.lineTo(w, h);
            arch.closePath();

            Graphics2D gi = (Graphics2D) g.create();
            gi.clip(arch);
            BufferedImage img = loadImage(path);
            if (img != null) drawCover(gi, img, 0, 0, w, h);
            else placeholder(gi, w, h, path);
            gi.dispose();

            g.setColor(hover ? PINK_500 : PINK_200);
            g.setStroke(new BasicStroke(hover ? 3f : 1.5f));
            g.draw(arch);
            g.dispose();
        }
    }

    // ===================== Frame =====================
    private final List<PillButton> chips = new ArrayList<>();
    private JPanel productsGrid;

    public Home() {
        setTitle("Glam & Go");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 820);
        setMinimumSize(new Dimension(1100, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(navbar(this::navigate), BorderLayout.NORTH);

        PageBody body = new PageBody();
        body.add(buildHero());
        body.add(buildFeatures());
        body.add(buildCategories());
        body.add(buildBooking());
        body.add(buildProducts());
        body.add(buildReviews());
        body.add(footer(this::navigate));

        JScrollPane scroll = new JScrollPane(body,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Central place for navigation. Replace the TODO lines with your own windows, e.g.:
     *   case "Services": new Services().setVisible(true); dispose(); break;
     */
    private void navigate(String page) {
        switch (page) {
            case "Home":
                break;
            // case "Services":     new Services().setVisible(true);    dispose(); break;
            case "Products":
                new Products().setVisible(true);
                dispose();
                break;
            // case "Appointments": new Appointments().setVisible(true); dispose(); break;
            // case "Cart":         new Cart().setVisible(true);        dispose(); break;
            // case "My Account":   new MyAccount().setVisible(true);   dispose(); break;
            default:
                JOptionPane.showMessageDialog(this, "\"" + page + "\" page is not connected yet.",
                        "Glam & Go", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ===================== Hero =====================
    private JComponent buildHero() {
        HeroPanel hero = new HeroPanel("/images/hero.jpg", 560);
        hero.setLayout(new GridBagLayout());

        JPanel box = vbox();
        JLabel eyebrow = label("Welcome to Glam & Go", text(18f), PINK_800);
        JLabel title = label("Your Beauty, Your Way", heading(62f), PINK_900);
        JLabel sub = htmlLabel("Discover your beauty with our professional salon services and carefully selected products.",
                620, "center", text(20f), PINK_800);

        PillButton book = new PillButton("Book Appointment", 17f, 32, 13)
                .style(PINK_800, Color.WHITE, null, PINK_950);
        book.addActionListener(e -> navigate("Appointments"));
        PillButton shop = new PillButton("Shop Products", 17f, 32, 13)
                .style(PINK_50, PINK_800, PINK_800, PINK_100);
        shop.addActionListener(e -> navigate("Products"));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        buttons.setOpaque(false);
        buttons.add(book);
        buttons.add(shop);

        for (JComponent c : new JComponent[]{eyebrow, title, sub, buttons}) c.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(eyebrow);
        box.add(Box.createVerticalStrut(12));
        box.add(title);
        box.add(Box.createVerticalStrut(18));
        box.add(sub);
        box.add(Box.createVerticalStrut(34));
        box.add(fixedHeight(buttons));
        hero.add(box);
        return hero;
    }

    // ===================== Features strip =====================
    private JComponent buildFeatures() {
        String[] items = {"Expert Stylists", "Premium Products", "Easy Booking", "Top Rated Team", "Hygiene First", "Secure Payment"};
        JPanel row = new JPanel(new GridLayout(1, items.length, 10, 0));
        row.setOpaque(false);
        for (String item : items) {
            JPanel cell = vbox();
            FeatureIcon icon = new FeatureIcon();
            icon.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel l = label(item, text(15f), PINK_800);
            l.setAlignmentX(Component.CENTER_ALIGNMENT);
            cell.add(fixedSize(icon));
            cell.add(Box.createVerticalStrut(10));
            cell.add(l);
            row.add(cell);
        }
        return new Section(PINK_100, 32, 1200, row);
    }

    // ===================== Categories =====================
    private JComponent buildCategories() {
        JPanel content = vbox();
        content.add(sectionTitle("Explore Our Categories", "Find the perfect service for your unique style.", PINK_900, PINK_800));
        content.add(Box.createVerticalStrut(44));

        JPanel grid = new JPanel(new GridLayout(1, 4, 32, 0));
        grid.setOpaque(false);
        grid.add(categoryCard("Hair", "/images/cat_hair.jpg"));
        grid.add(categoryCard("Nails", "/images/cat_nails.jpg"));
        grid.add(categoryCard("Skincare", "/images/cat_skincare.jpg"));
        grid.add(categoryCard("Makeup", "/images/cat_makeup.jpg"));
        content.add(grid);
        return new Section(Color.WHITE, 80, 1200, content);
    }

    private JPanel categoryCard(String name, String imagePath) {
        JPanel card = vbox();
        ArchImage arch = new ArchImage(imagePath, () -> navigate("Services"));
        JLabel l = label(name, heading(26f), PINK_900);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(arch);
        card.add(Box.createVerticalStrut(16));
        card.add(l);
        return card;
    }

    // ===================== Booking banner =====================
    private JComponent buildBooking() {
        RoundedPanel card = new RoundedPanel(32, Color.WHITE, true);
        card.setLayout(new GridLayout(1, 2));
        card.setBorder(new EmptyBorder(RoundedPanel.SH, RoundedPanel.SH, RoundedPanel.SH, RoundedPanel.SH));
        card.setPreferredSize(new Dimension(1100, 456));

        JPanel left = vbox();
        left.setBorder(new EmptyBorder(36, 46, 36, 30));

        JLabel title = label("Book Your Appointment", heading(36f), PINK_800);
        JLabel para = htmlLabel("Ready for a fresh new look? Choose your service, pick your favorite specialist, "
                + "and reserve the perfect time for your beauty session.", 370, "left", text(17f), PINK_900);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        para.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(title);
        left.add(Box.createVerticalStrut(16));
        left.add(para);
        left.add(Box.createVerticalStrut(22));
        for (String s : new String[]{
                "One-on-one consultation with our experts",
                "Choose your favorite specialist",
                "Easy online booking and confirmation"}) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            row.setOpaque(false);
            row.add(new CheckBadge());
            JLabel rowText = label(s, text(16f), PINK_800);
            rowText.setBorder(new EmptyBorder(0, 12, 0, 0));
            row.add(rowText);
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            left.add(fixedHeight(row));
            left.add(Box.createVerticalStrut(8));
        }
        left.add(Box.createVerticalStrut(14));
        PillButton cta = new PillButton("Book Your Spot Now", 17f, 30, 13)
                .style(PINK_800, Color.WHITE, null, PINK_950);
        cta.addActionListener(e -> navigate("Appointments"));
        cta.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(cta);
        left.add(Box.createVerticalGlue());

        card.add(left);
        card.add(new PhotoPanel("/images/booking.jpg", 32));
        return new Section(PINK_50, 80, 1100, card);
    }

    // ===================== Products =====================
    private JComponent buildProducts() {
        JPanel content = vbox();
        content.add(sectionTitle("Featured Products", "Salon favorites you can take home.", PINK_900, PINK_800));
        content.add(Box.createVerticalStrut(28));

        JPanel chipRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        chipRow.setOpaque(false);
        for (String cat : new String[]{"All", "Hair Care", "Skincare", "Makeup", "Nails"}) {
            PillButton chip = new PillButton(cat, 16f, 24, 9);
            chips.add(chip);
            chip.addActionListener(e -> {
                selectChip(chip);
                showProducts(cat);
            });
            chipRow.add(chip);
        }
        selectChip(chips.get(0));
        content.add(fixedHeight(chipRow));
        content.add(Box.createVerticalStrut(34));

        productsGrid = new JPanel(new GridLayout(1, 4, 28, 0));
        productsGrid.setOpaque(false);
        showProducts("All");
        content.add(productsGrid);

        content.add(Box.createVerticalStrut(30));
        JLabel viewAll = label("View all products", heading(18f), PINK_800);
        viewAll.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewAll.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        viewAll.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { viewAll.setForeground(PINK_500); }
            @Override public void mouseExited(MouseEvent e)  { viewAll.setForeground(PINK_800); }
            @Override public void mouseClicked(MouseEvent e) { navigate("Products"); }
        });
        content.add(viewAll);
        return new Section(Color.WHITE, 80, 1200, content);
    }

    private void selectChip(PillButton active) {
        for (PillButton c : chips) {
            if (c == active) c.style(PINK_800, Color.WHITE, null, PINK_800);
            else c.style(null, PINK_800, PINK_300, PINK_100);
        }
    }

    private void showProducts(String category) {
        productsGrid.removeAll();
        int count = 0;
        for (Product p : PRODUCTS) {
            if (!category.equals("All") && !p.category.equals(category)) continue;
            if (count == 4) break;
            productsGrid.add(productCard(p, () -> navigate("Product Details")));
            count++;
        }
        for (int i = count; i < 4; i++) {
            JPanel filler = new JPanel();
            filler.setOpaque(false);
            productsGrid.add(filler);
        }
        productsGrid.revalidate();
        productsGrid.repaint();
    }

    // ===================== Reviews =====================
    private JComponent buildReviews() {
        JPanel content = vbox();
        content.add(sectionTitle("Loved by Our Clients", "Real words from the women who trust Glam & Go.", PINK_900, PINK_800));
        content.add(Box.createVerticalStrut(44));

        JPanel grid = new JPanel(new GridLayout(1, 3, 28, 0));
        grid.setOpaque(false);
        grid.add(reviewCard("Noura", 5, "The staff was so professional and my hair has never looked better. Booking was quick and easy!"));
        grid.add(reviewCard("Lama", 5, "I booked my facial through the app and picked my favorite specialist. Beautiful salon and amazing service."));
        grid.add(reviewCard("Reem", 4, "Loved the products I bought after my appointment. The quality is great and ordering was smooth."));
        content.add(grid);
        return new Section(PINK_50, 80, 1200, content);
    }

    private JPanel reviewCard(String name, int rating, String comment) {
        RoundedPanel card = new RoundedPanel(28, Color.WHITE, true);
        card.setLayout(new BorderLayout(0, 16));
        int s = RoundedPanel.SH;
        card.setBorder(new EmptyBorder(s + 24, s + 24, s + 24, s + 24));

        JPanel header = new JPanel(new BorderLayout(14, 0));
        header.setOpaque(false);
        header.add(new Avatar(name), BorderLayout.WEST);
        header.add(label(name, heading(20f), PINK_900), BorderLayout.CENTER);
        JPanel starWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 18));
        starWrap.setOpaque(false);
        starWrap.add(new Stars(rating, 14));
        header.add(starWrap, BorderLayout.EAST);

        card.add(header, BorderLayout.NORTH);
        card.add(htmlLabel(comment, 250, "left", text(16f), PINK_900), BorderLayout.CENTER);
        return card;
    }

    // ===================== Main (for testing this page alone) =====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Home().setVisible(true));
    }
}