
package glamandgo;

import java.awt.*;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import static glamandgo.Theme.*;

public class PaymentMethods extends JFrame {

    private PaymentCardStore.SavedCard selectedCard;

    private final JPanel cardsPanel = vbox();

    private final JLabel selectedLabel = label(
            "No default card selected",
            text(15f),
            PINK_700
    );

    public PaymentMethods() {

        setTitle("Glam & Go | Payment Methods");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 820);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        List<PaymentCardStore.SavedCard> cards =
                PaymentCardStore.getSavedCards();

        if (!cards.isEmpty()) {
            selectedCard = cards.get(0);
        }

        add(navbar(this::navigate), BorderLayout.NORTH);

        PageBody body = new PageBody();

        body.add(buildHero());
        body.add(buildPaymentMethods());
        body.add(footer(this::navigate));

        JScrollPane scroll = new JScrollPane(
                body,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(24);

        add(scroll, BorderLayout.CENTER);

        refreshCards();
    }

    // ===================== Hero =====================

    private JComponent buildHero() {

        JPanel content = vbox();

        content.add(sectionTitle(
                "Payment Methods",
                "Manage your saved cards securely.",
                PINK_900,
                PINK_800
        ));

        return new Section(
                PINK_50,
                45,
                1100,
                content
        );
    }

    // ===================== Main Content =====================

