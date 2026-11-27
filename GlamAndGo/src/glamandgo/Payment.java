
package glamandgo;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import static glamandgo.Theme.*;

public class Payment extends JFrame {

    private static final double DELIVERY_FEE = 20.0;

    // ===================== Saved Cards =====================

    private static final ArrayList<SavedCard> savedCards =
            new ArrayList<>();

    private SavedCard selectedCard;

    private final JPanel savedCardsPanel = vbox();

    private static class SavedCard {

        final String holderName;
        final String cardType;
        final String lastFour;
        final String expiryDate;

        SavedCard(String holderName, String cardType,
                String lastFour, String expiryDate) {

            this.holderName = holderName;
            this.cardType = cardType;
            this.lastFour = lastFour;
            this.expiryDate = expiryDate;
        }
    }

    // ===================== Customer Information =====================

    private final JTextField nameField = new JTextField();
    private final JTextField phoneField = new JTextField();

    // ===================== Delivery Information =====================

    private final JTextField cityField = new JTextField();
    private final JTextField districtField = new JTextField();
    private final JTextField streetField = new JTextField();

    private final JTextArea addressField =
            new JTextArea(3, 20);

    // ===================== Receiving Method =====================

    private final JRadioButton deliveryRadio =
            new JRadioButton("Home Delivery", true);

    private final JRadioButton pickupRadio =
            new JRadioButton("Salon Pickup");

    // ===================== Payment Method =====================

    private final JRadioButton cardRadio =
            new JRadioButton("Credit / Debit Card", true);

    private final JRadioButton cashRadio =
            new JRadioButton("Cash on Delivery");

    private final JPanel addressPanel = vbox();
    private final JPanel pickupPanel = vbox();
    private final JPanel cardPanel = vbox();

    // ===================== Order Summary =====================

    private final JLabel subtotalLabel =
            label("", text(17f), PINK_800);

    private final JLabel deliveryLabel =
            label("", text(17f), PINK_800);

    private final JLabel totalLabel =
            label("", heading(24f), PINK_900);

    private final JLabel feeTitle =
            label("Delivery Fee", text(17f), PINK_800);

    private final PillButton placeOrderButton =
            new PillButton("Place Order", 17f, 25, 12)
                    .style(PINK_800, Color.WHITE, null, PINK_950);

    // ===================== Constructor =====================

    public Payment() {

        setTitle("Glam & Go | Payment");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(1280, 820);

        setMinimumSize(new Dimension(1100, 650));

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        if (!savedCards.isEmpty()) {
            selectedCard = savedCards.get(0);
        }

        add(navbar(this::navigate), BorderLayout.NORTH);

        PageBody body = new PageBody();

        body.add(buildHero());
        body.add(buildCheckout());
        body.add(footer(this::navigate));

        JScrollPane scroll = new JScrollPane(
                body,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setBorder(null);

        scroll.getVerticalScrollBar()
                .setUnitIncrement(24);

        add(scroll, BorderLayout.CENTER);

        deliveryRadio.addActionListener(
                e -> updateReceivingMethod()
        );

        pickupRadio.addActionListener(
                e -> updateReceivingMethod()
        );

        cardRadio.addActionListener(
                e -> updatePaymentMethod()
        );

        cashRadio.addActionListener(
                e -> updatePaymentMethod()
        );

        placeOrderButton.addActionListener(
                e -> placeOrder()
        );

        updateReceivingMethod();
        updatePaymentMethod();
    }

    // ===================== Hero =====================

    private JComponent buildHero() {

        JPanel content = vbox();

        content.add(sectionTitle(
                "Checkout",
                "Complete your beauty purchase with confidence.",
                PINK_900,
                PINK_800
        ));

        return new Section(
                PINK_50, 48, 1200, content
        );
    }

    // ===================== Checkout Layout =====================

    private JComponent buildCheckout() {

        JPanel content = new JPanel(
                new BorderLayout(28, 0)
        );

        content.setOpaque(false);

        content.add(
                buildPaymentForm(),
                BorderLayout.CENTER
        );

        JPanel summaryWrapper = new JPanel(
                new BorderLayout()
        );

        summaryWrapper.setOpaque(false);

        summaryWrapper.setPreferredSize(
                new Dimension(380, 500)
        );

        summaryWrapper.add(
                buildOrderSummary(),
                BorderLayout.NORTH
        );

        content.add(
                summaryWrapper,
                BorderLayout.EAST
        );

        return new Section(
                PINK_50, 35, 1200, content
        );
    }

    // ===================== Payment Form =====================

    private JComponent buildPaymentForm() {

        RoundedPanel form = new RoundedPanel(
                28, Color.WHITE, true
        );

        form.setLayout(
                new BoxLayout(form, BoxLayout.Y_AXIS)
        );

        int s = RoundedPanel.SH;

        form.setBorder(new EmptyBorder(
                s + 30,
                s + 30,
                s + 30,
                s + 30
        ));

        // Customer Information

        addLeft(form, label(
                "Customer Information",
                heading(27f),
                PINK_900
        ), 25);

        addField(form, "Full Name", nameField);

        addField(form, "Phone Number", phoneField);

        addDivider(form);

        // Receiving Method

        addLeft(form, label(
                "Receiving Method",
                heading(27f),
                PINK_900
        ), 20);

        ButtonGroup receivingGroup = new ButtonGroup();

        receivingGroup.add(deliveryRadio);
        receivingGroup.add(pickupRadio);

        form.add(buildOption(
                deliveryRadio,
                "Receive your products at home."
        ));

        form.add(Box.createVerticalStrut(12));

        form.add(buildOption(
                pickupRadio,
                "Collect your products from our salon."
        ));

        form.add(Box.createVerticalStrut(25));

        buildAddressPanel();

        form.add(addressPanel);

        buildPickupPanel();

        form.add(pickupPanel);

        addDivider(form);

        // Payment Method

        addLeft(form, label(
                "Payment Method",
                heading(27f),
                PINK_900
        ), 20);

        ButtonGroup paymentGroup = new ButtonGroup();

        paymentGroup.add(cardRadio);
        paymentGroup.add(cashRadio);

        form.add(buildOption(
                cardRadio,
                "Select or add a payment card."
        ));

        form.add(Box.createVerticalStrut(12));

        buildCardPanel();

        form.add(cardPanel);

        form.add(Box.createVerticalStrut(15));

        form.add(buildOption(
                cashRadio,
                "Pay when you receive your order."
        ));

        return form;
    }

    // ===================== Delivery Address =====================

    private void buildAddressPanel() {

        addressPanel.setOpaque(false);

        addressPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        addLeft(addressPanel, label(
                "Delivery Address",
                heading(23f),
                PINK_900
        ), 20);

        addField(addressPanel, "City", cityField);

        addField(addressPanel, "District", districtField);

        addField(addressPanel, "Street", streetField);

        addLeft(addressPanel, label(
                "Address Details",
                text(16f),
                PINK_800
        ), 8);

        addressField.setFont(text(15f));

        addressField.setForeground(PINK_950);

        addressField.setBackground(PINK_50);

        addressField.setLineWrap(true);

        addressField.setWrapStyleWord(true);

        addressField.setBorder(
                new EmptyBorder(10, 12, 10, 12)
        );

        JScrollPane addressScroll =
                new JScrollPane(addressField);

        addressScroll.setBorder(
                BorderFactory.createLineBorder(PINK_300)
        );

        addressScroll.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 100)
        );

