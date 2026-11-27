
package glamandgo;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSliderUI;

import static glamandgo.Theme.*;

public class Products extends JFrame {

    private static final int PAGE_SIZE = 8;
    private static final int MIN_PRICE = 20;
    private static final int MAX_PRICE = 200;

    private static final String[] CATEGORIES = {
        "Hair Care", "Skincare", "Makeup", "Nails"
    };

    private static final String[] SORTS = {
        "Best seller", "Price low to high",
        "Price high to low", "Rating"
    };

    private final List<JCheckBox> categoryBoxes =
            new ArrayList<>();

    private final JSlider priceSlider =
            new JSlider(MIN_PRICE, MAX_PRICE, MAX_PRICE);

    private final JLabel priceValue =
            label("", text(14), PINK_800);

    private final JCheckBox inStockOnly =
            checkBox("In stock only");

    private final JComboBox<String> sortBox =
            new JComboBox<>(SORTS);

    private final JLabel counter =
            label("", text(17), PINK_800);

    private final JPanel grid =
            new JPanel(new GridLayout(0, 4, 16, 36));

    private final JPanel pager =
            new JPanel(new FlowLayout(
                    FlowLayout.CENTER, 10, 0));

    private final JScrollPane scroll;

    private int currentPage = 1;

    // ===================== Constructor =====================

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

