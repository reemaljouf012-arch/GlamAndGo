
package glamandgo;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static glamandgo.Theme.*;

public class MyOrders extends JFrame {

    // ===================== Temporary Orders =====================

    private static final ArrayList<Order> orders =
            new ArrayList<>();

    private static int nextOrderId = 1001;

    private final JPanel ordersPanel = new JPanel();

    private final JPanel shoppingPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 0, 0)
    );

    private final JLabel orderCount =
            label("0 orders found", text(16f), PINK_600);

    private final JComboBox<String> statusFilter =
            new JComboBox<>(new String[]{
                "All Orders",
                "Pending",
                "Processing",
                "Shipped",
                "Delivered",
                "Cancelled"
            });

    // ===================== Order Item Model =====================

    static class OrderItem {

        final String name;
        final String image;
        final double price;
        final int quantity;

        OrderItem(Product product, int quantity) {

            this.name = product.name;
            this.image = product.image;
            this.price = product.price;
            this.quantity = quantity;
        }

        double getTotal() {
            return price * quantity;
        }
    }

    // ===================== Order Model =====================

    static class Order {

        final String id;
        final LocalDateTime date;

        final String customerName;
        final String phone;
        final String receivingMethod;
        final String paymentMethod;
        final String address;

        final double subtotal;
        final double deliveryFee;
        final double total;

        final ArrayList<OrderItem> items;

        String status;

        Order(
                String customerName,
                String phone,
                String receivingMethod,
                String paymentMethod,
                String address,
                double deliveryFee,
                Map<Product, Integer> cart) {

            this.id = "ORD-" + nextOrderId++;
            this.date = LocalDateTime.now();

            this.customerName = customerName;
            this.phone = phone;
            this.receivingMethod = receivingMethod;
            this.paymentMethod = paymentMethod;
            this.address = address;
            this.deliveryFee = deliveryFee;

            this.items = new ArrayList<>();

            double amount = 0;

            for (Map.Entry<Product, Integer> entry
                    : cart.entrySet()) {

                OrderItem item = new OrderItem(
                        entry.getKey(),
                        entry.getValue()
                );

                items.add(item);

                amount += item.getTotal();
            }

            this.subtotal = amount;
            this.total = subtotal + deliveryFee;
            this.status = "Pending";
        }

        int getItemCount() {

            int count = 0;

            for (OrderItem item : items) {
                count += item.quantity;
            }

            return count;
        }
    }

    // ===================== Add New Order =====================

    public static void addOrder(
            String customerName,
            String phone,
            String receivingMethod,
            String paymentMethod,
            String address,
            double deliveryFee,
            Map<Product, Integer> cart) {

        if (cart == null || cart.isEmpty()) {
            return;
        }

        Order order = new Order(
                customerName,
                phone,
                receivingMethod,
                paymentMethod,
                address,
                deliveryFee,
                cart
        );

        orders.add(0, order);
    }

    // ===================== Constructor =====================

    public MyOrders() {

        setTitle("Glam & Go | My Orders");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 820);
        setMinimumSize(new Dimension(1100, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(PINK_50);

        add(navbar(this::navigate), BorderLayout.NORTH);

        PageBody body = new PageBody();

        body.setBackground(PINK_50);

        body.add(buildHero());
        body.add(buildOrdersSection());
        body.add(footer(this::navigate));

        JScrollPane scroll = new JScrollPane(
                body,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setBorder(null);
        scroll.getViewport().setBackground(PINK_50);

        scroll.getVerticalScrollBar()
                .setUnitIncrement(24);

        add(scroll, BorderLayout.CENTER);

        refreshOrders();
    }

    // ===================== Hero =====================

    private JComponent buildHero() {

        JPanel content = new JPanel(
                new BorderLayout()
        );

        content.setOpaque(false);

        content.add(sectionTitle(
                "My Orders",
                "View and manage all your beauty purchases.",
                PINK_900,
                PINK_800
        ), BorderLayout.CENTER);

        return new Section(
                PINK_50,
                40,
                1200,
                content
        );
    }

    // ===================== Orders Section =====================

    private JComponent buildOrdersSection() {

        JPanel content = new JPanel(
                new BorderLayout(0, 24)
        );

        content.setOpaque(false);

        content.add(
                buildFilterBar(),
                BorderLayout.NORTH
        );

        ordersPanel.setLayout(
                new BoxLayout(
                        ordersPanel,
                        BoxLayout.Y_AXIS
                )
        );

        ordersPanel.setOpaque(false);

        JPanel ordersWrapper = new JPanel(
                new BorderLayout()
        );

        ordersWrapper.setOpaque(false);

        ordersWrapper.add(
                ordersPanel,
                BorderLayout.NORTH
        );

        content.add(
                ordersWrapper,
                BorderLayout.CENTER
        );

        shoppingPanel.setOpaque(false);

        shoppingPanel.setBorder(
                new EmptyBorder(8, 0, 12, 0)
        );

        PillButton shopButton = new PillButton(
                "Continue Shopping",
                17f,
                30,
                12
        ).style(
                PINK_800,
                Color.WHITE,
                null,
                PINK_950
        );

        shopButton.addActionListener(
                e -> navigate("Products")
        );

        shoppingPanel.add(shopButton);

        content.add(
                shoppingPanel,
                BorderLayout.SOUTH
        );

        return new Section(
                PINK_50,
                25,
                1200,
                content
        );
    }

    // ===================== Filter Bar =====================

    private JComponent buildFilterBar() {

        RoundedPanel bar = new RoundedPanel(
                22,
                Color.WHITE,
                true
        );

        bar.setLayout(
                new BorderLayout(25, 0)
        );

        int s = RoundedPanel.SH;

        bar.setBorder(new EmptyBorder(
                s + 20,
                s + 28,
                s + 20,
                s + 28
        ));

        JPanel left = new JPanel(
                new BorderLayout(0, 8)
        );

        left.setOpaque(false);

        JLabel historyTitle = label(
                "Order History",
                heading(26f),
                PINK_900
        );

        left.add(
                historyTitle,
                BorderLayout.NORTH
        );

        left.add(
                orderCount,
                BorderLayout.CENTER
        );

        bar.add(
                left,
                BorderLayout.CENTER
        );

        JPanel filterPanel = new JPanel(
                new BorderLayout(0, 7)
        );

        filterPanel.setOpaque(false);

        JLabel filterTitle = label(
                "Filter by Status",
                text(14f),
                PINK_800
        );

        filterPanel.add(
                filterTitle,
                BorderLayout.NORTH
        );

        statusFilter.setFont(text(16f));
        statusFilter.setForeground(PINK_900);
        statusFilter.setBackground(PINK_50);

        statusFilter.setPreferredSize(
                new Dimension(200, 42)
        );

        statusFilter.setBorder(
                BorderFactory.createLineBorder(PINK_300)
        );

        statusFilter.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        statusFilter.addActionListener(
                e -> refreshOrders()
        );

        filterPanel.add(
                statusFilter,
                BorderLayout.CENTER
        );

        JPanel right = new JPanel(
                new GridBagLayout()
        );

        right.setOpaque(false);

        right.add(filterPanel);

        bar.add(
                right,
                BorderLayout.EAST
        );

        return fullWidth(bar);
    }

    // ===================== Refresh Orders =====================

    private void refreshOrders() {

        ordersPanel.removeAll();

        String selectedStatus =
                (String) statusFilter.getSelectedItem();

        int count = 0;

        for (Order order : orders) {

            boolean matches =
                    "All Orders".equals(selectedStatus)
                    || order.status.equals(selectedStatus);

            if (matches) {

                if (count > 0) {

                    ordersPanel.add(
                            Box.createVerticalStrut(22)
                    );
                }

                ordersPanel.add(
                        fullWidth(buildOrderCard(order))
                );

                count++;
            }
        }

        orderCount.setText(
                count + (count == 1
                        ? " order found"
                        : " orders found")
        );

        if (count == 0) {

            ordersPanel.add(
                    fullWidth(buildEmptyState())
            );
        }

        shoppingPanel.setVisible(count > 0);

        ordersPanel.revalidate();
        ordersPanel.repaint();
        shoppingPanel.revalidate();

        revalidate();
        repaint();
    }

    // ===================== Empty State =====================

    private JComponent buildEmptyState() {

        RoundedPanel empty = new RoundedPanel(
                26,
                Color.WHITE,
                true
        );

        empty.setLayout(new GridBagLayout());

        int s = RoundedPanel.SH;

        empty.setBorder(new EmptyBorder(
                s + 42,
                s + 30,
                s + 42,
                s + 30
        ));

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 22, 0);

        empty.add(
                new ShoppingBagIcon(),
                gbc
        );

        String message;
        String description;

        if (orders.isEmpty()) {

            message = "No Orders Yet";

            description =
                    "Your beauty purchases will appear here "
                    + "once you place your first order.";

        } else {

            message = "No Matching Orders";

            description =
                    "There are no orders with "
                    + "the selected status.";
        }

        JLabel title = label(
                message,
                heading(32f),
                PINK_900
        );

        title.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 14, 0);

        empty.add(title, gbc);

        JLabel subtitle = htmlLabel(
                description,
                420,
                "center",
                text(17f),
                PINK_800
        );

        subtitle.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 26, 0);

        empty.add(subtitle, gbc);

        PillButton button = new PillButton(
                orders.isEmpty()
                        ? "Explore Products"
                        : "View All Orders",
                17f,
                32,
                13
        ).style(
                PINK_800,
                Color.WHITE,
                null,
                PINK_950
        );

        button.addActionListener(e -> {

            if (orders.isEmpty()) {
                navigate("Products");
            } else {
                statusFilter.setSelectedIndex(0);
            }
        });

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);

        empty.add(button, gbc);

        return empty;
    }

    // ===================== Shopping Bag Icon =====================

    private static class ShoppingBagIcon
            extends JComponent {

        ShoppingBagIcon() {

            Dimension size = new Dimension(100, 100);

            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
        }

        @Override
        protected void paintComponent(Graphics gr) {

            Graphics2D g = g2(gr);

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            double scale = Math.min(
                    getWidth() / 100.0,
                    getHeight() / 100.0
            );

            g.translate(
                    (getWidth() - 100 * scale) / 2,
                    (getHeight() - 100 * scale) / 2
            );

            g.scale(scale, scale);

            g.setColor(PINK_100);
            g.fillOval(0, 0, 100, 100);

            g.setColor(PINK_600);

            g.setStroke(new BasicStroke(
                    2.8f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND
            ));

            g.drawRoundRect(
                    28, 37, 44, 40, 8, 8
            );

            g.drawArc(
                    39, 22, 22, 30, 0, 180
            );

            g.drawLine(39, 37, 39, 43);
            g.drawLine(61, 37, 61, 43);

            g.dispose();
        }
    }

    // ===================== Order Card =====================

    private JComponent buildOrderCard(Order order) {

        RoundedPanel card = new RoundedPanel(
                26,
                Color.WHITE,
                true
        );

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        int s = RoundedPanel.SH;

        card.setBorder(new EmptyBorder(
                s + 25,
                s + 30,
                s + 25,
                s + 30
        ));

        card.add(
                fullWidth(buildOrderHeader(order))
        );

        addDivider(card);

        addLeft(card, label(
                "Order Items (" + order.getItemCount() + ")",
                heading(23f),
                PINK_900
        ), 18);

        for (int i = 0; i < order.items.size(); i++) {

            OrderItem item = order.items.get(i);

            card.add(
                    fullWidth(buildProductRow(item))
            );

            if (i < order.items.size() - 1) {

                card.add(
                        Box.createVerticalStrut(12)
                );
            }
        }

        addDivider(card);

        JPanel information = new JPanel(
                new GridLayout(1, 2, 18, 0)
        );

        information.setOpaque(false);

        information.add(
                buildReceivingInfo(order)
        );

        information.add(
                buildPaymentInfo(order)
        );

        card.add(fullWidth(information));

        addDivider(card);

        JPanel totals = new JPanel();

        totals.setLayout(
                new BoxLayout(totals, BoxLayout.Y_AXIS)
        );

        totals.setOpaque(false);

        totals.add(fullWidth(summaryRow(
                "Subtotal",
                money(order.subtotal),
                false
        )));

        totals.add(Box.createVerticalStrut(12));

        totals.add(fullWidth(summaryRow(
                "Home Delivery".equals(order.receivingMethod)
                        ? "Delivery Fee"
                        : "Pickup Fee",
                money(order.deliveryFee),
                false
        )));

        totals.add(Box.createVerticalStrut(18));

        totals.add(fullWidth(summaryRow(
                "Order Total",
                money(order.total),
                true
        )));

        card.add(fullWidth(totals));

        card.add(Box.createVerticalStrut(25));

        card.add(
                fullWidth(buildOrderActions(order))
        );

        return card;
    }

    // ===================== Order Header =====================

    private JComponent buildOrderHeader(Order order) {

        JPanel header = new JPanel(
                new BorderLayout(20, 0)
        );

        header.setOpaque(false);

        JPanel left = new JPanel(
                new BorderLayout(0, 8)
        );

        left.setOpaque(false);

        left.add(label(
                "Order #" + order.id,
                heading(26f),
                PINK_900
        ), BorderLayout.NORTH);

        left.add(label(
                "Placed on " + formatDate(order.date),
                text(15f),
                PINK_600
        ), BorderLayout.CENTER);

        header.add(left, BorderLayout.CENTER);

        JPanel right = new JPanel(
                new GridBagLayout()
        );

        right.setOpaque(false);

        right.add(
                buildStatusBadge(order.status)
        );

        header.add(right, BorderLayout.EAST);

        return header;
    }

    // ===================== Status Badge =====================

    private JComponent buildStatusBadge(String status) {

        Color background;
        Color foreground;

        switch (status) {

            case "Pending":

                background = new Color(0xFFF1D6);
                foreground = new Color(0x956B20);

                break;

            case "Processing":

                background = PINK_100;
                foreground = PINK_800;

                break;

            case "Shipped":

                background = new Color(0xE5EFFB);
                foreground = new Color(0x365F91);

                break;

            case "Delivered":

                background = new Color(0xE2F2E5);
                foreground = new Color(0x397047);

                break;

            case "Cancelled":

                background = new Color(0xFBE4E4);
                foreground = new Color(0xA04444);

                break;

            default:

                background = PINK_100;
                foreground = PINK_800;

                break;
        }

        RoundedPanel badge = new RoundedPanel(
                22,
                background,
                false
        );

        badge.setLayout(new BorderLayout());

        badge.setBorder(
                new EmptyBorder(10, 22, 10, 22)
        );

        JLabel statusLabel = label(
                status,
                text(15f),
                foreground
        );

        statusLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        badge.add(
                statusLabel,
                BorderLayout.CENTER
        );

        return badge;
    }

    // ===================== Product Row =====================

    private JComponent buildProductRow(OrderItem item) {

        RoundedPanel row = new RoundedPanel(
                18,
                PINK_50,
                false
        );

        row.setLayout(new BorderLayout(18, 0));

        row.setBorder(
                new EmptyBorder(14, 18, 14, 18)
        );

        row.add(
                new OrderImage(item.image),
                BorderLayout.WEST
        );

        JPanel info = new JPanel();

        info.setLayout(
                new BoxLayout(info, BoxLayout.Y_AXIS)
        );

        info.setOpaque(false);

        addLeft(info, htmlLabel(
                item.name,
                350,
                "left",
                heading(21f),
                PINK_900
        ), 8);

        addLeft(info, label(
                "Quantity: " + item.quantity,
                text(15f),
                PINK_800
        ), 6);

        addLeft(info, label(
                money(item.price) + " each",
                text(14f),
                PINK_600
        ), 0);

        JPanel infoWrapper = new JPanel(
                new GridBagLayout()
        );

        infoWrapper.setOpaque(false);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        infoWrapper.add(info, gbc);

        row.add(
                infoWrapper,
                BorderLayout.CENTER
        );

        JPanel pricePanel = new JPanel(
                new GridBagLayout()
        );

        pricePanel.setOpaque(false);

        pricePanel.add(label(
                money(item.getTotal()),
                heading(22f),
                PINK_900
        ));

        row.add(
                pricePanel,
                BorderLayout.EAST
        );

        return row;
    }

    // ===================== Receiving Information =====================

    private JComponent buildReceivingInfo(Order order) {

        RoundedPanel panel = new RoundedPanel(
                18,
                PINK_50,
                false
        );

        panel.setLayout(
                new BoxLayout(panel, BoxLayout.Y_AXIS)
        );

        panel.setBorder(
                new EmptyBorder(22, 22, 22, 22)
        );

        addLeft(panel, label(
                "Receiving Details",
                heading(22f),
                PINK_900
        ), 15);

        addLeft(panel, label(
                order.receivingMethod,
                text(17f),
                PINK_800
        ), 10);

        if ("Home Delivery".equals(
                order.receivingMethod)) {

            JTextArea addressArea = createReadOnlyArea(
                    order.address.trim(),
                    14f,
                    3
            );

            panel.add(fullWidth(addressArea));

        } else {

            addLeft(panel, label(
                    "Glam & Go Salon",
                    text(15f),
                    PINK_800
            ), 8);

            addLeft(panel, label(
                    "Free Pickup",
                    text(14f),
                    PINK_600
            ), 0);
        }

        return panel;
    }

    // ===================== Payment Information =====================

    private JComponent buildPaymentInfo(Order order) {

        RoundedPanel panel = new RoundedPanel(
                18,
                PINK_50,
                false
        );

        panel.setLayout(
                new BoxLayout(panel, BoxLayout.Y_AXIS)
        );

        panel.setBorder(
                new EmptyBorder(22, 22, 22, 22)
        );

        addLeft(panel, label(
                "Payment Details",
                heading(22f),
                PINK_900
        ), 15);

        addLeft(panel, label(
                order.paymentMethod,
                text(16f),
                PINK_800
        ), 10);

        String paymentStatus;

        if ("Cancelled".equals(order.status)) {

            paymentStatus = "Order Cancelled";

        } else if (order.paymentMethod.contains("(Test)")) {

            paymentStatus = "Simulated Payment";

        } else {

            paymentStatus = "Payment on Receipt";
        }

        addLeft(panel, label(
                paymentStatus,
                text(14f),
                PINK_600
        ), 12);

        addLeft(panel, label(
                "Customer: " + order.customerName,
                text(14f),
                PINK_800
        ), 6);

        addLeft(panel, label(
                "Phone: " + order.phone,
                text(14f),
                PINK_800
        ), 0);

        return panel;
    }

    // ===================== Summary Row =====================

    private JComponent summaryRow(
            String title,
            String value,
            boolean total) {

        JPanel row = new JPanel(
                new BorderLayout(20, 0)
        );

        row.setOpaque(false);

        row.add(label(
                title,
                total ? heading(24f) : text(17f),
                total ? PINK_900 : PINK_800
        ), BorderLayout.WEST);

        row.add(label(
                value,
                total ? heading(25f) : text(17f),
                PINK_900
        ), BorderLayout.EAST);

        return row;
    }

    // ===================== Order Actions =====================

    private JComponent buildOrderActions(Order order) {

        JPanel actions = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        12,
                        0
                )
        );

        actions.setOpaque(false);

        PillButton detailsButton = new PillButton(
                "View Details",
                15f,
                24,
                11
        ).style(
                PINK_800,
                Color.WHITE,
                null,
                PINK_950
        );

        detailsButton.addActionListener(
                e -> showOrderDetails(order)
        );

        actions.add(detailsButton);

        if ("Pending".equals(order.status)) {

            PillButton cancelButton = new PillButton(
                    "Cancel Order",
                    15f,
                    24,
                    11
            ).style(
                    Color.WHITE,
                    PINK_800,
                    PINK_300,
                    PINK_100
            );

            cancelButton.addActionListener(
                    e -> cancelOrder(order)
            );

            actions.add(cancelButton);
        }

        return actions;
    }

    // ===================== Order Details Dialog =====================

    private void showOrderDetails(Order order) {

        JPanel details = vbox();

        addDialogRow(
                details, "ORDER", order.id
        );

        addDialogRow(
                details, "STATUS", order.status
        );

        addDialogRow(
                details, "DATE", formatDate(order.date)
        );

        addDialogRow(
                details, "CUSTOMER", order.customerName
        );

        addDialogRow(
                details, "PHONE", order.phone
        );

        addDialogRow(
                details, "RECEIVING", order.receivingMethod
        );

        addDialogRow(
                details, "PAYMENT", order.paymentMethod
        );

        if ("Home Delivery".equals(
                order.receivingMethod)) {

            addDialogRow(
                    details, "ADDRESS", order.address
            );
        }

        addDivider(details);

        for (OrderItem item : order.items) {

            addDialogRow(
                    details,
                    item.name + " x " + item.quantity,
                    money(item.getTotal())
            );
        }

        addDivider(details);

        addDialogRow(
                details,
                "SUBTOTAL",
                money(order.subtotal)
        );

        addDialogRow(
                details,
                "DELIVERY / PICKUP",
                money(order.deliveryFee)
        );

        addDialogRow(
                details,
                "TOTAL",
                money(order.total)
        );

        showStyledDialog(
                "Order Details",
                "Your Beauty Order",
                "Here are all your order details.",
                details,
                null,
                "Close"
        );
    }

    // ===================== Cancel Order =====================

    private void cancelOrder(Order order) {

        if (!"Pending".equals(order.status)) {

            showStyledDialog(
                    "Cancel Order",
                    "Unable to Cancel",
                    "Only pending orders can be cancelled.",
                    null,
                    null,
                    "Got It"
            );

            return;
        }

        JPanel details = vbox();

        addDialogRow(
                details,
                "ORDER",
                order.id
        );

        addDialogRow(
                details,
                "ITEMS",
                String.valueOf(order.getItemCount())
        );

        addDialogRow(
                details,
                "STATUS",
                order.status
        );

        addDialogRow(
                details,
                "TOTAL",
                money(order.total)
        );

        boolean confirmed = showStyledDialog(
                "Cancel Order",
                "Are You Sure?",
                "Please review your order before cancelling.",
                details,
                "Go Back",
                "Cancel Order"
        );

        if (!confirmed) {
            return;
        }

        order.status = "Cancelled";

        refreshOrders();

        JPanel cancelledDetails = vbox();

        addDialogRow(
                cancelledDetails,
                "ORDER",
                order.id
        );

        addDialogRow(
                cancelledDetails,
                "STATUS",
                "Cancelled"
        );

        addDialogRow(
                cancelledDetails,
                "TOTAL",
                money(order.total)
        );

        showStyledDialog(
                "Order Cancelled",
                "Order Cancelled!",
                "Your order has been cancelled successfully.",
                cancelledDetails,
                null,
                "Done"
        );
    }

    // ===================== Custom Styled Dialog =====================

    private boolean showStyledDialog(
            String windowTitle,
            String title,
            String subtitle,
            JPanel details,
            String backText,
            String confirmText) {

        final boolean[] confirmed = {false};

        JDialog dialog = new JDialog(
                this,
                windowTitle,
                true
        );

        dialog.setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        dialog.setResizable(false);

        JPanel root = new JPanel();

        root.setLayout(
                new BoxLayout(root, BoxLayout.Y_AXIS)
        );

        root.setBackground(Color.WHITE);

        root.setBorder(new EmptyBorder(
                38, 35, 35, 35
        ));

        // Dialog Heading

        JLabel titleLabel = label(
                title,
                heading(38f),
                PINK_950
        );

        titleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        root.add(fullWidth(titleLabel));

        root.add(Box.createVerticalStrut(15));

        JLabel subtitleLabel = htmlLabel(
                subtitle,
                440,
                "center",
                text(18f),
                PINK_800
        );

        subtitleLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        root.add(fullWidth(subtitleLabel));

        root.add(Box.createVerticalStrut(32));

        // Rounded Information Card

        if (details != null) {

            RoundedPanel card = new RoundedPanel(
                    22,
                    PINK_50,
                    true
            );

            card.setLayout(new BorderLayout());

            int shadow = RoundedPanel.SH;

            card.setBorder(new EmptyBorder(
                    shadow + 24,
                    shadow + 22,
                    shadow + 24,
                    shadow + 22
            ));

            details.setOpaque(false);

            card.add(
                    details,
                    BorderLayout.CENTER
            );

            root.add(fullWidth(card));

            root.add(Box.createVerticalStrut(30));
        }

        // Dialog Buttons

        JPanel buttons = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        15,
                        0
                )
        );

        buttons.setOpaque(false);

        if (backText != null) {

            PillButton backButton = new PillButton(
                    backText,
                    17f,
                    28,
                    13
            ).style(
                    PINK_100,
                    PINK_900,
                    null,
                    PINK_200
            );

            backButton.addActionListener(
                    e -> dialog.dispose()
            );

            buttons.add(backButton);
        }

        PillButton confirmButton = new PillButton(
                confirmText,
                17f,
                28,
                13
        ).style(
                PINK_800,
                Color.WHITE,
                null,
                PINK_950
        );

        confirmButton.addActionListener(e -> {

            confirmed[0] = true;

            dialog.dispose();
        });

        buttons.add(confirmButton);

        root.add(fullWidth(buttons));

        // Scrollable Content

        JPanel wrapper = new JPanel(
                new BorderLayout()
        );

        wrapper.setBackground(Color.WHITE);

        wrapper.add(
                root,
                BorderLayout.NORTH
        );

        JScrollPane scroll = new JScrollPane(
                wrapper,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setBorder(null);

        scroll.getViewport().setBackground(
                Color.WHITE
        );

        scroll.getVerticalScrollBar()
                .setUnitIncrement(20);

        dialog.setContentPane(scroll);

        Rectangle screen = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();

        int width = Math.min(
                650,
                screen.width - 40
        );

        root.setSize(
                Math.max(1, width - 40),
                Short.MAX_VALUE
        );

        root.doLayout();

        int height = Math.min(
                Math.max(
                        360,
                        root.getPreferredSize().height + 70
                ),
                screen.height - 60
        );

        dialog.setSize(width, height);

        dialog.setLocationRelativeTo(this);

        dialog.setVisible(true);

        return confirmed[0];
    }

    // ===================== Dialog Information Row =====================

    private void addDialogRow(
            JPanel panel,
            String title,
            String value) {

        JPanel row = new JPanel(
                new BorderLayout(18, 0)
        );

        row.setOpaque(false);

        row.setBorder(new EmptyBorder(
                8, 0, 8, 0
        ));

        JLabel titleLabel = htmlLabel(
                title,
                155,
                "left",
                text(15f),
                PINK_700
        );

        JLabel valueLabel = htmlLabel(
                escapeHtml(value),
                225,
                "right",
                text(16f),
                PINK_950
        );

        valueLabel.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        row.add(
                titleLabel,
                BorderLayout.WEST
        );

        row.add(
                valueLabel,
                BorderLayout.CENTER
        );

        panel.add(fullWidth(row));
    }

    // ===================== Escape HTML =====================

    private String escapeHtml(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\n", "<br>");
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

                new ShoppingCart().setVisible(true);
                dispose();

                break;

            case "Appointments":
            case "My Appointments":

                new MyAppointments().setVisible(true);
                dispose();

                break;

            case "My Orders":
            case "Orders":

                break;

            default:

                showStyledDialog(
                        "Glam & Go",
                        "Coming Soon!",
                        page + " page is not connected yet.",
                        null,
                        null,
                        "Close"
                );
        }
    }

    // ===================== Full Width Helper =====================

    private JComponent fullWidth(JComponent component) {

        JPanel wrapper = new JPanel(
                new BorderLayout()
        ) {

            @Override
            public Dimension getMaximumSize() {

                return new Dimension(
                        Integer.MAX_VALUE,
                        getPreferredSize().height
                );
            }
        };

        wrapper.setOpaque(false);

        wrapper.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        wrapper.add(
                component,
                BorderLayout.CENTER
        );

        return wrapper;
    }

    // ===================== Read Only Text Area =====================

    private JTextArea createReadOnlyArea(
            String value,
            float fontSize,
            int rows) {

        JTextArea area = new JTextArea(value);

        area.setFont(text(fontSize));
        area.setForeground(PINK_800);
        area.setOpaque(false);
        area.setEditable(false);
        area.setFocusable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setRows(rows);
        area.setColumns(22);
        area.setBorder(null);

        return area;
    }

    // ===================== Add Left Helper =====================

    private void addLeft(
            JPanel panel,
            JComponent component,
            int gap) {

        component.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(component);

        if (gap > 0) {

            panel.add(
                    Box.createVerticalStrut(gap)
            );
        }
    }

    // ===================== Divider =====================

    private void addDivider(JPanel panel) {

        panel.add(
                Box.createVerticalStrut(22)
        );

        JSeparator separator = new JSeparator();

        separator.setForeground(PINK_300);
        separator.setBackground(PINK_300);

        separator.setPreferredSize(
                new Dimension(1, 1)
        );

        panel.add(fullWidth(separator));

        panel.add(
                Box.createVerticalStrut(22)
        );
    }

    // ===================== Money Format =====================

    private String money(double amount) {

        return String.format(
                Locale.US,
                "SAR %.2f",
                amount
        );
    }

    // ===================== Date Format =====================

    private String formatDate(LocalDateTime date) {

        return date.format(
                DateTimeFormatter.ofPattern(
                        "dd MMM yyyy, hh:mm a",
                        Locale.ENGLISH
                )
        );
    }

    // ===================== Product Image =====================

    private static class OrderImage extends JComponent {

        private final String path;

        OrderImage(String path) {

            this.path = path;

            setPreferredSize(
                    new Dimension(90, 100)
            );

            setMinimumSize(
                    new Dimension(90, 100)
            );
        }

        @Override
        protected void paintComponent(Graphics gr) {

            Graphics2D g = g2(gr);

            int w = getWidth();
            int h = getHeight();

            g.clip(new RoundRectangle2D.Double(
                    0,
                    0,
                    w,
                    h,
                    16,
                    16
            ));

            g.setColor(PINK_100);

            g.fillRect(0, 0, w, h);

            BufferedImage image = loadImage(path);

            if (image != null) {

                drawCover(
                        g,
                        image,
                        0,
                        0,
                        w,
                        h
                );

            } else {

                g.setColor(PINK_600);

                g.setFont(text(12f));

                FontMetrics fm = g.getFontMetrics();

                String placeholder = "Product";

                int x = (
                        w - fm.stringWidth(placeholder)
                ) / 2;

                int y = (
                        h + fm.getAscent()
                        - fm.getDescent()
                ) / 2;

                g.drawString(
                        placeholder,
                        x,
                        y
                );
            }

            g.dispose();
        }
    }

    // ===================== Main =====================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->

                new MyOrders().setVisible(true)

        );
    }
}