        addressScroll.setPreferredSize(
                new Dimension(400, 100)
        );

        addressScroll.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        addressPanel.add(addressScroll);

        addressPanel.add(Box.createVerticalStrut(20));
    }

    // ===================== Salon Pickup =====================

    private void buildPickupPanel() {

        pickupPanel.setOpaque(false);

        pickupPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        RoundedPanel info = new RoundedPanel(
                18, PINK_50, false
        );

        info.setLayout(
                new BoxLayout(info, BoxLayout.Y_AXIS)
        );

        info.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        addLeft(info, label(
                "Glam & Go Salon",
                heading(22f),
                PINK_900
        ), 10);

        addLeft(info, htmlLabel(
                "Your order will be prepared for collection "
                + "from our salon.",
                380,
                "left",
                text(15f),
                PINK_800
        ), 10);

        addLeft(info, label(
                "Free Pickup",
                heading(17f),
                PINK_800
        ), 0);

        info.setAlignmentX(Component.LEFT_ALIGNMENT);

        pickupPanel.add(info);
    }

    // ===================== Saved Cards Panel =====================

    private void buildCardPanel() {

        cardPanel.setOpaque(false);

        cardPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        RoundedPanel container = new RoundedPanel(
                18, PINK_50, false
        );

        container.setLayout(
                new BoxLayout(container, BoxLayout.Y_AXIS)
        );

        container.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        container.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        addLeft(container, label(
                "Saved Cards",
                heading(23f),
                PINK_900
        ), 18);

        savedCardsPanel.setOpaque(false);

        savedCardsPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        container.add(savedCardsPanel);

        container.add(Box.createVerticalStrut(15));

        PillButton addCardButton = new PillButton(
                "+ Add New Card",
                16f,
                22,
                10
        ).style(
                Color.WHITE,
                PINK_800,
                PINK_300,
                PINK_100
        );

        addCardButton.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        addCardButton.addActionListener(
                e -> showAddCardDialog()
        );

        container.add(addCardButton);

        container.add(Box.createVerticalStrut(15));

        addLeft(container, htmlLabel(
                "For testing purposes only. "
                + "Use fictional card details.",
                380,
                "left",
                text(13f),
                PINK_800
        ), 0);

        cardPanel.add(container);

        refreshSavedCards();
    }

    // ===================== Refresh Saved Cards =====================

    private void refreshSavedCards() {

        savedCardsPanel.removeAll();

        if (savedCards.isEmpty()) {

            selectedCard = null;

            addLeft(savedCardsPanel, label(
                    "No saved cards yet.",
                    text(16f),
                    PINK_800
            ), 8);

            addLeft(savedCardsPanel, label(
                    "Add a card to continue.",
                    text(14f),
                    PINK_600
            ), 5);

        } else {

            if (!savedCards.contains(selectedCard)) {
                selectedCard = savedCards.get(0);
            }

            ButtonGroup cardsGroup = new ButtonGroup();

            for (SavedCard card : savedCards) {

                savedCardsPanel.add(
                        buildSavedCard(card, cardsGroup)
                );

                savedCardsPanel.add(
                        Box.createVerticalStrut(12)
                );
            }
        }

        savedCardsPanel.revalidate();
        savedCardsPanel.repaint();

        revalidate();
        repaint();
    }

    // ===================== Single Saved Card =====================

    private JComponent buildSavedCard(
            SavedCard card,
            ButtonGroup group) {

        RoundedPanel cardBox = new RoundedPanel(
                16, Color.WHITE, false
        );

        cardBox.setLayout(
                new BorderLayout(15, 0)
        );

        cardBox.setBorder(
                new EmptyBorder(18, 15, 18, 15)
        );

        cardBox.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        // Card Selection

        JRadioButton select = new JRadioButton();

        select.setOpaque(false);

        select.setFocusPainted(false);

        select.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        group.add(select);

        select.setSelected(card == selectedCard);

        select.addActionListener(
                e -> selectedCard = card
        );

        cardBox.add(select, BorderLayout.WEST);

        // Card Information

        JPanel info = vbox();

        addLeft(info, label(
                card.cardType
                + "  \u2022\u2022\u2022\u2022  "
                + card.lastFour,
                heading(20f),
                PINK_900
        ), 8);

        addLeft(info, label(
                card.holderName,
                text(15f),
                PINK_800
        ), 6);

        addLeft(info, label(
                "Expires " + card.expiryDate,
                text(13f),
                PINK_600
        ), 5);

        addLeft(info, label(
                "Test Card",
                text(12f),
                PINK_600
        ), 0);

        cardBox.add(info, BorderLayout.CENTER);

        // Remove Card

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

        remove.addActionListener(
                e -> removeSavedCard(card)
        );

        cardBox.add(remove, BorderLayout.EAST);

        return fixedHeight(cardBox);
    }

    // ===================== Add New Card Dialog =====================

    private void showAddCardDialog() {

        JDialog dialog = new JDialog(
                this,
                "Glam & Go | Add New Card",
                true
        );

        dialog.setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        dialog.setResizable(false);

        JPanel root = new JPanel(new GridBagLayout());

        root.setBackground(Color.WHITE);

        root.setBorder(
                new EmptyBorder(25, 30, 25, 30)
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 12, 0);

        // ===================== Title =====================

        JLabel title = label(
                "Add New Card",
                heading(30f),
                PINK_900
        );

        title.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        root.add(title, gbc);

        gbc.gridy++;

        JLabel subtitle = label(
                "Enter your test card details.",
                text(15f),
                PINK_800
        );

        subtitle.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        root.add(subtitle, gbc);

        // ===================== Cardholder Name =====================

        JTextField holderField = new JTextField();

        setNameFilter(holderField);

        holderField.setToolTipText(
                "Name as shown on the card"
        );

        JPanel holderPanel = dialogField(
                "Cardholder Name",
                holderField
        );

        gbc.gridy++;

        root.add(holderPanel, gbc);

        // ===================== Card Type =====================

        JComboBox<String> typeCombo =
                new JComboBox<>(new String[]{
                    "Mada",
                    "Visa",
                    "Mastercard"
                });

        styleCardCombo(typeCombo);

        JPanel typePanel = new JPanel(
                new BorderLayout(0, 8)
        );

        typePanel.setOpaque(false);

        typePanel.add(
                label("Card Type", text(16f), PINK_800),
                BorderLayout.NORTH
        );

        typePanel.add(
                typeCombo,
                BorderLayout.CENTER
        );

        gbc.gridy++;

        root.add(typePanel, gbc);

        // ===================== Card Number =====================

        JTextField numberField = new JTextField();

        numberField.setToolTipText(
                "Enter any fictional 16-digit card number"
        );

        setCardNumberFilter(numberField);

        JPanel numberPanel = dialogField(
                "Card Number",
                numberField
        );

        JLabel numberCount = label(
                "0 / 16",
                text(12f),
                PINK_600
        );

        numberCount.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        numberPanel.add(
                numberCount,
                BorderLayout.SOUTH
        );

        numberField.getDocument().addDocumentListener(
                new DocumentListener() {

            private void updateCount() {

                int count = numberField.getText()
                        .replaceAll("[^0-9]", "")
                        .length();

                numberCount.setText(
                        count + " / 16"
                );
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCount();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCount();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCount();
            }
        });

        gbc.gridy++;

        root.add(numberPanel, gbc);

        // ===================== Expiry Date =====================

        JComboBox<String> monthCombo =
                new JComboBox<>();

        monthCombo.addItem("MM");

        for (int month = 1; month <= 12; month++) {

            monthCombo.addItem(
                    String.format(
                            Locale.US,
                            "%02d",
                            month
                    )
            );
        }

        styleCardCombo(monthCombo);

        JComboBox<String> yearCombo =
                new JComboBox<>();

        yearCombo.addItem("YYYY");

        int currentYear = YearMonth.now().getYear();

        for (int year = currentYear;
                year <= currentYear + 15;
                year++) {

            yearCombo.addItem(
                    String.valueOf(year)
            );
        }

        styleCardCombo(yearCombo);

        JPanel expiryPanel = new JPanel(
                new BorderLayout(0, 8)
        );

        expiryPanel.setOpaque(false);

        expiryPanel.add(
                label("Expiry Date", text(16f), PINK_800),
                BorderLayout.NORTH
        );

        JPanel dateFields = new JPanel(
                new GridLayout(1, 2, 10, 0)
        );

        dateFields.setOpaque(false);

        dateFields.add(monthCombo);

        dateFields.add(yearCombo);

        expiryPanel.add(
                dateFields,
                BorderLayout.CENTER
        );

        // ===================== CVV =====================

        JPasswordField cvvField =
                new JPasswordField();

        cvvField.setEchoChar('\u2022');

        cvvField.setToolTipText(
                "Enter a fictional 3-digit security code"
        );

        setDigitFilter(cvvField, 3);

        JPanel cvvPanel = dialogField(
                "CVV",
                cvvField
        );

        JLabel cvvCount = label(
                "0 / 3",
                text(12f),
                PINK_600
        );

        cvvCount.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        cvvPanel.add(
                cvvCount,
                BorderLayout.SOUTH
        );

        cvvField.getDocument().addDocumentListener(
                new DocumentListener() {

            private void updateCount() {

                cvvCount.setText(
                        cvvField.getDocument().getLength()
                        + " / 3"
                );
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCount();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCount();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCount();
            }
        });

        // ===================== Date and CVV Layout =====================

        JPanel expiryWrapper = new JPanel(
                new BorderLayout()
        );

        expiryWrapper.setOpaque(false);

        expiryWrapper.add(
                expiryPanel,
                BorderLayout.NORTH
        );

        JPanel cvvWrapper = new JPanel(
                new BorderLayout()
        );

        cvvWrapper.setOpaque(false);

        cvvWrapper.add(
                cvvPanel,
                BorderLayout.NORTH
        );

        JPanel details = new JPanel(
                new GridLayout(1, 2, 15, 0)
        );

        details.setOpaque(false);

        details.add(expiryWrapper);

        details.add(cvvWrapper);

        gbc.gridy++;

        root.add(details, gbc);

        // ===================== Testing Notice =====================

        JLabel notice = htmlLabel(
                "For testing purposes only. "
                + "Do not enter real banking information.",
                340,
                "center",
                text(13f),
                PINK_600
        );

        notice.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        gbc.gridy++;

        gbc.insets = new Insets(8, 0, 15, 0);

        root.add(notice, gbc);

        // ===================== Add Card Button =====================

        PillButton saveButton = new PillButton(
                "Add Card",
                17f,
                40,
                12
        ).style(
                PINK_800,
                Color.WHITE,
                null,
                PINK_950
        );

        saveButton.addActionListener(e -> {

            String holder =
                    holderField.getText().trim();

            String type =
                    (String) typeCombo.getSelectedItem();

            String number = numberField.getText()
                    .replaceAll("[^0-9]", "");

            // Validate Cardholder Name

            if (holder.isEmpty()) {

                showCardWarning(
                        dialog,
                        "Please enter the cardholder name."
                );

                holderField.requestFocusInWindow();

                return;
            }

            // Validate Card Number Length Only
            // Any fictional 16-digit number is accepted.

            if (!number.matches("[0-9]{16}")) {

                showCardWarning(
                        dialog,
                        "Card number must contain 16 digits."
                );

                numberField.requestFocusInWindow();

                return;
            }

            // Validate Expiry Selection

            if (monthCombo.getSelectedIndex() == 0
                    || yearCombo.getSelectedIndex() == 0) {

                showCardWarning(
                        dialog,
                        "Please select the expiry month and year."
                );

                return;
            }

            int month = Integer.parseInt(
                    (String) monthCombo.getSelectedItem()
            );

            int year = Integer.parseInt(
                    (String) yearCombo.getSelectedItem()
            );

            YearMonth expiryMonth =
                    YearMonth.of(year, month);

            // Validate Expiry Date

            if (expiryMonth.isBefore(YearMonth.now())) {

                showCardWarning(
                        dialog,
                        "This card has expired."
                );

                return;
            }

            // Validate CVV

            char[] cvv = cvvField.getPassword();

            boolean validCvv = cvv.length == 3;

            for (char digit : cvv) {

                if (digit < '0' || digit > '9') {
                    validCvv = false;
                }
            }

            Arrays.fill(cvv, '\0');

            if (!validCvv) {

                showCardWarning(
                        dialog,
                        "CVV must contain 3 digits."
                );

                cvvField.requestFocusInWindow();

                return;
            }

            // Format Expiry Date

            String expiry = String.format(
                    Locale.US,
                    "%02d/%02d",
                    month,
                    year % 100
            );

            // Store Only Last Four Digits

            String lastFour = number.substring(
                    number.length() - 4
            );

            SavedCard newCard = new SavedCard(
                    holder,
                    type,
                    lastFour,
                    expiry
            );

            savedCards.add(newCard);

            selectedCard = newCard;

            numberField.setText("");

            cvvField.setText("");

            refreshSavedCards();

            dialog.dispose();
        });

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 0, 0)
        );

        buttonPanel.setOpaque(false);

        buttonPanel.add(saveButton);

        gbc.gridy++;

        gbc.insets = new Insets(0, 0, 8, 0);

        root.add(buttonPanel, gbc);

        // ===================== Cancel Button =====================

        JButton cancelButton = new JButton("Cancel");

        cancelButton.setFont(text(15f));

        cancelButton.setForeground(PINK_800);

        cancelButton.setBorderPainted(false);

        cancelButton.setContentAreaFilled(false);

        cancelButton.setFocusPainted(false);

        cancelButton.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        cancelButton.addActionListener(e -> {

            numberField.setText("");

            cvvField.setText("");

            dialog.dispose();
        });

        JPanel cancelPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 0, 0)
        );

        cancelPanel.setOpaque(false);

        cancelPanel.add(cancelButton);

        gbc.gridy++;

        root.add(cancelPanel, gbc);

        // Clear Fields When Closing Dialog

        dialog.addWindowListener(
                new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(
                    java.awt.event.WindowEvent e) {

                numberField.setText("");

                cvvField.setText("");
            }
        });

        // ===================== Responsive Dialog =====================

        PageBody dialogBody = new PageBody();

        dialogBody.setBackground(Color.WHITE);

        JPanel wrapper = new FullWidth();

        wrapper.setLayout(new BorderLayout());

        wrapper.setBackground(Color.WHITE);

        wrapper.add(root, BorderLayout.NORTH);

        dialogBody.add(wrapper);

        JScrollPane dialogScroll = new JScrollPane(
                dialogBody,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        dialogScroll.setBorder(null);

        dialogScroll.getViewport()
                .setBackground(Color.WHITE);

        dialogScroll.getVerticalScrollBar()
                .setUnitIncrement(20);

        dialog.setContentPane(dialogScroll);

        Dimension preferred = root.getPreferredSize();

        Rectangle screen = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();

        int dialogWidth = Math.min(
                Math.max(580, preferred.width + 45),
                screen.width - 40
        );

        int dialogHeight = Math.min(
                preferred.height + 65,
                screen.height - 60
        );

        dialog.setSize(dialogWidth, dialogHeight);

        dialog.setLocationRelativeTo(this);

        dialog.getRootPane().setDefaultButton(saveButton);

        dialog.setVisible(true);
    }

    // ===================== Style Card ComboBox =====================

    private void styleCardCombo(
            JComboBox<String> combo) {

        combo.setFont(text(16f));

        combo.setForeground(PINK_900);

        combo.setBackground(PINK_50);

        combo.setPreferredSize(
                new Dimension(140, 42)
        );

        combo.setMinimumSize(
                new Dimension(70, 42)
        );

        combo.setBorder(
                BorderFactory.createLineBorder(PINK_300)
        );

        combo.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // ===================== Cardholder Name Filter =====================

    private void setNameFilter(JTextField field) {

        AbstractDocument document =
                (AbstractDocument) field.getDocument();

        document.setDocumentFilter(
                new DocumentFilter() {

            @Override
            public void insertString(
                    FilterBypass fb,
                    int offset,
                    String value,
                    AttributeSet attr)
                    throws BadLocationException {

                replace(
                        fb, offset, 0, value, attr
                );
            }

            @Override
            public void replace(
                    FilterBypass fb,
                    int offset,
                    int length,
                    String value,
                    AttributeSet attr)
                    throws BadLocationException {

                if (value == null) {
                    value = "";
                }

                String oldText = fb.getDocument().getText(
                        0,
                        fb.getDocument().getLength()
                );

                String newText =
                        oldText.substring(0, offset)
                        + value
                        + oldText.substring(offset + length);

                if (newText.matches("[\\p{L} .'-]*")
                        && newText.length() <= 50) {

                    fb.replace(
                            offset,
                            length,
                            value,
                            attr
                    );
                }
            }
        });
    }

    // ===================== Card Number Filter =====================

    private void setCardNumberFilter(JTextField field) {

        AbstractDocument document =
                (AbstractDocument) field.getDocument();

        document.setDocumentFilter(
                new DocumentFilter() {

            @Override
            public void insertString(
                    FilterBypass fb,
                    int offset,
                    String value,
                    AttributeSet attr)
                    throws BadLocationException {

                replace(
                        fb, offset, 0, value, attr
                );
            }

            @Override
            public void remove(
                    FilterBypass fb,
                    int offset,
                    int length)
                    throws BadLocationException {

                String oldText = fb.getDocument().getText(
                        0,
                        fb.getDocument().getLength()
                );

                if (length == 1
                        && offset > 0
                        && offset < oldText.length()
                        && oldText.charAt(offset) == ' ') {

                    offset--;

                    length++;
                }

                replace(
                        fb,
                        offset,
                        length,
                        "",
                        null
                );
            }

            @Override
            public void replace(
                    FilterBypass fb,
                    int offset,
                    int length,
                    String value,
                    AttributeSet attr)
                    throws BadLocationException {

                if (value == null) {
                    value = "";
                }

                if (!value.matches("[0-9 ]*")) {
                    return;
                }

                String oldText = fb.getDocument().getText(
                        0,
                        fb.getDocument().getLength()
                );

                String newText =
                        oldText.substring(0, offset)
                        + value
                        + oldText.substring(offset + length);

                String digits = newText.replaceAll(
                        "[^0-9]",
                        ""
                );

                // Maximum 16 Digits

                if (digits.length() > 16) {
                    return;
                }

                // Automatic Formatting

                StringBuilder formatted =
                        new StringBuilder();

                for (int i = 0;
                        i < digits.length();
                        i++) {

                    if (i > 0 && i % 4 == 0) {
                        formatted.append(" ");
                    }

                    formatted.append(
                            digits.charAt(i)
                    );
                }

                // Calculate Caret Position

                int digitsBeforeCaret = (
                        oldText.substring(0, offset)
                        + value
                ).replaceAll("[^0-9]", "").length();

                int caretPosition = 0;

                int seenDigits = 0;

                while (caretPosition < formatted.length()
                        && seenDigits < digitsBeforeCaret) {

                    if (Character.isDigit(
                            formatted.charAt(caretPosition))) {

                        seenDigits++;
                    }

                    caretPosition++;
                }

                fb.replace(
                        0,
                        fb.getDocument().getLength(),
                        formatted.toString(),
                        attr
                );

                final int newCaret = caretPosition;

                SwingUtilities.invokeLater(() -> {

                    field.setCaretPosition(
                            Math.min(
                                    newCaret,
                                    field.getDocument().getLength()
                            )
                    );
                });
            }
        });
    }

    // ===================== Digits Only Filter =====================

    private void setDigitFilter(
            JTextField field,
            int maxLength) {

        AbstractDocument document =
                (AbstractDocument) field.getDocument();

        document.setDocumentFilter(
                new DocumentFilter() {

            @Override
            public void insertString(
                    FilterBypass fb,
                    int offset,
                    String value,
                    AttributeSet attr)
                    throws BadLocationException {

                replace(
                        fb, offset, 0, value, attr
                );
            }

            @Override
            public void replace(
                    FilterBypass fb,
                    int offset,
                    int length,
                    String value,
                    AttributeSet attr)
                    throws BadLocationException {

                if (value == null) {
                    value = "";
                }

                int newLength =
                        fb.getDocument().getLength()
                        - length
                        + value.length();

                if (value.matches("[0-9]*")
                        && newLength <= maxLength) {

                    fb.replace(
                            offset,
                            length,
                            value,
                            attr
                    );
                }
            }
        });
    }

    // ===================== Card Warning =====================

    private void showCardWarning(
            JDialog dialog,
            String message) {

        JOptionPane.showMessageDialog(
                dialog,
                message,
                "Invalid Card Details",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // ===================== Dialog Field Helper =====================

    private JPanel dialogField(
            String title,
            JTextField field) {

        JPanel panel = new JPanel(
                new BorderLayout(0, 8)
        );

        panel.setOpaque(false);

        panel.add(label(
                title,
                text(16f),
                PINK_800
        ), BorderLayout.NORTH);

        field.setFont(text(16f));

        field.setForeground(PINK_950);

        field.setBackground(PINK_50);

        field.setCaretColor(PINK_800);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PINK_300),
                        new EmptyBorder(8, 12, 8, 12)
                )
        );

        field.setPreferredSize(
                new Dimension(140, 42)
        );

        field.setMinimumSize(
                new Dimension(60, 42)
        );

        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    // ===================== Remove Saved Card =====================

    private void removeSavedCard(SavedCard card) {

        int result = JOptionPane.showConfirmDialog(
                this,
                "Remove " + card.cardType
                + " ending in " + card.lastFour + "?",
                "Remove Card",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {

            savedCards.remove(card);

            if (selectedCard == card) {
                selectedCard = null;
            }

            refreshSavedCards();
        }
    }

    // ===================== Receiving Option =====================

    private JComponent buildOption(
            JRadioButton radio,
            String description) {

        RoundedPanel option = new RoundedPanel(
                18, PINK_50, false
        );

        option.setLayout(
                new BoxLayout(option, BoxLayout.Y_AXIS)
        );

        option.setBorder(
                new EmptyBorder(15, 18, 15, 18)
        );

        radio.setFont(text(17f));

        radio.setForeground(PINK_900);

        radio.setOpaque(false);

        radio.setFocusPainted(false);

        radio.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        addLeft(option, radio, 6);

        addLeft(option, htmlLabel(
                description,
                380,
                "left",
                text(14f),
                PINK_800
        ), 0);

        option.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return option;
    }

    // ===================== Order Summary =====================

    private JComponent buildOrderSummary() {

        RoundedPanel summary = new RoundedPanel(
                28, PINK_100, true
        );

        summary.setLayout(
                new BoxLayout(summary, BoxLayout.Y_AXIS)
        );

        int s = RoundedPanel.SH;

        summary.setBorder(new EmptyBorder(
                s + 25,
                s + 22,
                s + 25,
                s + 22
        ));

        addLeft(summary, label(
                "Order Summary",
                heading(27f),
                PINK_900
        ), 25);

        int count = 0;

        for (Map.Entry<Product, Integer> entry
                : ProductDetails.cart.entrySet()) {

            count += entry.getValue();
        }

        addLeft(summary, label(
                count + " item(s) in your order",
                text(15f),
                PINK_800
        ), 20);

        for (Map.Entry<Product, Integer> entry
                : ProductDetails.cart.entrySet()) {

            summary.add(buildProductRow(
                    entry.getKey(),
                    entry.getValue()
            ));

            summary.add(
                    Box.createVerticalStrut(15)
            );
        }

        if (ProductDetails.cart.isEmpty()) {

            addLeft(summary, label(
                    "Your cart is empty.",
                    text(16f),
                    PINK_800
            ), 15);
        }

        addDivider(summary);

        summary.add(summaryRow(
                label("Subtotal", text(17f), PINK_800),
                subtotalLabel
        ));

        summary.add(Box.createVerticalStrut(15));

        summary.add(summaryRow(
                feeTitle,
                deliveryLabel
        ));

        addDivider(summary);

        summary.add(summaryRow(
                label("Total", heading(23f), PINK_900),
                totalLabel
        ));

        summary.add(Box.createVerticalStrut(28));

        placeOrderButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        placeOrderButton.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        placeOrderButton.getPreferredSize().height
                )
        );

        placeOrderButton.setEnabled(
                !ProductDetails.cart.isEmpty()
        );

        summary.add(placeOrderButton);

        summary.add(Box.createVerticalStrut(15));

        JLabel note = htmlLabel(
                "Your order will be processed "
                + "using your selected payment method.",
                260,
                "center",
                text(14f),
                PINK_800
        );

        note.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        summary.add(note);

        summary.add(Box.createVerticalStrut(20));

        PillButton backButton = new PillButton(
                "Back to Shopping Cart",
                15f,
                18,
                10
        ).style(
                PINK_50,
                PINK_800,
                PINK_300,
                PINK_200
        );

        backButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        backButton.addActionListener(
                e -> navigate("Cart")
        );

        summary.add(backButton);

        return summary;
    }

    // ===================== Product Row =====================

    private JComponent buildProductRow(
            Product product,
            int quantity) {

        JPanel row = new JPanel(
                new BorderLayout(12, 0)
        );

        row.setOpaque(false);

        row.add(
                new PaymentImage(product.image),
                BorderLayout.WEST
        );

        JPanel info = vbox();

        addLeft(info, htmlLabel(
                product.name,
                175,
                "left",
                heading(18f),
                PINK_900
        ), 6);

        addLeft(info, label(
                "Quantity: " + quantity,
                text(14f),
                PINK_800
        ), 6);

        addLeft(info, label(
                money(product.price * quantity),
                heading(17f),
                PINK_900
        ), 0);

        row.add(info, BorderLayout.CENTER);

        row.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return fixedHeight(row);
    }

    // ===================== Update Receiving Method =====================

    private void updateReceivingMethod() {

        boolean delivery = deliveryRadio.isSelected();

        addressPanel.setVisible(delivery);

        pickupPanel.setVisible(!delivery);

        if (delivery) {

            cashRadio.setText("Cash on Delivery");

            feeTitle.setText("Delivery Fee");

        } else {

            cashRadio.setText("Pay at Salon");

            feeTitle.setText("Pickup Fee");
        }

        updateTotals();

        revalidate();

        repaint();
    }

    // ===================== Update Payment Method =====================

    private void updatePaymentMethod() {

        cardPanel.setVisible(
                cardRadio.isSelected()
        );

        revalidate();

        repaint();
    }

    // ===================== Calculate Total =====================

    private double calculateSubtotal() {

        double subtotal = 0;

        for (Map.Entry<Product, Integer> entry
                : ProductDetails.cart.entrySet()) {

            subtotal += entry.getKey().price
                    * entry.getValue();
        }

        return subtotal;
    }

    private double calculateDeliveryFee() {

        if (ProductDetails.cart.isEmpty()) {
            return 0.0;
        }

        return deliveryRadio.isSelected()
                ? DELIVERY_FEE
                : 0.0;
    }

    private double calculateTotal() {

        return calculateSubtotal()
                + calculateDeliveryFee();
    }

    private void updateTotals() {

        subtotalLabel.setText(
                money(calculateSubtotal())
        );

        deliveryLabel.setText(
                money(calculateDeliveryFee())
        );

        totalLabel.setText(
                money(calculateTotal())
        );
    }

    // ===================== Order Validation =====================

    private boolean validateOrder() {

        if (ProductDetails.cart.isEmpty()) {

            showWarning(
                    "Your shopping cart is empty."
            );

            return false;
        }

        if (nameField.getText().trim().isEmpty()) {

            showWarning(
                    "Please enter your full name."
            );

            nameField.requestFocusInWindow();

            return false;
        }

        String phone = phoneField.getText().trim();

        if (!phone.matches("05[0-9]{8}")) {

            showWarning(
                    "Please enter a valid Saudi mobile number."
            );

            phoneField.requestFocusInWindow();

            return false;
        }

        // Validate Delivery Address

        if (deliveryRadio.isSelected()) {

            if (cityField.getText().trim().isEmpty()
                    || districtField.getText().trim().isEmpty()
                    || streetField.getText().trim().isEmpty()
                    || addressField.getText().trim().isEmpty()) {

                showWarning(
                        "Please complete your delivery address."
                );

                return false;
            }
        }

        // Validate Selected Card

        if (cardRadio.isSelected()) {

            if (selectedCard == null
                    || !savedCards.contains(selectedCard)) {

                showWarning(
                        "Please add or select a payment card."
                );

                return false;
            }

            int month = Integer.parseInt(
                    selectedCard.expiryDate.substring(0, 2)
            );

            int year = 2000 + Integer.parseInt(
                    selectedCard.expiryDate.substring(3, 5)
            );

            if (YearMonth.of(year, month)
                    .isBefore(YearMonth.now())) {

                showWarning(
                        "Your selected card has expired."
                );

                return false;
            }
        }

        // Validate Product Quantities

        for (Map.Entry<Product, Integer> entry
                : ProductDetails.cart.entrySet()) {

            if (entry.getValue() < 1
                    || entry.getValue() > entry.getKey().quantity) {

                showWarning(
                        "Invalid quantity for "
                        + entry.getKey().name
                );

                return false;
            }
        }

        return true;
    }

    // ===================== Place Order =====================

    private void placeOrder() {

        if (!validateOrder()) {
            return;
        }

        String receivingMethod =
                deliveryRadio.isSelected()
                        ? "Home Delivery"
                        : "Salon Pickup";

        String paymentMethod;

        if (cardRadio.isSelected()) {

            paymentMethod =
                    selectedCard.cardType
                    + " ending in "
                    + selectedCard.lastFour
                    + " (Test)";

        } else {

            paymentMethod = cashRadio.getText();
        }

        double total = calculateTotal();

        String address = "";

        if (deliveryRadio.isSelected()) {

            address = "\nAddress: "
                    + cityField.getText().trim()
                    + ", "
                    + districtField.getText().trim()
                    + ", "
                    + streetField.getText().trim()
                    + "\n"
                    + addressField.getText().trim()
                    + "\n";
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                "Please review your order:\n\n"
                + "Customer: "
                + nameField.getText().trim() + "\n"
                + "Phone: "
                + phoneField.getText().trim() + "\n"
                + "Receiving Method: "
                + receivingMethod + "\n"
                + "Payment Method: "
                + paymentMethod + "\n"
                + address
                + "\nTotal: " + money(total)
                + "\n\nConfirm your order?",
                "Confirm Order",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        // Temporary Order Confirmation
        // Database integration will be added later.

        String paymentStatus =
                cardRadio.isSelected()
                        ? "Simulated payment successful."
                        : "Payment due upon receiving your order.";

        ProductDetails.cart.clear();

        JOptionPane.showMessageDialog(
                this,
                "Your order has been confirmed!\n\n"
                + "Thank you for shopping with Glam & Go.\n\n"
                + paymentStatus
                + "\nTotal: " + money(total),
                "Order Confirmation",
                JOptionPane.INFORMATION_MESSAGE
        );

        new Home().setVisible(true);

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

                new ShoppingCart().setVisible(true);

                dispose();

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

    // ===================== Form Field Helper =====================

    private void addField(
            JPanel panel,
            String title,
            JTextField field) {

        addLeft(panel, label(
                title,
                text(16f),
                PINK_800
        ), 8);

        field.setFont(text(16f));

        field.setForeground(PINK_950);

        field.setBackground(PINK_50);

        field.setCaretColor(PINK_800);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PINK_300),
                        new EmptyBorder(10, 12, 10, 12)
                )
        );

        field.setPreferredSize(
                new Dimension(400, 44)
        );

        field.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 44)
        );

        field.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(field);

        panel.add(Box.createVerticalStrut(20));
    }

    // ===================== Summary Row =====================

    private JComponent summaryRow(
            JLabel title,
            JLabel value) {

        JPanel row = new JPanel(
                new BorderLayout(10, 0)
        );

        row.setOpaque(false);

        row.add(title, BorderLayout.WEST);

        row.add(value, BorderLayout.EAST);

        return fixedHeight(row);
    }

    // ===================== Divider =====================

    private void addDivider(JPanel panel) {

        panel.add(Box.createVerticalStrut(22));

        JSeparator line = new JSeparator();

        line.setForeground(PINK_300);

        line.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 1)
        );

        panel.add(line);

        panel.add(Box.createVerticalStrut(22));
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

            panel.add(
                    Box.createVerticalStrut(gap)
            );
        }
    }

    private String money(double amount) {

        return String.format(
                Locale.US,
                "SAR %.2f",
                amount
        );
    }

    private void showWarning(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Glam & Go",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // ===================== Product Image =====================

    private static class PaymentImage extends JComponent {

        private final String path;

        PaymentImage(String path) {

            this.path = path;

            setPreferredSize(
                    new Dimension(75, 90)
            );
        }

        @Override
        protected void paintComponent(Graphics gr) {

            Graphics2D g = g2(gr);

            int w = getWidth();

            int h = getHeight();

            g.clip(new RoundRectangle2D.Double(
                    0, 0, w, h, 15, 15
            ));

            g.setColor(PINK_50);

            g.fillRect(0, 0, w, h);

            BufferedImage image = loadImage(path);

            if (image != null) {

                drawCover(g, image, 0, 0, w, h);

            } else {

                g.setColor(PINK_500);

                g.setFont(text(11f));

                g.drawString(
                        "Product",
                        10,
                        h / 2
                );
            }

            g.dispose();
        }
    }

    // ===================== Main =====================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->

                new Payment().setVisible(true)

        );
    }
}