        scroll = new JScrollPane(
                body,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setBorder(null);

        scroll.getVerticalScrollBar().setUnitIncrement(24);

        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    // ===================== Navigation =====================

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

                scroll.getVerticalScrollBar().setValue(0);

                break;

            // Shopping Cart Navigation

            case "Cart":
            case "Shopping Cart":

                new ShoppingCart().setVisible(true);

                dispose();

                break;

            // My Appointments Navigation

            case "Appointments":
            case "My Appointments":

                new MyAppointments().setVisible(true);

                dispose();

                break;

            // My Orders Navigation

            case "My Orders":
            case "Orders":

                new MyOrders().setVisible(true);

                dispose();

                break;

            default:

                JOptionPane.showMessageDialog(
                        this,
                        "\"" + page
                        + "\" page is not connected yet.",
                        "Glam & Go",
                        JOptionPane.INFORMATION_MESSAGE
                );
        }
    }

    // ===================== Sidebar =====================

    private JComponent buildSidebar() {

        JPanel side = vbox();

        side.setOpaque(true);

        side.setBackground(PINK_50);

        side.setBorder(
                new EmptyBorder(30, 28, 30, 24));

        side.setPreferredSize(
                new Dimension(240, 520));

        addLeft(
                side,
                label("Category", heading(20), PINK_900),
                12
        );

        for (String category : CATEGORIES) {

            JCheckBox box = checkBox(category);

            box.addActionListener(
                    e -> filtersChanged());

            categoryBoxes.add(box);

            addLeft(side, box, 6);
        }

        side.add(Box.createVerticalStrut(24));

        addLeft(
                side,
                label("Price", heading(20), PINK_900),
                10
        );

        priceSlider.setOpaque(false);

        priceSlider.setUI(
                new PinkSliderUI(priceSlider));

        priceSlider.setPreferredSize(
                new Dimension(180, 28));

        priceSlider.addChangeListener(
                e -> filtersChanged());

        addLeft(side, priceSlider, 4);

        addLeft(side, priceValue, 0);

        side.add(Box.createVerticalStrut(28));

        addLeft(
                side,
                label("Availability", heading(20), PINK_900),
                12
        );

        inStockOnly.addActionListener(
                e -> filtersChanged());

        addLeft(side, inStockOnly, 0);

        side.add(Box.createVerticalGlue());

        return side;
    }

    // ===================== Sidebar Helpers =====================

    private static void addLeft(
            JPanel parent,
            JComponent component,
            int gap) {

        component.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        component.setMaximumSize(
                new Dimension(
                        188,
                        component.getPreferredSize().height
                )
        );

        parent.add(component);

        if (gap > 0) {

            parent.add(
                    Box.createVerticalStrut(gap));
        }
    }

    private static JCheckBox checkBox(String title) {

        JCheckBox box = new JCheckBox(title);

        box.setFont(text(15));

        box.setForeground(PINK_900);

        box.setOpaque(false);

        box.setIconTextGap(10);

        box.setIcon(new CheckIcon());

        box.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR));

        return box;
    }

    // ===================== Product Results =====================

    private JComponent buildResults() {

        JPanel top = new JPanel(
                new BorderLayout());

        top.setOpaque(false);

        top.add(counter, BorderLayout.WEST);

        JPanel sortRow = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT, 8, 0));

        sortRow.setOpaque(false);

        sortRow.add(
                label("Sort by:", text(16), PINK_800));

        sortBox.setFont(text(15));

        sortBox.setForeground(PINK_900);

        sortBox.setBackground(Color.WHITE);

        sortBox.addActionListener(
                e -> filtersChanged());

        sortRow.add(sortBox);

        top.add(sortRow, BorderLayout.EAST);

        grid.setOpaque(false);

        pager.setOpaque(false);

        JPanel gridHolder = new JPanel(
                new BorderLayout());

        gridHolder.setOpaque(false);

        gridHolder.add(grid, BorderLayout.NORTH);

        JPanel results = new JPanel(
                new BorderLayout(0, 24));

        results.setOpaque(false);

        results.setBorder(
                new EmptyBorder(24, 24, 44, 24));

        results.add(top, BorderLayout.NORTH);

        results.add(gridHolder, BorderLayout.CENTER);

        results.add(pager, BorderLayout.SOUTH);

        return results;
    }

    // ===================== Filters =====================

    private void filtersChanged() {

        currentPage = 1;

        refresh();
    }

    private List<Product> filteredProducts() {

        List<String> selectedCategories =
                new ArrayList<>();

        for (JCheckBox box : categoryBoxes) {

            if (box.isSelected()) {

                selectedCategories.add(box.getText());
            }
        }

        List<Product> found = new ArrayList<>();

        for (Product product : PRODUCTS) {

            if (!selectedCategories.isEmpty()
                    && !selectedCategories.contains(
                            product.category)) {

                continue;
            }

            if (product.price > priceSlider.getValue()) {

                continue;
            }

            if (inStockOnly.isSelected()
                    && product.quantity <= 0) {

                continue;
            }

            found.add(product);
        }

        Comparator<Product> order;

        switch (sortBox.getSelectedIndex()) {

            case 1:

                order = Comparator.comparingDouble(
                        p -> p.price);

                break;

            case 2:

                order = Comparator.<Product>comparingDouble(
                        p -> p.price).reversed();

                break;

            case 3:

                order = Comparator.<Product>comparingDouble(
                        p -> p.rating).reversed();

                break;

            default:

                order = Comparator.<Product>comparingInt(
                        p -> p.salesCount).reversed();
        }

        found.sort(order);

        return found;
    }

    // ===================== Refresh Products =====================

    private void refresh() {

        List<Product> found = filteredProducts();

        int totalPages = Math.max(
                1,
                (found.size() + PAGE_SIZE - 1) / PAGE_SIZE
        );

        currentPage = Math.min(
                currentPage, totalPages);

        int from = (currentPage - 1) * PAGE_SIZE;

        int to = Math.min(
                from + PAGE_SIZE, found.size());

        priceValue.setText(
                "SAR " + MIN_PRICE
                + " - " + priceSlider.getValue());

        if (found.isEmpty()) {

            counter.setText(
                    "No products match your filters");

        } else {

            counter.setText(
                    "Showing " + (to - from)
                    + " of " + found.size());
        }

        grid.removeAll();

        for (Product product : found.subList(from, to)) {

            grid.add(productCard(product, () -> {

                ProductDetails details =
                        new ProductDetails(this, product);

                details.setVisible(true);
            }));
        }

        for (int i = to - from; i < 4; i++) {

            JPanel filler = new JPanel();

            filler.setOpaque(false);

            grid.add(filler);
        }

        buildPager(totalPages);

        revalidate();

        repaint();
    }

    // ===================== Pagination =====================

    private void buildPager(int totalPages) {

        pager.removeAll();

        if (totalPages == 1) {

            return;
        }

        pager.add(pageButton(
                "<",
                currentPage - 1,
                currentPage > 1,
                false
        ));

        for (int page = 1; page <= totalPages; page++) {

            pager.add(pageButton(
                    String.valueOf(page),
                    page,
                    true,
                    page == currentPage
            ));
        }

        pager.add(pageButton(
                ">",
                currentPage + 1,
                currentPage < totalPages,
                false
        ));
    }

    private PillButton pageButton(
            String title,
            int target,
            boolean enabled,
            boolean active) {

        PillButton button = new PillButton(
                title, 15, 0, 0) {

            @Override
            public Dimension getPreferredSize() {

                return new Dimension(42, 42);
            }
        };

        if (active) {

            button.style(
                    PINK_800,
                    Color.WHITE,
                    null,
                    PINK_800
            );

        } else if (enabled) {

            button.style(
                    null,
                    PINK_800,
                    PINK_300,
                    PINK_100
            );

        } else {

            button.style(
                    null,
                    PINK_300,
                    PINK_200,
                    null
            );
        }

        button.setEnabled(enabled);

        if (!enabled) {

            button.setCursor(
                    Cursor.getDefaultCursor());
        }

        button.addActionListener(e -> {

            currentPage = target;

            refresh();

            SwingUtilities.invokeLater(
                    () -> scroll.getVerticalScrollBar()
                            .setValue(0));
        });

        return button;
    }

    // ===================== Custom Checkbox =====================

    private static class CheckIcon implements Icon {

        @Override
        public int getIconWidth() {

            return 17;
        }

        @Override
        public int getIconHeight() {

            return 17;
        }

        @Override
        public void paintIcon(
                Component component,
                Graphics graphics,
                int x,
                int y) {

            Graphics2D g =
                    (Graphics2D) graphics.create();

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            boolean selected =
                    ((JCheckBox) component).isSelected();

            g.setColor(
                    selected ? PINK_800 : Color.WHITE);

            g.fillRoundRect(
                    x, y, 16, 16, 5, 5);

            g.setColor(
                    selected ? PINK_800 : PINK_300);

            g.drawRoundRect(
                    x, y, 16, 16, 5, 5);

            if (selected) {

                g.setColor(Color.WHITE);

                g.setStroke(new BasicStroke(2f));

                g.drawLine(
                        x + 4, y + 8,
                        x + 7, y + 11);

                g.drawLine(
                        x + 7, y + 11,
                        x + 12, y + 5);
            }

            g.dispose();
        }
    }

    // ===================== Custom Price Slider =====================

    private static class PinkSliderUI
            extends BasicSliderUI {

        PinkSliderUI(JSlider slider) {

            super(slider);
        }

        @Override
        public void paintTrack(Graphics graphics) {

            Graphics2D g =
                    (Graphics2D) graphics.create();

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int x = trackRect.x;

            int y = trackRect.y
                    + trackRect.height / 2 - 2;

            int thumbCenter = thumbRect.x
                    + thumbRect.width / 2;

            g.setColor(PINK_200);

            g.fillRoundRect(
                    x,
                    y,
                    trackRect.width,
                    5,
                    5,
                    5
            );

            g.setColor(PINK_800);

            g.fillRoundRect(
                    x,
                    y,
                    Math.max(0, thumbCenter - x),
                    5,
                    5,
                    5
            );

            g.dispose();
        }

        @Override
        public void paintThumb(Graphics graphics) {

            Graphics2D g =
                    (Graphics2D) graphics.create();

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int size = 15;

            int x = thumbRect.x
                    + (thumbRect.width - size) / 2;

            int y = thumbRect.y
                    + (thumbRect.height - size) / 2;

            g.setColor(PINK_800);

            g.fillOval(x, y, size, size);

            g.dispose();
        }
    }

    // ===================== Main =====================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new Products().setVisible(true)
        );
    }
}
