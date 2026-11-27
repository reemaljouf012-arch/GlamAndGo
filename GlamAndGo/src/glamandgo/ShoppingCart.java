
package glamandgo;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static glamandgo.Theme.*;

public class ShoppingCart extends JFrame {

    private final JPanel itemsPanel = vbox();

    private final JLabel itemCount =
            label("", heading(22f), PINK_900);

    private final JLabel subtotalLabel =
            label("", text(18f), PINK_800);

    private final JLabel totalLabel =
            label("", heading(24f), PINK_900);

    private final PillButton checkoutButton =
            new PillButton("Proceed to Checkout", 17f, 24, 12)
                    .style(PINK_800, Color.WHITE, null, PINK_950);

    // ===================== Constructor =====================

    public ShoppingCart() {

        setTitle("Glam & Go | Shopping Cart");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(1280, 820);

        setMinimumSize(new Dimension(1100, 600));

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(navbar(this::navigate), BorderLayout.NORTH);

        PageBody body = new PageBody();

        body.add(buildTitle());

        body.add(buildCart());

        body.add(footer(this::navigate));

        JScrollPane scroll = new JScrollPane(
                body,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setBorder(null);

        scroll.getVerticalScrollBar().setUnitIncrement(24);

        add(scroll, BorderLayout.CENTER);

        checkoutButton.addActionListener(e -> checkout());

        refreshCart();
    }

    // ===================== Page Title =====================

    private JComponent buildTitle() {

        JPanel content = vbox();

        content.add(sectionTitle(
                "Your Shopping Cart",
                "Your beauty essentials, all in one place.",
                PINK_900,
                PINK_800
        ));

        return new Section(
                PINK_50,
                48,
                1200,
                content
        );
    }

    // ===================== Cart Layout =====================

    private JComponent buildCart() {

        JPanel content = new JPanel(
                new BorderLayout(28, 0)
        );

        content.setOpaque(false);

        content.add(
                buildItemsSection(),
                BorderLayout.CENTER
        );

        JPanel summaryWrapper = new JPanel(
                new BorderLayout()
        );

        summaryWrapper.setOpaque(false);

        summaryWrapper.setPreferredSize(
                new Dimension(340, 400)
        );

        summaryWrapper.add(
                buildSummary(),
                BorderLayout.NORTH
        );

        content.add(
                summaryWrapper,
                BorderLayout.EAST
        );

        return new Section(
                PINK_50,
                35,
                1200,
                content
        );
    }

    // ===================== Cart Items =====================

    private JComponent buildItemsSection() {

        RoundedPanel card = new RoundedPanel(
                28,
                Color.WHITE,
                true
        );

        card.setLayout(new BorderLayout(0, 24));

        int s = RoundedPanel.SH;

        card.setBorder(new EmptyBorder(
                s + 24,
                s + 24,
                s + 24,
                s + 24
        ));

        card.add(itemCount, BorderLayout.NORTH);

        card.add(itemsPanel, BorderLayout.CENTER);

        PillButton continueButton =
                new PillButton(
                        "Continue Shopping",
                        16f,
                        22,
                        10
                ).style(
                        PINK_50,
                        PINK_800,
                        PINK_300,
                        PINK_100
                );

        continueButton.addActionListener(
                e -> navigate("Products")
        );

        JPanel bottom = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 0, 0)
        );

        bottom.setOpaque(false);

        bottom.add(continueButton);

        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    // ===================== Single Cart Item =====================

