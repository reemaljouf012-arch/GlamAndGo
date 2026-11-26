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

/**
 * Glam & Go - Home page.
 *
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

    // ===================== Colors =====================
    private static final Color PINK_50  = new Color(0xFFF3F7);
    private static final Color PINK_100 = new Color(0xFFE4EC);
    private static final Color PINK_200 = new Color(0xF9CEDB);
    private static final Color PINK_300 = new Color(0xEAB3C4);
    private static final Color PINK_500 = new Color(0xC17690);
    private static final Color PINK_800 = new Color(0x693146);
    private static final Color PINK_900 = new Color(0x4F1D31);
    private static final Color PINK_950 = new Color(0x390C1F);

    // ===================== Fonts =====================
    private static final Font HEADING_FONT = loadFont("/fonts/Recia-Medium.otf");
    private static final Font TEXT_FONT    = loadFont("/fonts/Sentient-Regular.otf");

    private static Font loadFont(String path) {
        try (InputStream in = Home.class.getResourceAsStream(path)) {
            if (in != null) {
                Font f = Font.createFont(Font.TRUETYPE_FONT, in);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(f);
                return f;
            }
        } catch (Exception e) {
            System.err.println("Could not load font: " + path);
        }
        return new Font(Font.SERIF, Font.PLAIN, 12);
    }

    private static Font heading(float size) { return HEADING_FONT.deriveFont(Font.PLAIN, size); }
    private static Font text(float size)    { return TEXT_FONT.deriveFont(Font.PLAIN, size); }

    // ===================== Images =====================
    private static final Map<String, BufferedImage> IMAGES = new HashMap<>();

    private static BufferedImage loadImage(String path) {
        if (IMAGES.containsKey(path)) return IMAGES.get(path);
        BufferedImage img = null;
        try {
            URL url = Home.class.getResource(path);
            if (url != null) img = ImageIO.read(url);
        } catch (Exception ignored) { }
        IMAGES.put(path, img);
        return img;
    }

    /** Draws the image so it fills the area (center-cropped). */
    private static void drawCover(Graphics2D g, BufferedImage img, int x, int y, int w, int h) {
        double s = Math.max((double) w / img.getWidth(), (double) h / img.getHeight());
        int dw = (int) Math.ceil(img.getWidth() * s);
        int dh = (int) Math.ceil(img.getHeight() * s);
        g.drawImage(img, x + (w - dw) / 2, y + (h - dh) / 2, dw, dh, null);
    }

    /** Draws the image fully visible inside the area (not cropped). */
    private static void drawContain(Graphics2D g, BufferedImage img, int x, int y, int w, int h) {
        double s = Math.min((double) w / img.getWidth(), (double) h / img.getHeight());
        int dw = (int) Math.round(img.getWidth() * s);
        int dh = (int) Math.round(img.getHeight() * s);
        g.drawImage(img, x + (w - dw) / 2, y + (h - dh) / 2, dw, dh, null);
    }

    private static void placeholder(Graphics2D g, int w, int h, String hint) {
        g.setPaint(new GradientPaint(0, 0, PINK_100, w, h, PINK_300));
        g.fillRect(0, 0, w, h);
        g.setColor(PINK_800);
        g.setFont(text(14f));
        FontMetrics fm = g.getFontMetrics();
        String msg = "Add " + hint;
        g.drawString(msg, (w - fm.stringWidth(msg)) / 2, h / 2 + fm.getAscent() / 2);
    }

    private static Graphics2D g2(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        return g2;
    }

    private static Path2D starShape(double cx, double cy, double outer, double inner, int points) {
        Path2D s = new Path2D.Float();
        for (int i = 0; i < points * 2; i++) {
            double a = -Math.PI / 2 + i * Math.PI / points;
            double r = (i % 2 == 0) ? outer : inner;
            double x = cx + r * Math.cos(a);
            double y = cy + r * Math.sin(a);
            if (i == 0) s.moveTo(x, y); else s.lineTo(x, y);
        }
        s.closePath();
        return s;
    }

    private static Path2D heartShape(float x, float y, float w, float h) {
        Path2D p = new Path2D.Float();
        p.moveTo(x + w / 2, y + h);
        p.curveTo(x - w * 0.25f, y + h * 0.45f, x + w * 0.15f, y - h * 0.2f, x + w / 2, y + h * 0.28f);
        p.curveTo(x + w * 0.85f, y - h * 0.2f, x + w * 1.25f, y + h * 0.45f, x + w / 2, y + h);
        p.closePath();
        return p;
    }

    // ===================== Small UI helpers =====================
    private static JPanel vbox() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        return p;
    }

    private static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    /** Wrapped text label. align = "left" | "center". */
    private static JLabel htmlLabel(String text, int width, String align, Font font, Color color) {
        String safe = text.replace("&", "&amp;");
        JLabel l = new JLabel("<html><div style='width:" + width + "px;text-align:" + align + ";'>" + safe + "</div></html>");
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    private static <T extends JComponent> T fixedSize(T c) {
        c.setMaximumSize(c.getPreferredSize());
        return c;
    }

    private static <T extends JComponent> T fixedHeight(T c) {
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
        return c;
    }

    private static JPanel sectionTitle(String title, String subtitle, Color titleColor, Color subColor) {
        JPanel p = vbox();
        JLabel t = label(title, heading(42f), titleColor);
        JLabel s = label(subtitle, text(18f), subColor);
        t.setAlignmentX(Component.CENTER_ALIGNMENT);
        s.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(t);
        p.add(Box.createVerticalStrut(10));
        p.add(s);
        return p;
    }

    // ===================== Layout building blocks =====================
    /** Full-width panel inside a BoxLayout: stretches horizontally, keeps its own height. */
    private static class FullWidth extends JPanel {
        @Override public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
        }
    }

    /** Centers its single child and limits its width. */
    private static class CenterLayout implements LayoutManager {
        private final int maxW;
        CenterLayout(int maxW) { this.maxW = maxW; }
        @Override public void addLayoutComponent(String name, Component comp) { }
        @Override public void removeLayoutComponent(Component comp) { }
        @Override public Dimension preferredLayoutSize(Container p) {
            int h = p.getComponentCount() > 0 ? p.getComponent(0).getPreferredSize().height : 0;
            return new Dimension(maxW, h);
        }
        @Override public Dimension minimumLayoutSize(Container p) {
            return new Dimension(0, preferredLayoutSize(p).height);
        }
        @Override public void layoutContainer(Container p) {
            if (p.getComponentCount() == 0) return;
            int w = Math.min(p.getWidth(), maxW);
            p.getComponent(0).setBounds((p.getWidth() - w) / 2, 0, w, p.getHeight());
        }
    }

    /** A page section: colored band, vertical padding, centered content. */
    private static class Section extends FullWidth {
        Section(Color bg, int vPad, int maxW, JComponent content) {
            setLayout(new BorderLayout());
            setBackground(bg);
            setBorder(new EmptyBorder(vPad, 40, vPad, 40));
            JPanel centered = new JPanel(new CenterLayout(maxW));
            centered.setOpaque(false);
            centered.add(content);
            add(centered, BorderLayout.CENTER);
        }
    }

    /** Scrollable body that always matches the window width (no clipped content). */
    private static class PageBody extends JPanel implements Scrollable {
        PageBody() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);
        }
        @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 24; }
        @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return Math.max(100, r.height - 60); }
        @Override public boolean getScrollableTracksViewportWidth() { return true; }
        @Override public boolean getScrollableTracksViewportHeight() { return false; }
    }

    // ===================== Custom components =====================
    private static class RoundedPanel extends JPanel {
        static final int SH = 6;
        private final int arc;
        private final Color fill;
        private final boolean shadow;

        RoundedPanel(int arc, Color fill, boolean shadow) {
            this.arc = arc;
            this.fill = fill;
            this.shadow = shadow;
            setOpaque(false);
        }

        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = g2(gr);
            int w = getWidth(), h = getHeight();
            int m = shadow ? SH : 0;
            if (shadow) {
                for (int i = 0; i < SH; i++) {
                    g.setColor(new Color(79, 29, 49, 9));
                    int inset = SH - i;
                    g.fill(new RoundRectangle2D.Float(inset, inset + 2, w - 2 * inset, h - 2 * inset, arc + i * 2, arc + i * 2));
                }
            }
            g.setColor(fill);
            g.fill(new RoundRectangle2D.Float(m, m, w - 2 * m, h - 2 * m, arc, arc));
            g.dispose();
        }
    }

    private static class PillButton extends JButton {
        private Color fill, fg, border, hoverFill;
        private final int padX, padY;
        private boolean hover;

        PillButton(String label, float fontSize, int padX, int padY) {
            super(label);
            this.padX = padX;
            this.padY = padY;
            setFont(text(fontSize));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
            });
        }

        PillButton style(Color fill, Color fg, Color border, Color hoverFill) {
            this.fill = fill;
            this.fg = fg;
            this.border = border;
            this.hoverFill = hoverFill;
            setForeground(fg);
            repaint();
            return this;
        }

        @Override public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(getFont());
            return new Dimension(fm.stringWidth(getText()) + 2 * padX, fm.getHeight() + 2 * padY);
        }

        @Override public Dimension getMaximumSize() { return getPreferredSize(); }

        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = g2(gr);
            int w = getWidth(), h = getHeight();
            Color f = hover ? hoverFill : fill;
            if (f != null) {
                g.setColor(f);
                g.fillRoundRect(0, 0, w, h, h, h);
            }
            if (border != null) {
                g.setColor(border);
                g.setStroke(new BasicStroke(1.6f));
                g.drawRoundRect(1, 1, w - 3, h - 3, h - 2, h - 2);
            }
            g.setColor(fg);
            g.setFont(getFont());
            FontMetrics fm = g.getFontMetrics();
            g.drawString(getText(), (w - fm.stringWidth(getText())) / 2, (h - fm.getHeight()) / 2 + fm.getAscent());
            g.dispose();
        }
    }

    private static class RoundedField extends JTextField {
        private final String placeholder;
        private final Color fill, border;

        RoundedField(String placeholder, int width, Color fill, Color border) {
            this.placeholder = placeholder;
            this.fill = fill;
            this.border = border;
            setOpaque(false);
            setBorder(new EmptyBorder(8, 18, 8, 18));
            setFont(text(15f));
            setForeground(PINK_900);
            setCaretColor(PINK_900);
            setPreferredSize(new Dimension(width, 40));
        }

        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = g2(gr);
            int w = getWidth(), h = getHeight();
            g.setColor(fill);
            g.fill(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, h, h));
            g.setColor(border);
            g.draw(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, h, h));
            if (getText().isEmpty()) {
                g.setColor(PINK_500);
                g.setFont(getFont());
                FontMetrics fm = g.getFontMetrics();
                g.drawString(placeholder, getInsets().left, (h - fm.getHeight()) / 2 + fm.getAscent());
            }
            g.dispose();
            super.paintComponent(gr);
        }
    }

    private static class IconButton extends JComponent {
        private final boolean cart;
        private boolean hover;

        IconButton(boolean cart, Runnable action) {
            this.cart = cart;
            setPreferredSize(new Dimension(32, 32));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
                @Override public void mouseClicked(MouseEvent e) { action.run(); }
            });
        }

        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = g2(gr);
            g.setColor(hover ? PINK_300 : PINK_50);
            g.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.translate(2, 2);
            if (cart) {
                Path2D p = new Path2D.Float();
                p.moveTo(0, 3); p.lineTo(4, 3); p.lineTo(8, 16); p.lineTo(21, 16); p.lineTo(24, 6); p.lineTo(5.5, 6);
                g.draw(p);
                g.fill(new Ellipse2D.Float(9, 20, 4, 4));
                g.fill(new Ellipse2D.Float(18, 20, 4, 4));
            } else {
                g.draw(new Ellipse2D.Float(8, 2, 12, 12));
                g.draw(new Arc2D.Float(3, 17, 22, 20, 0, 180, Arc2D.OPEN));
            }
            g.dispose();
        }
    }

    private static class Stars extends JComponent {
        private final double rating;
        private final int size;

        Stars(double rating, int size) {
            this.rating = rating;
            this.size = size;
            setPreferredSize(new Dimension(5 * (size + 4) - 4, size));
        }

        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = g2(gr);
            g.setStroke(new BasicStroke(1.2f));
            for (int i = 0; i < 5; i++) {
                Path2D s = starShape(i * (size + 4) + size / 2.0, size / 2.0, size / 2.0 - 0.5, size / 4.5, 5);
                g.setColor(PINK_500);
                if (i < Math.round(rating)) g.fill(s); else g.draw(s);
            }
            g.dispose();
        }
    }

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

    /** Product card image area with badge and heart. */
    private static class ProductTile extends JComponent {
        private final Product product;
        private boolean liked;
        private final Rectangle heart = new Rectangle();

        ProductTile(Product product, Runnable onOpen) {
            this.product = product;
            setPreferredSize(new Dimension(250, 280));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    if (heart.contains(e.getPoint())) {
                        liked = !liked;
                        repaint();
                    } else {
                        onOpen.run();
                    }
                }
            });
        }

        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = g2(gr);
            int sh = RoundedPanel.SH;
            int w = getWidth(), h = getHeight();
            int arc = 28;

            for (int i = 0; i < sh; i++) {
                g.setColor(new Color(79, 29, 49, 9));
                int inset = sh - i;
                g.fill(new RoundRectangle2D.Float(inset, inset + 2, w - 2 * inset, h - 2 * inset, arc + i * 2, arc + i * 2));
            }
            RoundRectangle2D card = new RoundRectangle2D.Float(sh, sh, w - 2 * sh, h - 2 * sh, arc, arc);
            g.setColor(Color.WHITE);
            g.fill(card);

            Graphics2D gi = (Graphics2D) g.create();
            gi.clip(card);
            BufferedImage img = loadImage(product.image);
            if (img != null) {
                drawContain(gi, img, sh + 28, sh + 44, w - 2 * sh - 56, h - 2 * sh - 70);
            } else {
                gi.setColor(PINK_50);
                gi.fill(card);
                gi.setColor(PINK_500);
                gi.setFont(text(13f));
                FontMetrics fm = gi.getFontMetrics();
                String msg = "Add " + product.image;
                gi.drawString(msg, (w - fm.stringWidth(msg)) / 2, h / 2 + fm.getAscent() / 2);
            }
            gi.dispose();

            if (product.badge != null) {
                g.setFont(text(13f));
                FontMetrics fm = g.getFontMetrics();
                int bw = fm.stringWidth(product.badge) + 26, bh = 26;
                g.setColor(PINK_800);
                g.fillRoundRect(sh + 14, sh + 14, bw, bh, bh, bh);
                g.setColor(Color.WHITE);
                g.drawString(product.badge, sh + 14 + 13, sh + 14 + (bh - fm.getHeight()) / 2 + fm.getAscent());
            }

            heart.setBounds(w - sh - 48, sh + 12, 34, 30);
            Path2D hs = heartShape(heart.x + 4, heart.y + 5, 26, 22);
            if (liked) {
                g.setColor(PINK_500);
                g.fill(hs);
            } else {
                g.setColor(PINK_800);
                g.setStroke(new BasicStroke(1.8f));
                g.draw(hs);
            }
            g.dispose();
        }
    }

    // ===================== Data =====================
    private static class Product {
        final String name, desc, category, badge, image;
        final double price, rating;

        Product(String name, String desc, String category, String badge, double price, double rating, String image) {
            this.name = name;
            this.desc = desc;
            this.category = category;
            this.badge = badge;
            this.price = price;
            this.rating = rating;
            this.image = image;
        }
    }

    private static final Product[] PRODUCTS = {
        new Product("Silk Repair Serum",   "Smooth & shiny hair",   "Hair Care", "Best seller", 48, 4, "/images/product1.jpg"),
        new Product("Rose Glow Cream",     "Deep hydration",        "Skincare",  null,          62, 5, "/images/product2.jpg"),
        new Product("Gentle Foam Cleanser","Fresh daily cleanse",   "Skincare",  "New",         56, 4, "/images/product3.jpg"),
        new Product("Velvet Matte Lipstick","Rich long-wear color", "Makeup",    null,          45, 4, "/images/product4.jpg"),
        new Product("Keratin Hair Mask",   "Repair & nourish",      "Hair Care", null,          52, 5, "/images/product5.jpg"),
        new Product("Cuticle Care Oil",    "Strong, healthy nails", "Nails",     "New",         28, 4, "/images/product6.jpg"),
        new Product("Radiance Foundation", "Natural glowing finish","Makeup",    null,          70, 4, "/images/product7.jpg"),
        new Product("Daily Sun Shield",    "SPF 50 protection",     "Skincare",  "Best seller", 45, 5, "/images/product8.jpg"),
    };

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

        add(buildNavbar(), BorderLayout.NORTH);

        PageBody body = new PageBody();
        body.add(buildHero());
        body.add(buildFeatures());
        body.add(buildCategories());
        body.add(buildBooking());
        body.add(buildProducts());
        body.add(buildReviews());
        body.add(buildFooter());

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
            // case "Products":     new Products().setVisible(true);    dispose(); break;
            // case "Appointments": new Appointments().setVisible(true); dispose(); break;
            // case "Cart":         new Cart().setVisible(true);        dispose(); break;
            // case "My Account":   new MyAccount().setVisible(true);   dispose(); break;
            default:
                JOptionPane.showMessageDialog(this, "\"" + page + "\" page is not connected yet.",
                        "Glam & Go", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ===================== Navbar =====================
    private JPanel buildNavbar() {
        JPanel nav = new JPanel(new BorderLayout(24, 0));
        nav.setBackground(PINK_900);
        nav.setBorder(new EmptyBorder(0, 40, 0, 40));
        nav.setPreferredSize(new Dimension(100, 80));

        JLabel logo = label("Glam & Go", heading(32f), PINK_50);
        logo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logo.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { navigate("Home"); }
        });

        JPanel links = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 26));
        links.setOpaque(false);
        for (String page : new String[]{"Home", "Services", "Products", "Appointments"}) {
            links.add(navLink(page));
        }

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 20));
        right.setOpaque(false);
        RoundedField search = new RoundedField("Search...", 230, PINK_50, PINK_300);
        search.addActionListener(e -> navigate("Products")); // TODO: pass search.getText() to the Products page
        right.add(search);
        right.add(new IconButton(true, () -> navigate("Cart")));
        right.add(new IconButton(false, () -> navigate("My Account")));

        nav.add(logo, BorderLayout.WEST);
        nav.add(links, BorderLayout.CENTER);
        nav.add(right, BorderLayout.EAST);
        return nav;
    }

    private JLabel navLink(String page) {
        JLabel l = label(page, text(17f), PINK_50);
        l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        l.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { l.setForeground(PINK_300); }
            @Override public void mouseExited(MouseEvent e)  { l.setForeground(PINK_50); }
            @Override public void mouseClicked(MouseEvent e) { navigate(page); }
        });
        return l;
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
            productsGrid.add(productCard(p));
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

    private JPanel productCard(Product p) {
        JPanel box = vbox();
        ProductTile tile = new ProductTile(p, () -> navigate("Product Details"));
        tile.setAlignmentX(Component.CENTER_ALIGNMENT);

        Stars stars = fixedSize(new Stars(p.rating, 15));
        stars.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel name = label(p.name, heading(18f), PINK_900);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel desc = label(p.desc, text(14f), PINK_800);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel price = label(String.format(Locale.US, "SAR %.2f", p.price), heading(18f), PINK_900);
        price.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(tile);
        box.add(Box.createVerticalStrut(12));
        box.add(stars);
        box.add(Box.createVerticalStrut(8));
        box.add(name);
        box.add(Box.createVerticalStrut(2));
        box.add(desc);
        box.add(Box.createVerticalStrut(6));
        box.add(price);
        return box;
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

    // ===================== Footer =====================
    private JComponent buildFooter() {
        JPanel cols = new JPanel(new GridLayout(1, 4, 40, 0));
        cols.setOpaque(false);

        // Brand
        JPanel brand = vbox();
        JLabel logo = label("Glam & Go", heading(34f), PINK_50);
        JLabel tag = htmlLabel("Beauty in every detail. Find your glow with us.", 220, "left", text(15f), PINK_200);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        tag.setAlignmentX(Component.LEFT_ALIGNMENT);
        brand.add(logo);
        brand.add(Box.createVerticalStrut(10));
        brand.add(tag);
        brand.add(Box.createVerticalGlue());

        cols.add(brand);
        cols.add(footerColumn("Shop", new String[][]{
                {"All Products", "Products"}, {"Hair Care", "Products"}, {"Skincare", "Products"}, {"Makeup", "Products"}}));
        cols.add(footerColumn("Support", new String[][]{
                {"My Appointments", "My Appointments"}, {"My Orders", "My Orders"}, {"My Account", "My Account"}, {"Contact Us", "Contact Us"}}));

        // Newsletter
        JPanel join = vbox();
        JLabel jt = label("Join Glam & Go", heading(22f), PINK_50);
        JLabel jd = htmlLabel("Subscribe for exclusive offers and beauty tips.", 230, "left", text(15f), PINK_200);
        RoundedField email = new RoundedField("Email Address", 230, PINK_100, PINK_100);
        PillButton send = new PillButton("Send", 16f, 30, 10).style(PINK_300, PINK_950, null, PINK_200);
        send.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Thank you for subscribing!", "Glam & Go", JOptionPane.INFORMATION_MESSAGE);
            email.setText("");
        });
        for (JComponent c : new JComponent[]{jt, jd, email, send}) c.setAlignmentX(Component.LEFT_ALIGNMENT);
        join.add(jt);
        join.add(Box.createVerticalStrut(10));
        join.add(jd);
        join.add(Box.createVerticalStrut(14));
        join.add(fixedSize(email));
        join.add(Box.createVerticalStrut(12));
        join.add(send);
        join.add(Box.createVerticalGlue());
        cols.add(join);

        JPanel content = vbox();
        content.add(cols);
        content.add(Box.createVerticalStrut(40));
        JLabel copy = label("\u00A9 2026 Glam & Go. All rights reserved.", text(14f), PINK_300);
        copy.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(copy);
        return new Section(PINK_900, 56, 1200, content);
    }

    private JPanel footerColumn(String title, String[][] links) {
        JPanel col = vbox();
        JLabel t = label(title, heading(22f), PINK_50);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        col.add(t);
        col.add(Box.createVerticalStrut(14));
        for (String[] link : links) {
            JLabel l = label(link[0], text(15f), PINK_200);
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            l.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { l.setForeground(Color.WHITE); }
                @Override public void mouseExited(MouseEvent e)  { l.setForeground(PINK_200); }
                @Override public void mouseClicked(MouseEvent e) { navigate(link[1]); }
            });
            col.add(l);
            col.add(Box.createVerticalStrut(10));
        }
        col.add(Box.createVerticalGlue());
        return col;
    }

    // ===================== Main (for testing this page alone) =====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Home().setVisible(true));
    }
}