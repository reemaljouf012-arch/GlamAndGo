package glamandgo;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSliderUI;
import static glamandgo.Theme.*;

/**
 * Glam & Go - Products Store page.
 * Data comes from Theme.PRODUCTS for now (later from the database).
 */
public class Products extends JFrame {

    private static final int PAGE_SIZE = 8;
    private static final int MIN_PRICE = 20, MAX_PRICE = 200;
    // Must match the names in the Product_Categories table
    private static final String[] CATEGORIES = {"Hair Care", "Skincare", "Makeup", "Nails"};
    private static final String[] SORTS = {"Best seller", "Price low to high", "Price high to low", "Rating"};

    private final List<JCheckBox> categoryBoxes = new ArrayList<>();
    private final JSlider priceSlider = new JSlider(MIN_PRICE, MAX_PRICE, MAX_PRICE);
    private final JLabel priceValue = label("", text(14f), PINK_800);
    private final JCheckBox inStockOnly = checkBox("In stock only");
    private final JComboBox<String> sortBox = new JComboBox<>(SORTS);
    private final JLabel counter = label("", text(17f), PINK_800);
    private final JPanel grid = new JPanel(new GridLayout(0, 4, 16, 36));
    private final JPanel pager = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
    private JScrollPane scroll;
    private int currentPage = 1;

