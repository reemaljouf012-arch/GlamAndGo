
package glamandgo;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import static glamandgo.Theme.*;

public class AdminDashboard extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    private final Color DARK = PINK_950;
    private final Color MUTED = PINK_700;
    private final Color PAGE_BG = new Color(0xFFFAFC);

    private final JLabel pageTitle =
            label("Overview", heading(32), DARK);

    private final JLabel pageSubtitle =
            label("Welcome to your salon operations dashboard.",
                    text(15), MUTED);

    private final JPanel sidebar = new JPanel();

    private final List<PillButton> navigationButtons =
            new ArrayList<>();

    private JTable appointmentsTable;
    private DefaultTableModel appointmentsModel;

    private JTable employeesTable;
    private JTable servicesTable;
    private JTable productsTable;

    private JLabel appointmentsCount;
    private JLabel employeesCount;
    private JLabel servicesCount;
    private JLabel productsCount;

    private String currentPage = "Overview";

    // ===================== Constructor =====================

    public AdminDashboard() {

        setTitle("Glam & Go | Admin Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1350, 850);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(buildMainArea(), BorderLayout.CENTER);

        buildPages();
        showPage("Overview");
    }

    // ===================== Main Layout =====================

    private JComponent buildSidebar() {

        sidebar.setLayout(
                new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        sidebar.setBackground(DARK);
        sidebar.setPreferredSize(new Dimension(255, 850));
        sidebar.setBorder(
                new EmptyBorder(30, 20, 25, 20));

        JLabel logo = label(
                "Glam & Go",
                heading(29),
                PINK_50
        );

        JLabel adminLabel = label(
                "ADMIN PANEL",
                text(11),
                PINK_300
        );

        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        adminLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(adminLabel);
        sidebar.add(Box.createVerticalStrut(45));

        JLabel menuLabel = label(
                "WORKSPACE",
                text(11),
                PINK_400
        );

        menuLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(menuLabel);
        sidebar.add(Box.createVerticalStrut(14));

        addNavigationButton("Overview");
        addNavigationButton("Appointments");
        addNavigationButton("Employees");
        addNavigationButton("Services");
        addNavigationButton("Products");

        sidebar.add(Box.createVerticalGlue());

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(
                new BoxLayout(bottom, BoxLayout.Y_AXIS));

        JLabel brand = label(
                "BEAUTY IN EVERY DETAIL",
                text(10),
                PINK_300
        );

        JLabel version = label(
                "Glam & Go  |  Admin",
                text(12),
                PINK_100
        );

        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        version.setAlignmentX(Component.LEFT_ALIGNMENT);

        bottom.add(brand);
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(version);

        sidebar.add(bottom);

        return sidebar;
    }

    private void addNavigationButton(String name) {

        PillButton button = new PillButton(
                name, 15, 18, 13
        );

        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(
                new Dimension(215, 48));
        button.setPreferredSize(
                new Dimension(215, 48));

        styleNavigationButton(button, false);

        button.addActionListener(e -> showPage(name));

        navigationButtons.add(button);
        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(9));
    }

    private void styleNavigationButton(
            PillButton button, boolean selected) {

        if (selected) {
            button.style(
                    PINK_300,
                    DARK,
                    null,
                    PINK_200
            );
        } else {
            button.style(
                    null,
                    PINK_100,
                    null,
                    PINK_800
            );
        }
    }

    // ===================== Main Area =====================

    private JComponent buildMainArea() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(PAGE_BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(
                new EmptyBorder(24, 38, 22, 38));

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(
                new BoxLayout(titleGroup, BoxLayout.Y_AXIS));

        pageTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        pageSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleGroup.add(pageTitle);
        titleGroup.add(Box.createVerticalStrut(5));
        titleGroup.add(pageSubtitle);

        JLabel adminBadge = label(
                "ADMIN",
                text(13),
                PINK_800
        );

        RoundedPanel badge = new RoundedPanel(
                30, PINK_50, false);

        badge.setLayout(new GridBagLayout());
        badge.setBorder(new EmptyBorder(7, 18, 7, 18));
        badge.add(adminBadge);

        header.add(titleGroup, BorderLayout.WEST);
        header.add(badge, BorderLayout.EAST);

        contentPanel.setBackground(PAGE_BG);

        JScrollPane scroll = new JScrollPane(
                contentPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setBorder(null);
        scroll.getViewport().setBackground(PAGE_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(22);

        main.add(header, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);

        return main;
    }

    // ===================== Pages =====================

    private void buildPages() {

        contentPanel.add(buildOverviewPage(), "Overview");
        contentPanel.add(buildAppointmentsPage(), "Appointments");
        contentPanel.add(buildEmployeesPage(), "Employees");
        contentPanel.add(buildServicesPage(), "Services");
        contentPanel.add(buildProductsPage(), "Products");
    }

    private void showPage(String page) {

        currentPage = page;

        cardLayout.show(contentPanel, page);

        pageTitle.setText(page);

        switch (page) {

            case "Overview":
                pageSubtitle.setText(
                        "Welcome to your salon operations dashboard.");
                break;

            case "Appointments":
                pageSubtitle.setText(
                        "View and manage customer appointments.");
                refreshAppointmentsTable();
                break;

            case "Employees":
                pageSubtitle.setText(
                        "View the beauty experts in your salon.");
                break;

            case "Services":
                pageSubtitle.setText(
                        "Explore the services offered by your salon.");
                break;

            case "Products":
                pageSubtitle.setText(
                        "Monitor your product catalog and stock.");
                break;
        }

        for (PillButton button : navigationButtons) {
            styleNavigationButton(
                    button,
                    button.getText().equals(page)
            );
        }
    }

    // ===================== Overview =====================

    private JComponent buildOverviewPage() {

        JPanel page = pageContainer();

        JPanel welcome = new JPanel();
        welcome.setLayout(
                new BoxLayout(welcome, BoxLayout.Y_AXIS));
        welcome.setBackground(PINK_50);
        welcome.setBorder(
                new EmptyBorder(27, 30, 27, 30));

        JLabel welcomeTitle = label(
                "Your Salon at a Glance",
                heading(25),
                DARK
        );

        JLabel welcomeText = label(
                "Manage daily operations from one place.",
                text(15),
                MUTED
        );

        welcomeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        welcomeText.setAlignmentX(Component.LEFT_ALIGNMENT);

        welcome.add(welcomeTitle);
        welcome.add(Box.createVerticalStrut(8));
        welcome.add(welcomeText);

        page.add(welcome);
        page.add(Box.createVerticalStrut(25));

        JPanel stats = new JPanel(
                new GridLayout(1, 4, 15, 15));
        stats.setOpaque(false);

        appointmentsCount = new JLabel();
        employeesCount = new JLabel();
        servicesCount = new JLabel();
        productsCount = new JLabel();

        stats.add(buildStatCard(
                "Appointments",
                appointmentsCount,
                "Bookings recorded",
                "01"
        ));

        stats.add(buildStatCard(
                "Employees",
                employeesCount,
                "Beauty specialists",
                "02"
        ));

        stats.add(buildStatCard(
                "Services",
                servicesCount,
                "Available services",
                "03"
        ));

        stats.add(buildStatCard(
                "Products",
                productsCount,
                "Catalog items",
                "04"
        ));

        page.add(stats);
        page.add(Box.createVerticalStrut(30));

        JLabel sectionTitle = label(
                "Quick Access",
                heading(23),
                DARK
        );

        page.add(sectionTitle);
        page.add(Box.createVerticalStrut(15));

        JPanel quickAccess = new JPanel(
                new GridLayout(1, 3, 15, 15));
        quickAccess.setOpaque(false);

        quickAccess.add(buildQuickCard(
                "Appointments",
                "Review bookings and update their status.",
                "Manage Appointments",
                () -> showPage("Appointments")
        ));

        quickAccess.add(buildQuickCard(
                "Employees",
                "View the specialists working at your salon.",
                "View Employees",
                () -> showPage("Employees")
        ));

        quickAccess.add(buildQuickCard(
                "Products",
                "Check the products and their stock levels.",
                "View Products",
                () -> showPage("Products")
        ));

        page.add(quickAccess);
        page.add(Box.createVerticalStrut(30));

        JLabel note = label(
                "Dashboard figures are based on the current in-app data.",
                text(13),
                PINK_700
        );

        page.add(note);

        refreshStatistics();

        return page;
    }

    private JPanel buildStatCard(
            String title,
            JLabel value,
            String description,
            String number) {

        RoundedPanel card = new RoundedPanel(
                20, Color.WHITE, true);

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setBorder(
                new EmptyBorder(20, 18, 20, 18));

        JLabel index = label(
                number,
                text(12),
                PINK_500
        );

        JLabel heading = label(
                title,
                text(14),
                MUTED
        );

        value.setFont(heading(31));
        value.setForeground(DARK);

        JLabel detail = label(
                description,
                text(12),
                PINK_600
        );

        for (JComponent c : new JComponent[]{
                index, heading, value, detail
        }) {
            c.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        card.add(index);
        card.add(Box.createVerticalStrut(13));
        card.add(heading);
        card.add(Box.createVerticalStrut(5));
        card.add(value);
        card.add(Box.createVerticalStrut(5));
        card.add(detail);

        return card;
    }

    private JPanel buildQuickCard(
            String title,
            String description,
            String buttonText,
            Runnable action) {

        RoundedPanel card = new RoundedPanel(
                20, Color.WHITE, true);

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setBorder(
                new EmptyBorder(22, 20, 22, 20));

        JLabel heading = label(
                title,
                heading(21),
                DARK
        );

        JLabel desc = htmlLabel(
                description,
                210,
                "left",
                text(14),
                MUTED
        );

        PillButton button = new PillButton(
                buttonText, 13, 17, 9
        );

        button.style(
                PINK_800,
                Color.WHITE,
                null,
                PINK_900
        );

        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.addActionListener(e -> action.run());

        card.add(heading);
        card.add(Box.createVerticalStrut(12));
        card.add(desc);
        card.add(Box.createVerticalStrut(22));
        card.add(button);

        return card;
    }

    private void refreshStatistics() {

        if (appointmentsCount == null) {
            return;
        }

        appointmentsCount.setText(
                String.valueOf(MyAppointments.appointments.size()));

        employeesCount.setText("8");
        servicesCount.setText("11");
        productsCount.setText(
                String.valueOf(PRODUCTS.length));
    }

    // ===================== Appointments =====================

    private JComponent buildAppointmentsPage() {

        JPanel page = pageContainer();

        JPanel intro = sectionHeader(
                "Appointment Management",
                "Review bookings and update their current status."
        );

        page.add(intro);
        page.add(Box.createVerticalStrut(22));

        appointmentsModel = new DefaultTableModel(
                new Object[]{
                    "Service",
                    "Specialist",
                    "Date",
                    "Time",
                    "Status"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        appointmentsTable = createTable(appointmentsModel);

        page.add(buildTablePanel(appointmentsTable));
        page.add(Box.createVerticalStrut(18));

        JPanel actions = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);

        PillButton complete = new PillButton(
                "Mark as Completed", 13, 18, 10
        );

        complete.style(
                PINK_800, Color.WHITE, null, PINK_900);

        PillButton cancel = new PillButton(
                "Cancel Appointment", 13, 18, 10
        );

        cancel.style(
                Color.WHITE, PINK_800, PINK_300, PINK_100);

        PillButton refresh = new PillButton(
                "Refresh", 13, 18, 10
        );

        refresh.style(
                PINK_100, PINK_900, null, PINK_200);

        complete.addActionListener(
                e -> updateSelectedAppointment("Completed"));

        cancel.addActionListener(
                e -> updateSelectedAppointment("Cancelled"));

        refresh.addActionListener(
                e -> refreshAppointmentsTable());

        actions.add(complete);
        actions.add(cancel);
        actions.add(refresh);

        page.add(actions);

        refreshAppointmentsTable();

        return page;
    }

    private void refreshAppointmentsTable() {

        if (appointmentsModel == null) {
            return;
        }

        appointmentsModel.setRowCount(0);

        for (MyAppointments.Appointment appointment
                : MyAppointments.appointments) {

            appointmentsModel.addRow(new Object[]{
                appointment.serviceName,
                appointment.employeeName,
                appointment.date,
                appointment.time,
                appointment.status
            });
        }

        appointmentsModel.fireTableDataChanged();
        refreshStatistics();
    }

    private void updateSelectedAppointment(String newStatus) {

        int selected = appointmentsTable.getSelectedRow();

        if (selected < 0) {
            showMessage(
                    "Please select an appointment first.",
                    "No Appointment Selected"
            );
            return;
        }

        int modelRow =
                appointmentsTable.convertRowIndexToModel(selected);

        if (modelRow >= MyAppointments.appointments.size()) {
            return;
        }

        MyAppointments.Appointment appointment =
                MyAppointments.appointments.get(modelRow);

        if ("Cancelled".equals(appointment.status)
                || "Completed".equals(appointment.status)) {

            showMessage(
                    "This appointment has already been "
                    + appointment.status.toLowerCase() + ".",
                    "Status Unchanged"
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Change this appointment status to "
                + newStatus + "?",
                "Confirm Status Update",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            appointment.status = newStatus;

            refreshAppointmentsTable();

            showMessage(
                    "Appointment status updated successfully.",
                    "Update Successful"
            );
        }
    }

    // ===================== Employees =====================

    private JComponent buildEmployeesPage() {

        JPanel page = pageContainer();

        page.add(sectionHeader(
                "Employee Directory",
                "The beauty specialists currently listed in the application."
        ));

        page.add(Box.createVerticalStrut(22));

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{
                    "Employee",
                    "Specialty",
                    "Rating",
                    "Reviews"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        String[][] employees = {
            {"Sarah", "Hair Specialist", "4.9", "120"},
            {"Nora", "Hair Stylist", "4.8", "95"},
            {"Lama", "Skincare Specialist", "4.9", "110"},
            {"Maya", "Beauty Specialist", "4.8", "87"},
            {"Reem", "Makeup Artist", "4.9", "135"},
            {"Hala", "Makeup Artist", "4.8", "102"},
            {"Dana", "Nail Technician", "4.9", "98"},
            {"Joud", "Nail Technician", "4.8", "90"}
        };

        for (String[] employee : employees) {
            model.addRow(employee);
        }

        employeesTable = createTable(model);

        page.add(buildTablePanel(employeesTable));

        page.add(Box.createVerticalStrut(15));

        page.add(label(
                "Employee information is currently displayed from the "
                + "existing Employees page data.",
                text(13),
                MUTED
        ));

        return page;
    }

    // ===================== Services =====================

    private JComponent buildServicesPage() {

        JPanel page = pageContainer();

        page.add(sectionHeader(
                "Service Catalog",
                "Services and prices currently listed in the application."
        ));

        page.add(Box.createVerticalStrut(22));

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{
                    "Service",
                    "Category",
                    "Price"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        String[][] services = {
            {"Hair Styling", "Hair Care", "SAR 120"},
            {"Hair Treatment", "Hair Care", "SAR 180"},
            {"Hair Coloring", "Hair Care", "SAR 250"},
            {"Facial Treatment", "Skincare", "SAR 150"},
            {"Deep Cleansing", "Skincare", "SAR 130"},
            {"Skin Hydration", "Skincare", "SAR 170"},
            {"Classic Makeup", "Makeup", "SAR 200"},
            {"Evening Makeup", "Makeup", "SAR 280"},
            {"Manicure", "Nails", "SAR 90"},
            {"Pedicure", "Nails", "SAR 110"},
            {"Nail Art", "Nails", "SAR 140"}
        };

        for (String[] service : services) {
            model.addRow(service);
        }

        servicesTable = createTable(model);

        page.add(buildTablePanel(servicesTable));

        page.add(Box.createVerticalStrut(15));

        page.add(label(
                "Service information is currently displayed from the "
                + "existing Services page data.",
                text(13),
                MUTED
        ));

        return page;
    }

    // ===================== Products =====================

    private JComponent buildProductsPage() {

        JPanel page = pageContainer();

        page.add(sectionHeader(
                "Product Inventory",
                "Review the products and stock quantities in the catalog."
        ));

        page.add(Box.createVerticalStrut(22));

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{
                    "Product",
                    "Category",
                    "Price",
                    "Stock",
                    "Sold"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        for (Product product : PRODUCTS) {

            model.addRow(new Object[]{
                product.name,
                product.category,
                String.format(
                        java.util.Locale.US,
                        "SAR %.2f",
                        product.price
                ),
                product.quantity,
                product.salesCount
            });
        }

        productsTable = createTable(model);

        page.add(buildTablePanel(productsTable));

        page.add(Box.createVerticalStrut(15));

        page.add(buildInventorySummary());

        return page;
    }

    private JComponent buildInventorySummary() {

        int total = PRODUCTS.length;
        int outOfStock = 0;
        int lowStock = 0;

        for (Product product : PRODUCTS) {

            if (product.quantity == 0) {
                outOfStock++;
            } else if (product.quantity <= 5) {
                lowStock++;
            }
        }

        RoundedPanel summary = new RoundedPanel(
                18, PINK_50, false);

        summary.setLayout(new FlowLayout(
                FlowLayout.LEFT, 25, 16));

        summary.add(inventoryLabel(
                "Total products: " + total));

        summary.add(inventoryLabel(
                "Low stock: " + lowStock));

        summary.add(inventoryLabel(
                "Out of stock: " + outOfStock));

        return summary;
    }

    private JLabel inventoryLabel(String value) {

        return label(
                value,
                text(14),
                PINK_800
        );
    }

    // ===================== Shared UI Helpers =====================

    private JPanel pageContainer() {

        JPanel page = new JPanel();
        page.setLayout(
                new BoxLayout(page, BoxLayout.Y_AXIS));

        page.setBackground(PAGE_BG);
        page.setBorder(
                new EmptyBorder(30, 35, 35, 35));

        return page;
    }

    private JPanel sectionHeader(
            String title,
            String subtitle) {

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(
                new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel heading = label(
                title,
                heading(23),
                DARK
        );

        JLabel description = label(
                subtitle,
                text(14),
                MUTED
        );

        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(heading);
        panel.add(Box.createVerticalStrut(7));
        panel.add(description);

        return panel;
    }

    private JTable createTable(DefaultTableModel model) {

        JTable table = new JTable(model);

        table.setFont(text(14));
        table.setForeground(DARK);
        table.setBackground(Color.WHITE);
        table.setRowHeight(43);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(PINK_100);
        table.setSelectionForeground(DARK);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        table.getTableHeader().setFont(
                text(13));

        table.getTableHeader().setForeground(
                PINK_800);

        table.getTableHeader().setBackground(
                PINK_50);

        table.getTableHeader().setPreferredSize(
                new Dimension(100, 42));

        table.getTableHeader().setReorderingAllowed(false);

        return table;
    }

    private JComponent buildTablePanel(JTable table) {

        RoundedPanel panel = new RoundedPanel(
                18, Color.WHITE, true);

        panel.setLayout(new BorderLayout());
        panel.setBorder(
                new EmptyBorder(12, 12, 12, 12));

        JScrollPane scroll = new JScrollPane(table);

        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);

        panel.add(scroll, BorderLayout.CENTER);

        panel.setPreferredSize(
                new Dimension(900, 390));

        panel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 430));

        return panel;
    }

    private void showMessage(String message, String title) {

        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ===================== Main =====================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new AdminDashboard().setVisible(true)
        );
    }
}