    private JComponent buildItem(
            Product product,
            int quantity) {

        JPanel row = new JPanel(
                new BorderLayout(18, 0)
        );

        row.setOpaque(false);

        row.setBorder(
                new EmptyBorder(16, 0, 16, 0)
        );

        // Product Image

        CartImage image = new CartImage(product.image);

        row.add(image, BorderLayout.WEST);

        // Product Information

        JPanel info = vbox();

        addLeft(info, label(
                product.category,
                text(13f),
                PINK_500
        ), 5);

        addLeft(info, htmlLabel(
                product.name,
                220,
                "left",
                heading(21f),
                PINK_900
        ), 6);

        addLeft(info, htmlLabel(
                product.desc,
                220,
                "left",
                text(14f),
                PINK_800
        ), 8);

        addLeft(info, label(
                money(product.price),
                heading(18f),
                PINK_800
        ), 0);

        row.add(info, BorderLayout.CENTER);

        // Quantity and Total

        JPanel actions = vbox();

        JLabel itemTotal = label(
                money(product.price * quantity),
                heading(19f),
                PINK_900
        );

        itemTotal.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        actions.add(itemTotal);

        actions.add(Box.createVerticalStrut(12));

        JPanel quantityPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 8, 0)
        );

        quantityPanel.setOpaque(false);

        PillButton minus = new PillButton(
                "-", 18f, 12, 5
        ).style(
                PINK_50,
                PINK_800,
                PINK_300,
                PINK_100
        );

        PillButton plus = new PillButton(
                "+", 18f, 12, 5
        ).style(
                PINK_50,
                PINK_800,
                PINK_300,
                PINK_100
        );

        JLabel quantityText = label(
                String.valueOf(quantity),
                heading(18f),
                PINK_900
        );

        minus.setEnabled(quantity > 1);

        plus.setEnabled(quantity < product.quantity);

        minus.addActionListener(e ->
                changeQuantity(product, -1)
        );

        plus.addActionListener(e ->
                changeQuantity(product, 1)
        );

        quantityPanel.add(minus);

        quantityPanel.add(quantityText);

        quantityPanel.add(plus);

        actions.add(fixedHeight(quantityPanel));

        actions.add(Box.createVerticalStrut(10));

        // Remove Button

        JButton remove = new JButton("Remove");

        remove.setFont(text(14f));

        remove.setForeground(PINK_800);

        remove.setBorderPainted(false);

        remove.setContentAreaFilled(false);

        remove.setFocusPainted(false);

        remove.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        remove.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        remove.addActionListener(e ->
                removeProduct(product)
        );

        actions.add(remove);

        row.add(actions, BorderLayout.EAST);

        return row;
    }

    // ===================== Order Summary =====================

    private JComponent buildSummary() {

        RoundedPanel card = new RoundedPanel(
                28,
                PINK_100,
                true
        );

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        int s = RoundedPanel.SH;

        card.setBorder(new EmptyBorder(
                s + 26,
                s + 22,
                s + 26,
                s + 22
        ));

        addLeft(card, label(
                "Order Summary",
                heading(26f),
                PINK_900
        ), 30);

        card.add(summaryRow(
                "Subtotal",
                subtotalLabel
        ));

        card.add(Box.createVerticalStrut(22));

        JSeparator separator = new JSeparator();

        separator.setForeground(PINK_300);

        separator.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 1)
        );

        card.add(separator);

        card.add(Box.createVerticalStrut(22));

        card.add(summaryRow(
                "Total",
                totalLabel
        ));

        card.add(Box.createVerticalStrut(30));

        checkoutButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        checkoutButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        checkoutButton.getPreferredSize().height
                )
        );

        card.add(checkoutButton);

        card.add(Box.createVerticalStrut(18));

        JLabel secure = htmlLabel(
                "Review your items before checkout.",
                250,
                "center",
                text(14f),
                PINK_800
        );

        secure.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        card.add(secure);

        return card;
    }

    // ===================== Summary Row =====================

    private JComponent summaryRow(
            String title,
            JLabel value) {

        JPanel row = new JPanel(
                new BorderLayout(10, 0)
        );

        row.setOpaque(false);

        row.add(
                label(title, text(18f), PINK_900),
                BorderLayout.WEST
        );

        row.add(value, BorderLayout.EAST);

        return fixedHeight(row);
    }

    // ===================== Refresh Cart =====================

    private void refreshCart() {

        itemsPanel.removeAll();

        int count = 0;

        double subtotal = 0;

        Map<Product, Integer> cart =
                ProductDetails.cart;

        for (Map.Entry<Product, Integer> entry
                : cart.entrySet()) {

            Product product = entry.getKey();

            int quantity = entry.getValue();

            count += quantity;

            subtotal += product.price * quantity;

            itemsPanel.add(
                    buildItem(product, quantity)
            );

            JSeparator line = new JSeparator();

            line.setForeground(PINK_200);

            line.setMaximumSize(
                    new Dimension(Integer.MAX_VALUE, 1)
            );

            itemsPanel.add(line);
        }

        if (cart.isEmpty()) {

            itemsPanel.add(buildEmptyCart());
        }

        itemCount.setText(
                "Cart Items (" + count + ")"
        );

        subtotalLabel.setText(money(subtotal));

        totalLabel.setText(money(subtotal));

        checkoutButton.setEnabled(!cart.isEmpty());

        itemsPanel.revalidate();

        itemsPanel.repaint();

        revalidate();

        repaint();
    }

    // ===================== Empty Cart =====================

    private JComponent buildEmptyCart() {

        JPanel empty = vbox();

        empty.setBorder(
                new EmptyBorder(65, 15, 65, 15)
        );

        JLabel title = label(
                "Your cart is empty",
                heading(28f),
                PINK_900
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel message = htmlLabel(
                "Explore our beauty collection and add "
                + "your favorite products to your cart.",
                350,
                "center",
                text(17f),
                PINK_800
        );

        message.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        empty.add(title);

        empty.add(Box.createVerticalStrut(15));

        empty.add(message);

        return empty;
    }

    // ===================== Change Quantity =====================

    private void changeQuantity(
            Product product,
            int change) {

        int current = ProductDetails.cart
                .getOrDefault(product, 0);

        int updated = current + change;

        if (updated >= 1
                && updated <= product.quantity) {

            ProductDetails.cart.put(
                    product,
                    updated
            );

            refreshCart();
        }
    }

    // ===================== Remove Product =====================

    private void removeProduct(Product product) {

        int result = JOptionPane.showConfirmDialog(
                this,
                "Remove " + product.name + " from your cart?",
                "Remove Product",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {

            ProductDetails.cart.remove(product);

            refreshCart();
        }
    }

    // ===================== Checkout =====================

    private void checkout() {

        if (ProductDetails.cart.isEmpty()) {

            return;
        }

        // Open Payment Page

        Payment payment = new Payment();

        payment.setVisible(true);

        // Close Shopping Cart Page

        dispose();
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

                new Products().setVisible(true);

                dispose();

                break;

            case "Cart":
            case "Shopping Cart":

                refreshCart();

                break;

            default:

                JOptionPane.showMessageDialog(
                        this,
                        page + " page is not connected yet.",
                        "Glam & Go",
                        JOptionPane.INFORMATION_MESSAGE
                );
        }
    }

    // ===================== Helpers =====================

    private void addLeft(
            JPanel panel,
            JComponent component,
            int gap) {

        component.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(component);

        if (gap > 0) {

            panel.add(Box.createVerticalStrut(gap));
        }
    }

    private String money(double amount) {

        return String.format(
                Locale.US,
                "SAR %.2f",
                amount
        );
    }

    // ===================== Product Image =====================

    private static class CartImage extends JComponent {

        private final String path;

        CartImage(String path) {

            this.path = path;

            setPreferredSize(
                    new Dimension(115, 135)
            );
        }

        @Override
        protected void paintComponent(Graphics gr) {

            Graphics2D g = g2(gr);

            int w = getWidth();

            int h = getHeight();

            g.clip(new RoundRectangle2D.Double(
                    0, 0, w, h, 20, 20
            ));

            g.setColor(PINK_50);

            g.fillRect(0, 0, w, h);

            BufferedImage image = loadImage(path);

            if (image != null) {

                drawCover(g, image, 0, 0, w, h);

            } else {

                g.setColor(PINK_500);

                g.setFont(text(12f));

                g.drawString(
                        "Product Image",
                        12,
                        h / 2
                );
            }

            g.dispose();
        }
    }

    // ===================== Main =====================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->

                new ShoppingCart().setVisible(true)

        );
    }
}