    public Products() {
        setTitle("Glam & Go | Products");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 820);
        setMinimumSize(new Dimension(1100, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(navbar(this::navigate), BorderLayout.NORTH);

        FullWidth store = new FullWidth();
        store.setLayout(new BorderLayout());
        store.setBackground(Color.WHITE);
        store.add(buildSidebar(), BorderLayout.WEST);
        store.add(buildResults(), BorderLayout.CENTER);

        PageBody body = new PageBody();
        body.add(store);
        body.add(footer(this::navigate));

        scroll = new JScrollPane(body,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    private void navigate(String page) {
        switch (page) {
            case "Home":
                new Home().setVisible(true);
                dispose();
                break;
            case "Products":
                scroll.getVerticalScrollBar().setValue(0);
                break;
            default:
                JOptionPane.showMessageDialog(this, "\"" + page + "\" page is not connected yet.",
                        "Glam & Go", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ===================== Filters sidebar =====================
    private JComponent buildSidebar() {
        JPanel side = vbox();
        side.setOpaque(true);
        side.setBackground(PINK_50);
        side.setBorder(new EmptyBorder(30, 28, 30, 24));
        side.setPreferredSize(new Dimension(240, 520));

        addLeft(side, label("Category", heading(20f), PINK_900), 12);
        for (String category : CATEGORIES) {
            JCheckBox box = checkBox(category);
            box.addActionListener(e -> filtersChanged());
            categoryBoxes.add(box);
            addLeft(side, box, 6);
        }

        side.add(Box.createVerticalStrut(24));
        addLeft(side, label("Price", heading(20f), PINK_900), 10);

        priceSlider.setOpaque(false);
        priceSlider.setFocusable(true);
        priceSlider.setUI(new PinkSliderUI(priceSlider));
        priceSlider.setPreferredSize(new Dimension(180, 28));
        priceSlider.addChangeListener(e -> filtersChanged());
        addLeft(side, priceSlider, 4);
        addLeft(side, priceValue, 0);

        side.add(Box.createVerticalStrut(28));
        addLeft(side, label("Availability", heading(20f), PINK_900), 12);
        inStockOnly.addActionListener(e -> filtersChanged());
        addLeft(side, inStockOnly, 0);
        side.add(Box.createVerticalGlue());
        return side;
    }

    private static void addLeft(JPanel parent, JComponent child, int gapAfter) {
        child.setAlignmentX(Component.LEFT_ALIGNMENT);
        child.setMaximumSize(new Dimension(188, child.getPreferredSize().height));
        parent.add(child);
        parent.add(Box.createVerticalStrut(gapAfter));
    }

    private static JCheckBox checkBox(String title) {
        JCheckBox box = new JCheckBox(title);
        box.setFont(text(15f));
        box.setForeground(PINK_900);
        box.setOpaque(false);
        box.setFocusPainted(false);
        box.setIconTextGap(10);
        box.setIcon(new CheckIcon());
        box.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return box;
    }

    // ===================== Results area =====================
    private JComponent buildResults() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(counter, BorderLayout.WEST);

        JPanel sortRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        sortRow.setOpaque(false);
        sortRow.add(label("Sort by:", text(16f), PINK_800));
        sortBox.setFont(text(15f));
        sortBox.setForeground(PINK_900);
        sortBox.setBackground(Color.WHITE);
        sortBox.addActionListener(e -> filtersChanged());
        sortRow.add(sortBox);
        top.add(sortRow, BorderLayout.EAST);

        grid.setOpaque(false);
        pager.setOpaque(false);

        // Keeps the cards at their natural height when there are few results
        JPanel gridHolder = new JPanel(new BorderLayout());
        gridHolder.setOpaque(false);
        gridHolder.add(grid, BorderLayout.NORTH);

        JPanel results = new JPanel(new BorderLayout(0, 24));
        results.setOpaque(false);
        results.setBorder(new EmptyBorder(24, 24, 44, 24));
        results.add(top, BorderLayout.NORTH);
        results.add(gridHolder, BorderLayout.CENTER);
        results.add(pager, BorderLayout.SOUTH);
        return results;
    }

    // ===================== Filter, sort, paginate =====================
    private void filtersChanged() {
        currentPage = 1;
        refresh();
    }

    private List<Product> filteredProducts() {
        List<String> selected = new ArrayList<>();
        for (JCheckBox box : categoryBoxes) {
            if (box.isSelected()) selected.add(box.getText());
        }

        List<Product> found = new ArrayList<>();
        for (Product p : PRODUCTS) {
            if (!selected.isEmpty() && !selected.contains(p.category)) continue;
            if (p.price > priceSlider.getValue()) continue;
            if (inStockOnly.isSelected() && p.quantity <= 0) continue;
            found.add(p);
        }

        Comparator<Product> order;
        switch (sortBox.getSelectedIndex()) {
            case 1:  order = Comparator.comparingDouble(p -> p.price); break;
            case 2:  order = Comparator.<Product>comparingDouble(p -> p.price).reversed(); break;
            case 3:  order = Comparator.<Product>comparingDouble(p -> p.rating).reversed(); break;
            default: order = Comparator.<Product>comparingInt(p -> p.salesCount).reversed();
        }
        found.sort(order);
        return found;
    }

    private void refresh() {
        List<Product> found = filteredProducts();
        int totalPages = Math.max(1, (found.size() + PAGE_SIZE - 1) / PAGE_SIZE);
        currentPage = Math.min(currentPage, totalPages);
        int from = (currentPage - 1) * PAGE_SIZE;
        int to = Math.min(from + PAGE_SIZE, found.size());

        priceValue.setText("SAR " + MIN_PRICE + " - " + priceSlider.getValue());
        counter.setText(found.isEmpty()
                ? "No products match your filters"
                : "Showing " + (to - from) + " of " + found.size());

        grid.removeAll();
        for (Product p : found.subList(from, to)) {
            grid.add(productCard(p, () -> navigate("Product Details")));
        }
        // At least 4 cells, so a single card does not stretch across the row
        for (int i = to - from; i < 4; i++) {
            JPanel filler = new JPanel();
            filler.setOpaque(false);
            grid.add(filler);
        }

        buildPager(totalPages);
        revalidate();
        repaint();
    }

    private void buildPager(int totalPages) {
        pager.removeAll();
        if (totalPages == 1) return;
        pager.add(pageButton("<", currentPage - 1, currentPage > 1, false));
        for (int i = 1; i <= totalPages; i++) {
            pager.add(pageButton(String.valueOf(i), i, true, i == currentPage));
        }
        pager.add(pageButton(">", currentPage + 1, currentPage < totalPages, false));
    }

    private PillButton pageButton(String title, int target, boolean enabled, boolean active) {
        PillButton button = new PillButton(title, 15f, 0, 0) {
            @Override public Dimension getPreferredSize() { return new Dimension(42, 42); }
        };
        if (active) button.style(PINK_800, Color.WHITE, null, PINK_800);
        else if (enabled) button.style(null, PINK_800, PINK_300, PINK_100);
        else button.style(null, PINK_300, PINK_200, null);
        button.setEnabled(enabled);
        if (!enabled) button.setCursor(Cursor.getDefaultCursor());
        button.addActionListener(e -> {
            currentPage = target;
            refresh();
            SwingUtilities.invokeLater(() -> scroll.getVerticalScrollBar().setValue(0));
        });
        return button;
    }

    // ===================== Page-specific components =====================
    private static class CheckIcon implements Icon {
        @Override public int getIconWidth()  { return 17; }
        @Override public int getIconHeight() { return 17; }

        @Override public void paintIcon(Component c, Graphics gr, int x, int y) {
            Graphics2D g = g2(gr);
            boolean on = ((AbstractButton) c).isSelected();
            g.setColor(on ? PINK_800 : Color.WHITE);
            g.fillRoundRect(x, y, 16, 16, 5, 5);
            g.setColor(on ? PINK_800 : PINK_500);
            g.drawRoundRect(x, y, 16, 16, 5, 5);
            if (on) {
                g.setColor(Color.WHITE);
                g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.drawLine(x + 4, y + 8, x + 7, y + 11);
                g.drawLine(x + 7, y + 11, x + 12, y + 5);
            }
            g.dispose();
        }
    }

    private static class PinkSliderUI extends BasicSliderUI {
        PinkSliderUI(JSlider slider) { super(slider); }

        @Override protected Dimension getThumbSize() { return new Dimension(16, 16); }

        @Override public void paintTrack(Graphics gr) {
            Graphics2D g = g2(gr);
            int y = trackRect.y + trackRect.height / 2 - 2;
            g.setColor(PINK_200);
            g.fillRoundRect(trackRect.x, y, trackRect.width, 4, 4, 4);
            g.setColor(PINK_500);
            g.fillRoundRect(trackRect.x, y, thumbRect.x + 8 - trackRect.x, 4, 4, 4);
            g.dispose();
        }

        @Override public void paintThumb(Graphics gr) {
            Graphics2D g = g2(gr);
            g.setColor(PINK_800);
            g.fill(new Ellipse2D.Float(thumbRect.x, thumbRect.y, 15, 15));
            g.dispose();
        }

        @Override public void paintFocus(Graphics g) { }
    }

    // ===================== Main (for testing this page alone) =====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Products().setVisible(true));
    }
}