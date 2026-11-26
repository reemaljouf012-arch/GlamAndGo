
package glamandgo;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static glamandgo.Theme.*;

public class AppointmentBooking extends JFrame {

    private final String serviceName;
    private final String employeeName;

    private JSpinner dateSpinner;
    private JComboBox<String> timeCombo;

    public AppointmentBooking(String serviceName, String employeeName) {

        this.serviceName = serviceName;
        this.employeeName = employeeName;

        setTitle("Glam & Go | Appointment Booking");
        setSize(1280, 820);
        setMinimumSize(new Dimension(1100, 650));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(navbar(this::navigate), BorderLayout.NORTH);

        PageBody body = new PageBody();

        body.add(buildHero());
        body.add(buildBookingSection());
        body.add(footer(this::navigate));

        JScrollPane scroll = new JScrollPane(
                body,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(24);

        add(scroll, BorderLayout.CENTER);
    }

    // Hero Section
    private JComponent buildHero() {

        JPanel hero = new JPanel();
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));
        hero.setBackground(PINK_50);
        hero.setBorder(new EmptyBorder(65, 20, 65, 20));

        JLabel title = label(
                "Book Your Appointment",
                heading(48),
                PINK_950
        );

        JLabel subtitle = label(
                "Choose your preferred date and time.",
                text(18),
                PINK_800
        );

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        hero.add(title);
        hero.add(Box.createVerticalStrut(15));
        hero.add(subtitle);

        return hero;
    }

    // Booking Section
    private JComponent buildBookingSection() {

        JPanel section = new JPanel(new GridBagLayout());
        section.setBackground(Color.WHITE);
        section.setBorder(new EmptyBorder(40, 20, 55, 20));

        RoundedPanel form = new RoundedPanel(22, PINK_50, true);

        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(35, 40, 35, 40));

        form.setPreferredSize(new Dimension(520, 520));

        // Selected Service
        form.add(createInfoLabel("Selected Service"));
        form.add(createValueLabel(serviceName));

        form.add(Box.createVerticalStrut(22));

        // Selected Employee
        form.add(createInfoLabel("Selected Employee"));
        form.add(createValueLabel(employeeName));

        form.add(Box.createVerticalStrut(30));

        // Appointment Date
        form.add(createInfoLabel("Choose Appointment Date"));

        SpinnerDateModel dateModel = new SpinnerDateModel(
                new Date(),
                new Date(),
                null,
                Calendar.DAY_OF_MONTH
        );

        dateSpinner = new JSpinner(dateModel);

        JSpinner.DateEditor dateEditor =
                new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");

        dateSpinner.setEditor(dateEditor);

        dateEditor.getTextField().setComponentOrientation(
                ComponentOrientation.LEFT_TO_RIGHT
        );

        dateSpinner.setFont(text(15));
        dateSpinner.setMaximumSize(new Dimension(500, 42));
        dateSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(dateSpinner);

        form.add(Box.createVerticalStrut(25));

        // Appointment Time
        form.add(createInfoLabel("Choose Appointment Time"));

        String[] times = {
            "10:00 AM",
            "11:00 AM",
            "12:00 PM",
            "01:00 PM",
            "02:00 PM",
            "03:00 PM",
            "04:00 PM",
            "05:00 PM",
            "06:00 PM",
            "07:00 PM",
            "08:00 PM"
        };

        timeCombo = new JComboBox<>(times);
        timeCombo.setFont(text(15));
        timeCombo.setMaximumSize(new Dimension(500, 42));
        timeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(timeCombo);

        form.add(Box.createVerticalStrut(35));

        // Confirm Button
        PillButton confirmButton = new PillButton(
                "Confirm Booking", 15, 30, 12
        );

        confirmButton.style(
                PINK_800,
                Color.WHITE,
                null,
                PINK_900
        );

        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmButton.addActionListener(e -> confirmBooking());

        form.add(confirmButton);

        section.add(form);

        return section;
    }

    // Form Labels
    private JLabel createInfoLabel(String textValue) {

        JLabel label = label(
                textValue,
                text(15),
                PINK_800
        );

        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        return label;
    }

    private JLabel createValueLabel(String textValue) {

        JLabel label = label(
                textValue,
                heading(21),
                PINK_950
        );

        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        return label;
    }

    // Booking Confirmation
    private void confirmBooking() {

        Date selectedDate = (Date) dateSpinner.getValue();

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("dd/MM/yyyy");

        String date = dateFormat.format(selectedDate);

        String time = (String) timeCombo.getSelectedItem();

        JOptionPane.showMessageDialog(
                this,
                "Your appointment details:\n\n"
                + "Service: " + serviceName + "\n"
                + "Employee: " + employeeName + "\n"
                + "Date: " + date + "\n"
                + "Time: " + time,
                "Booking Confirmation",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Navigation
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

            default:

                JOptionPane.showMessageDialog(
                        this,
                        "\"" + page + "\" page is not connected yet.",
                        "Glam & Go",
                        JOptionPane.INFORMATION_MESSAGE
                );
        }
    }
}
