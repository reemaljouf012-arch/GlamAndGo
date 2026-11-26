package glamandgo;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static glamandgo.Theme.*;

public class ProductDetails extends JDialog {

    private static final int SIMILAR_LIMIT = 3;
    private static final Map<Product, Integer> cart = new LinkedHashMap<>();

    private Product product;
    private final JScrollPane scroll = new JScrollPane();
    private final JLabel cartStatus = label(" ", text(15), PINK_800);
    private final PillButton addButton =
            new PillButton("Add to cart", 18, 26, 10);

    public ProductDetails(JFrame owner, Product product) {
        super(owner, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        setContentPane(scroll);

        addButton.style(PINK_800, Color.WHITE, null, PINK_900);
        addButton.addActionListener(e -> addToCart());

        showProduct(product);

        Dimension screen = getToolkit().getScreenSize();
        setSize(Math.min(1180, screen.width - 50),
                Math.min(820, screen.height - 65));
        setLocationRelativeTo(owner);
    }

    private void showProduct(Product selected) {
        product = selected;
        setTitle("Glam & Go | " + product.name);

        PageBody body = new PageBody();
        body.setBackground(Color.WHITE);

        FullWidth page = new FullWidth();
        page.setLayout(new BorderLayout(0, 32));
        page.setBackground(Color.WHITE);
        page.setBorder(new EmptyBorder(20, 32, 28, 32));

        page.add(buildHeader(), BorderLayout.NORTH);

        JPanel hero = transparent(new GridLayout(1, 2, 28, 0));
        hero.add(buildGallery());
        hero.add(buildInformation());
        page.add(hero, BorderLayout.CENTER);

        JPanel similar = buildSimilarProducts();
        if (similar != null) {
            page.add(similar, BorderLayout.SOUTH);
        }

        body.add(page);
        scroll.setViewportView(body);
        updateCartButton();

        SwingUtilities.invokeLater(() ->
                scroll.getViewport().setViewPosition(new Point(0, 0)));
    }

    private JPanel buildHeader() {
        JPanel header = transparent(new BorderLayout());

        PillButton back = new PillButton("Back to products", 15, 16, 6);
        back.style(PINK_50, PINK_800, PINK_200, PINK_100);
        back.addActionListener(e -> dispose());

        header.add(back, BorderLayout.WEST);
        header.add(label("Glam & Go", heading(24), PINK_900),
                BorderLayout.EAST);

        return header;
    }

    private JPanel buildGallery() {
        JPanel gallery = transparent(new BorderLayout(9, 0));
        gallery.add(new ProductPhoto(product, 0), BorderLayout.CENTER);

        JPanel sidePhotos = transparent(new GridLayout(2, 1, 0, 9));
        sidePhotos.setPreferredSize(new Dimension(135, 350));
        sidePhotos.add(new ProductPhoto(product, 1));
        sidePhotos.add(new ProductPhoto(product, 2));

        gallery.add(sidePhotos, BorderLayout.EAST);
        return gallery;
    }

    private JPanel buildInformation() {
        JPanel info = vbox();

        addLeft(info, label(product.name, heading(34), PINK_950), 5);
        addLeft(info, label(product.desc, text(20), PINK_800), 20);

        JPanel priceRow = transparent(new BorderLayout(12, 0));
        String price = String.format(Locale.US, "SAR %.2f", product.price);

        priceRow.add(label(price, heading(28), PINK_800), BorderLayout.WEST);

        JPanel stars = transparent(new FlowLayout(FlowLayout.RIGHT, 0, 7));
        stars.add(new Stars(product.rating, 19));
        stars.setToolTipText(product.rating + " out of 5");
        priceRow.add(stars, BorderLayout.EAST);

        addLeft(info, priceRow, 18);

        String description;
        if ("Silk Repair Serum".equals(product.name)) {
            description = "A lightweight finishing serum for smooth, shiny hair. "
                    + "Use it as the final touch in your styling routine "
                    + "for a soft, glossy finish.";
        } else {
            description = product.desc + ". Part of the Glam & Go "
                    + product.category.toLowerCase(Locale.ROOT)
                    + " collection.";
        }

        JTextArea descriptionArea = new JTextArea(description, 3, 27);
        descriptionArea.setFont(text(18));
        descriptionArea.setForeground(PINK_900);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        addLeft(info, descriptionArea, 18);
        addLeft(info, addButton, 5);
        addLeft(info, cartStatus, 16);

        JPanel facts = transparent(new GridLayout(1, 3, 8, 0));
        facts.add(factCard("Collection", product.category));
        facts.add(factCard("Availability",
                product.quantity > 0 ? "In stock" : "Sold out"));
        facts.add(factCard("Stock", product.quantity + " units"));

        addLeft(info, facts, 0);
        return info;
    }

    private JPanel buildSimilarProducts() {
        JPanel cards = transparent(new GridLayout(1, SIMILAR_LIMIT, 16, 0));
        int count = 0;

        for (Product other : PRODUCTS) {
            if (other == product || !product.category.equals(other.category)) {
                continue;
            }

            cards.add(productCard(other, () -> showProduct(other)));
            count++;

            if (count == SIMILAR_LIMIT) {
                break;
            }
        }

        if (count == 0) {
            return null;
        }

        // Keep card widths consistent if the category has only one or two matches.
        while (cards.getComponentCount() < SIMILAR_LIMIT) {
            cards.add(transparent(new BorderLayout()));
        }

        JPanel section = transparent(new BorderLayout(0, 14));
        section.add(label("Similar Products", heading(25), PINK_900),
                BorderLayout.NORTH);
        section.add(cards, BorderLayout.CENTER);

        return section;
    }

    private void addToCart() {
        int count = cart.getOrDefault(product, 0);

        if (count < product.quantity) {
            cart.put(product, count + 1);
        }

        updateCartButton();
    }

    private void updateCartButton() {
        int count = cart.getOrDefault(product, 0);

        if (product.quantity <= 0) {
            addButton.setEnabled(false);
            addButton.setText("Sold out");
        } else if (count >= product.quantity) {
            addButton.setEnabled(false);
            addButton.setText("Stock limit reached");
        } else {
            addButton.setEnabled(true);
            addButton.setText("Add to cart");
        }

        if (count == 0) {
            cartStatus.setText(" ");
        } else {
            String unit = count == 1 ? " unit" : " units";
            cartStatus.setText(count + unit + " in your cart");
        }
    }

    public static Map<Product, Integer> cartItems() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(cart));
    }

    private static JPanel factCard(String title, String value) {
        RoundedPanel card = new RoundedPanel(16, PINK_50, false);
        card.setLayout(new GridLayout(2, 1, 0, 3));
        card.setBorder(new EmptyBorder(9, 10, 9, 10));

        card.add(label(title, text(13), PINK_800));
        card.add(label(value, heading(16), PINK_900));

        return card;
    }

    private static JPanel transparent(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setOpaque(false);
        return panel;
    }

    private static void addLeft(JPanel parent, JComponent child, int gap) {
        child.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(child);

        if (gap > 0) {
            parent.add(Box.createVerticalStrut(gap));
        }
    }

    private static class ProductPhoto extends JComponent {

        private final Product product;
        private final int view;
        private final BufferedImage photo;

        ProductPhoto(Product product, int view) {
            this.product = product;
            this.view = view;
            this.photo = findPhoto();
            setToolTipText(product.name);
        }

        private BufferedImage findPhoto() {
            if ("Silk Repair Serum".equals(product.name)) {
                BufferedImage sheet = loadImage("/images/serum.png");

                if (sheet != null && sheet.getWidth() >= 3) {
                    int left = view * sheet.getWidth() / 3;
                    int right = (view + 1) * sheet.getWidth() / 3;

                    return sheet.getSubimage(
                            left, 0, right - left, sheet.getHeight());
                }
            }

            String path = product.image;
            if (path == null || path.isEmpty()) {
                return null;
            }

            int dot = path.lastIndexOf('.');
            if (view > 0 && dot > 0) {
                String suffix = view == 1 ? "-detail" : "-set";
                String extraPath = path.substring(0, dot)
                        + suffix + path.substring(dot);

                BufferedImage extra = loadImage(extraPath);
                if (extra != null) {
                    return extra;
                }
            }

            return loadImage(path);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();
            int w = getWidth();
            int h = getHeight();

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.clip(new RoundRectangle2D.Double(0, 0, w, h, 20, 20));

            if (photo != null) {
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                drawCover(g, photo, 0, 0, w, h);
            } else {
                drawPackaging(g, w, h);
            }

            g.dispose();
        }

        private void drawPackaging(Graphics2D g, int w, int h) {
            g.setPaint(new GradientPaint(0, 0, PINK_50, w, h, PINK_300));
            g.fillRect(0, 0, w, h);

            double scale = Math.min(w / 240.0, h / 290.0);
            if (view == 1) {
                scale *= 1.7;
            }

            g.translate(w / 2.0, h / 2.0);
            g.scale(scale, scale);

            if (view == 2) {
                g.rotate(-0.16);
            }

            g.setColor(PINK_300);
            g.fillOval(-70, 93, 146, 14);

            g.setPaint(new GradientPaint(-55, 0, PINK_100, 60, 0, PINK_500));
            g.fillRoundRect(-55, -68, 110, 168, 24, 24);

            g.setColor(PINK_800);
            g.fillRoundRect(-32, -106, 64, 44, 10, 10);

            g.setColor(PINK_50);
            g.fillRoundRect(-49, -15, 98, 72, 8, 8);

            g.setColor(PINK_900);
            g.setFont(heading(17));
            drawCentered(g, "Glam & Go", 13);

            g.setFont(text(12));
            drawCentered(g, product.category, 37);
        }

        private void drawCentered(Graphics2D g, String value, int y) {
            int x = -g.getFontMetrics().stringWidth(value) / 2;
            g.drawString(value, x, y);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new ProductDetails(null, PRODUCTS[0]).setVisible(true));
    }
}