    private JComponent buildPaymentMethods() {

        JPanel content = vbox();

        RoundedPanel container = new RoundedPanel(
                28,
                Color.WHITE,
                true
        );

        container.setLayout(
                new BoxLayout(container, BoxLayout.Y_AXIS)
        );

        int s = RoundedPanel.SH;

        container.setBorder(new EmptyBorder(
                s + 30,
                s + 30,
                s + 30,
                s + 30
        ));

        JLabel title = label(
                "My Saved Cards",
                heading(28f),
                PINK_900
        );

        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(title);

        container.add(Box.createVerticalStrut(8));

        JLabel description = htmlLabel(
                "Add and manage your payment cards. "
                + "Your saved cards can also be used "
                + "when checking out.",
                650,
                "left",
                text(16f),
                PINK_700
        );

        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(description);

        container.add(Box.createVerticalStrut(25));

        JSeparator divider = new JSeparator();

        divider.setForeground(PINK_200);

        divider.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 1)
        );

        container.add(divider);

        container.add(Box.createVerticalStrut(25));

        cardsPanel.setOpaque(false);

        cardsPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        container.add(cardsPanel);

        container.add(Box.createVerticalStrut(20));

        PillButton addButton = new PillButton(
                "+ Add New Card",
                17f,
                28,
                13
        ).style(
                PINK_800,
                Color.WHITE,
                null,
                PINK_950
        );

        addButton.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        addButton.addActionListener(
                e -> showAddCardDialog()
        );

        container.add(addButton);

        container.add(Box.createVerticalStrut(20));

        selectedLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        container.add(selectedLabel);

        container.add(Box.createVerticalStrut(15));

        JLabel notice = htmlLabel(
                "For this project demo, card information is "
                + "stored in memory. Only the card type, "
                + "holder name, last four digits and expiry "
                + "date are retained.",
                700,
                "left",
                text(13f),
                PINK_600
        );

        notice.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(notice);

        content.add(container);

        return new Section(
                Color.WHITE,
                35,
                1000,
                content
        );
    }

    // ===================== Refresh Cards =====================

    private void refreshCards() {

        cardsPanel.removeAll();

        List<PaymentCardStore.SavedCard> cards =
                PaymentCardStore.getSavedCards();

        if (cards.isEmpty()) {

            selectedCard = null;

            JLabel emptyTitle = label(
                    "No saved cards yet",
                    heading(22f),
                    PINK_800
            );

            emptyTitle.setAlignmentX(
                    Component.LEFT_ALIGNMENT
            );

            cardsPanel.add(emptyTitle);

            cardsPanel.add(Box.createVerticalStrut(8));

            JLabel emptyDescription = label(
                    "Add your first card to make checkout easier.",
                    text(15f),
                    PINK_700
            );

            emptyDescription.setAlignmentX(
                    Component.LEFT_ALIGNMENT
            );

            cardsPanel.add(emptyDescription);

            selectedLabel.setText(
                    "No default card selected"
            );

        } else {

            if (!PaymentCardStore.containsCard(selectedCard)) {
                selectedCard = cards.get(0);
            }

            ButtonGroup group = new ButtonGroup();

            for (PaymentCardStore.SavedCard card : cards) {

                cardsPanel.add(
                        buildCardItem(card, group)
                );

                cardsPanel.add(
                        Box.createVerticalStrut(15)
                );
            }

            selectedLabel.setText(
                    "Default card: "
                    + selectedCard.cardType
                    + " ending in "
                    + selectedCard.lastFour
            );
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();

        revalidate();
        repaint();
    }

    // ===================== Card Item =====================

    private JComponent buildCardItem(
            PaymentCardStore.SavedCard card,
            ButtonGroup group) {

        RoundedPanel panel = new RoundedPanel(
                20,
                PINK_50,
                false
        );

        panel.setLayout(
                new BorderLayout(18, 0)
        );

        panel.setBorder(
                new EmptyBorder(20, 20, 20, 20)
        );

        // Selection

        JRadioButton radio = new JRadioButton();

        radio.setOpaque(false);

        radio.setFocusPainted(false);

        radio.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        group.add(radio);

        radio.setSelected(card == selectedCard);

        radio.addActionListener(e -> {

            selectedCard = card;

            refreshCards();
        });

        panel.add(radio, BorderLayout.WEST);

        // Card Information

        JPanel info = vbox();

        JLabel cardType = label(
                card.cardType
                + "  ••••  "
                + card.lastFour,
                heading(22f),
                PINK_900
        );

        JLabel holder = label(
                card.holderName,
                text(16f),
                PINK_800
        );

        JLabel expiry = label(
                "Expires " + card.expiryDate,
                text(14f),
                PINK_600
        );

        addLeft(info, cardType, 8);

        addLeft(info, holder, 7);

        addLeft(info, expiry, 0);

        panel.add(info, BorderLayout.CENTER);

        // Default Badge

        if (card == selectedCard) {

            JLabel defaultBadge = label(
                    "DEFAULT",
                    text(12f),
                    PINK_800
            );

            defaultBadge.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            panel.add(
                    defaultBadge,
                    BorderLayout.NORTH
            );
        }

        // Remove Button

        JButton removeButton = new JButton("Remove");

        removeButton.setFont(text(14f));

        removeButton.setForeground(PINK_800);

        removeButton.setBorderPainted(false);

        removeButton.setContentAreaFilled(false);

        removeButton.setFocusPainted(false);

        removeButton.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        removeButton.addActionListener(
                e -> removeCard(card)
        );

        panel.add(
                removeButton,
                BorderLayout.EAST
        );

        return fixedHeight(panel);
    }

    // ===================== Add Card Dialog =====================

    private void showAddCardDialog() {

        JDialog dialog = new JDialog(
                this,
                "Add New Card",
                true
        );

        dialog.setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        dialog.setResizable(false);

        JPanel form = vbox();

        form.setBackground(Color.WHITE);

        form.setOpaque(true);

        form.setBorder(
                new EmptyBorder(25, 30, 25, 30)
        );

        JLabel title = label(
                "Add New Card",
                heading(28f),
                PINK_900
        );

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        form.add(title);

        form.add(Box.createVerticalStrut(8));

        JLabel subtitle = label(
                "Enter your card details below.",
                text(15f),
                PINK_700
        );

        subtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        form.add(subtitle);

        form.add(Box.createVerticalStrut(22));

        // Cardholder Name

        JTextField holderField = new JTextField();

        setNameFilter(holderField);

        addField(
                form,
                "Cardholder Name",
                holderField
        );

        // Card Type

        JComboBox<String> typeCombo =
                new JComboBox<>(new String[]{
                    "Mada",
                    "Visa",
                    "Mastercard"
                });

        styleCombo(typeCombo);

        addComponentField(
                form,
                "Card Type",
                typeCombo
        );

        // Card Number

        JTextField numberField = new JTextField();

        setDigitFilter(numberField, 16);

        addField(
                form,
                "Card Number (16 digits)",
                numberField
        );

        // Expiry Date

        JComboBox<String> monthCombo =
                new JComboBox<>();

        monthCombo.addItem("MM");

        for (int i = 1; i <= 12; i++) {

            monthCombo.addItem(
                    String.format(Locale.US, "%02d", i)
            );
        }

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

        styleCombo(monthCombo);

        styleCombo(yearCombo);

        JPanel expiryFields = new JPanel(
                new GridLayout(1, 2, 12, 0)
        );

        expiryFields.setOpaque(false);

        expiryFields.add(monthCombo);

        expiryFields.add(yearCombo);

        addComponentField(
                form,
                "Expiry Date",
                expiryFields
        );

        // CVV

        JPasswordField cvvField =
                new JPasswordField();

        setDigitFilter(cvvField, 3);

        addField(
                form,
                "CVV",
                cvvField
        );

        form.add(Box.createVerticalStrut(5));

        JLabel notice = htmlLabel(
                "Demo only: use fictional card details. "
                + "Never enter real banking information.",
                400,
                "left",
                text(13f),
                PINK_600
        );

        notice.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        form.add(notice);

        form.add(Box.createVerticalStrut(22));

        // Buttons

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 12, 0)
        );

        buttons.setOpaque(false);

        PillButton saveButton = new PillButton(
                "Save Card",
                16f,
                25,
                12
        ).style(
                PINK_800,
                Color.WHITE,
                null,
                PINK_950
        );

        JButton cancelButton = new JButton("Cancel");

        cancelButton.setFont(text(15f));

        cancelButton.setForeground(PINK_800);

        cancelButton.setBorderPainted(false);

        cancelButton.setContentAreaFilled(false);

        cancelButton.setFocusPainted(false);

        buttons.add(saveButton);

        buttons.add(cancelButton);

        form.add(buttons);

        saveButton.addActionListener(e -> {

            String holder =
                    holderField.getText().trim();

            String number =
                    numberField.getText().trim();

            String type =
                    (String) typeCombo.getSelectedItem();

            String cvv =
                    new String(cvvField.getPassword());

            // Validate Holder

            if (holder.isEmpty()) {

                warning(
                        dialog,
                        "Please enter the cardholder name."
                );

                holderField.requestFocusInWindow();

                return;
            }

            // Validate Number

            if (!number.matches("[0-9]{16}")) {

                warning(
                        dialog,
                        "Card number must contain 16 digits."
                );

                numberField.requestFocusInWindow();

                return;
            }

            // Validate Expiry

            if (monthCombo.getSelectedIndex() == 0
                    || yearCombo.getSelectedIndex() == 0) {

                warning(
                        dialog,
                        "Please select the expiry date."
                );

                return;
            }

            int month = Integer.parseInt(
                    (String) monthCombo.getSelectedItem()
            );

            int year = Integer.parseInt(
                    (String) yearCombo.getSelectedItem()
            );

            YearMonth expiry = YearMonth.of(
                    year,
                    month
            );

            if (expiry.isBefore(YearMonth.now())) {

                warning(
                        dialog,
                        "This card has expired."
                );

                return;
            }

            // Validate CVV

            if (!cvv.matches("[0-9]{3}")) {

                warning(
                        dialog,
                        "CVV must contain 3 digits."
                );

                cvvField.requestFocusInWindow();

                return;
            }

            // Store Only Safe Card Information

            String lastFour = number.substring(12);

            String expiryDate = String.format(
                    Locale.US,
                    "%02d/%02d",
                    month,
                    year % 100
            );

            PaymentCardStore.SavedCard newCard =
                    new PaymentCardStore.SavedCard(
                            holder,
                            type,
                            lastFour,
                            expiryDate
                    );

            PaymentCardStore.addCard(newCard);

            selectedCard = newCard;

            // Clear Sensitive Fields

            numberField.setText("");

            cvvField.setText("");

            Arrays.fill(
                    cvvField.getPassword(),
                    '\0'
            );

            refreshCards();

            dialog.dispose();
        });

        cancelButton.addActionListener(e -> {

            numberField.setText("");

            cvvField.setText("");

            dialog.dispose();
        });

        // Dialog Layout

        JScrollPane scroll = new JScrollPane(
                form,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setBorder(null);

        scroll.getViewport().setBackground(Color.WHITE);

        scroll.getVerticalScrollBar()
                .setUnitIncrement(20);

        dialog.setContentPane(scroll);

        dialog.setSize(560, 720);

        dialog.setLocationRelativeTo(this);

        dialog.getRootPane().setDefaultButton(saveButton);

        dialog.setVisible(true);
    }

    // ===================== Remove Card =====================

    private void removeCard(
            PaymentCardStore.SavedCard card) {

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove "
                + card.cardType
                + " ending in "
                + card.lastFour
                + "?",
                "Remove Card",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {

            PaymentCardStore.removeCard(card);

            if (selectedCard == card) {
                selectedCard = null;
            }

            refreshCards();
        }
    }

    // ===================== Form Helpers =====================

    private void addField(
            JPanel parent,
            String title,
            JTextField field) {

        field.setFont(text(15f));

        field.setForeground(PINK_900);

        field.setBackground(PINK_50);

        field.setCaretColor(PINK_800);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PINK_300),
                        new EmptyBorder(9, 12, 9, 12)
                )
        );

        field.setPreferredSize(
                new Dimension(400, 42)
        );

        addComponentField(
                parent,
                title,
                field
        );
    }

    private void addComponentField(
            JPanel parent,
            String title,
            JComponent component) {

        JLabel label = label(
                title,
                text(15f),
                PINK_800
        );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        component.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        component.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 44)
        );

        parent.add(label);

        parent.add(Box.createVerticalStrut(7));

        parent.add(component);

        parent.add(Box.createVerticalStrut(15));
    }

    private void styleCombo(
            JComboBox<String> combo) {

        combo.setFont(text(15f));

        combo.setForeground(PINK_900);

        combo.setBackground(PINK_50);

        combo.setBorder(
                BorderFactory.createLineBorder(PINK_300)
        );

        combo.setPreferredSize(
                new Dimension(150, 42)
        );

        combo.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // ===================== Name Filter =====================

    private void setNameFilter(
            JTextField field) {

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

                replace(fb, offset, 0, value, attr);
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

                String old = fb.getDocument().getText(
                        0,
                        fb.getDocument().getLength()
                );

                String updated =
                        old.substring(0, offset)
                        + value
                        + old.substring(offset + length);

                if (updated.matches("[\\p{L} .'-]*")
                        && updated.length() <= 50) {

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

    // ===================== Digit Filter =====================

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

                replace(fb, offset, 0, value, attr);
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

                if (!value.matches("[0-9]*")) {
                    return;
                }

                int newLength =
                        fb.getDocument().getLength()
                        - length
                        + value.length();

                if (newLength <= maxLength) {

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

            case "My Account":

                new MyAccount().setVisible(true);

                dispose();

                break;

            case "My Appointments":
            case "Appointments":

                new MyAppointments().setVisible(true);

                dispose();

                break;

            case "My Orders":

                new MyOrders().setVisible(true);

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

    // ===================== Helper =====================

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

    private void warning(
            Component parent,
            String message) {

        JOptionPane.showMessageDialog(
                parent,
                message,
                "Invalid Card Details",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // ===================== Main =====================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new PaymentMethods().setVisible(true)
        );
    }
}
