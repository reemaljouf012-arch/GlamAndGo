
package glamandgo;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static glamandgo.Theme.*;

public class AppointmentBooking extends JFrame {

    private final String serviceName;
    private final String employeeName;

    private Date selectedDate;
    private JLabel dateLabel;
    private JComboBox<String> timeCombo;

    private final SimpleDateFormat storageDateFormat =
            new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    private final SimpleDateFormat displayDateFormat =
            new SimpleDateFormat("MMMM d, yyyy", Locale.US);

    private final Font calendarFont =
            new Font("SansSerif", Font.PLAIN, 14);

    private final Font calendarBold =
            new Font("SansSerif", Font.BOLD, 14);

    private static final String[] MONTHS = {
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    };

    public AppointmentBooking(String serviceName, String employeeName) {

        this.serviceName = serviceName;
        this.employeeName = employeeName;
        selectedDate = new Date();

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

    // ===================== Hero =====================

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

    // ===================== Booking Form =====================

    private JComponent buildBookingSection() {

        JPanel section = new JPanel(new GridBagLayout());
        section.setBackground(Color.WHITE);
        section.setBorder(new EmptyBorder(40, 20, 55, 20));

        RoundedPanel form = new RoundedPanel(22, PINK_50, true);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(35, 40, 35, 40));
        form.setPreferredSize(new Dimension(520, 520));

        form.add(createInfoLabel("Selected Service"));
        form.add(createValueLabel(serviceName));

        form.add(Box.createVerticalStrut(22));

        form.add(createInfoLabel("Selected Employee"));
        form.add(createValueLabel(employeeName));

        form.add(Box.createVerticalStrut(30));

        form.add(createInfoLabel("Choose Appointment Date"));

        JPanel datePanel = new JPanel(new BorderLayout(12, 0));
        datePanel.setOpaque(false);
        datePanel.setMaximumSize(new Dimension(500, 44));
        datePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        dateLabel = new JLabel(displayDateFormat.format(selectedDate));
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        dateLabel.setForeground(PINK_950);
        dateLabel.setOpaque(true);
        dateLabel.setBackground(Color.WHITE);
        dateLabel.setBorder(new EmptyBorder(10, 14, 10, 14));

        PillButton calendarButton = new PillButton(
                "Choose Date", 13, 20, 9
        );

        calendarButton.style(
                PINK_800, Color.WHITE, null, PINK_900
        );

        calendarButton.addActionListener(e -> openCalendar());

        datePanel.add(dateLabel, BorderLayout.CENTER);
        datePanel.add(calendarButton, BorderLayout.EAST);

        form.add(datePanel);
        form.add(Box.createVerticalStrut(25));

        form.add(createInfoLabel("Choose Appointment Time"));

        String[] times = {
            "10:00 AM", "11:00 AM", "12:00 PM",
            "01:00 PM", "02:00 PM", "03:00 PM",
            "04:00 PM", "05:00 PM", "06:00 PM",
            "07:00 PM", "08:00 PM"
        };

        timeCombo = new JComboBox<>(times);
        timeCombo.setFont(text(15));
        timeCombo.setMaximumSize(new Dimension(500, 42));
        timeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(timeCombo);
        form.add(Box.createVerticalStrut(35));

        PillButton confirmButton = new PillButton(
                "Confirm Booking", 15, 30, 12
        );

        confirmButton.style(
                PINK_800, Color.WHITE, null, PINK_900
        );

        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.addActionListener(e -> confirmBooking());

        form.add(confirmButton);
        section.add(form);

        return section;
    }

    private JLabel createInfoLabel(String value) {

        JLabel result = label(value, text(15), PINK_800);
        result.setAlignmentX(Component.LEFT_ALIGNMENT);
        return result;
    }

    private JLabel createValueLabel(String value) {

        JLabel result = label(value, heading(21), PINK_950);
        result.setAlignmentX(Component.LEFT_ALIGNMENT);
        return result;
    }

    // ===================== Real Calendar Popup =====================

    private void openCalendar() {

        Calendar initial = Calendar.getInstance();
        initial.setTime(selectedDate);

        final int[] year = {initial.get(Calendar.YEAR)};
        final int[] month = {initial.get(Calendar.MONTH)};

        JDialog dialog = new JDialog(
                this, "Choose Appointment Date", true
        );

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);

        JPanel main = new JPanel(new BorderLayout(0, 18));
        main.setBackground(Color.WHITE);
        main.setBorder(new EmptyBorder(25, 28, 22, 28));

        // Heading
        JPanel headingPanel = new JPanel();
        headingPanel.setOpaque(false);
        headingPanel.setLayout(new BoxLayout(
                headingPanel, BoxLayout.Y_AXIS
        ));

        JLabel title = label(
                "Select a Date", heading(27), PINK_950
        );
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = label(
                "Choose your preferred appointment day.",
                text(13), PINK_800
        );
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headingPanel.add(title);
        headingPanel.add(Box.createVerticalStrut(7));
        headingPanel.add(subtitle);

        main.add(headingPanel, BorderLayout.NORTH);

        // Month and year selection
        JPanel center = new JPanel(new BorderLayout(0, 16));
        center.setOpaque(false);

        JPanel selectors = new JPanel(new GridLayout(1, 2, 12, 0));
        selectors.setOpaque(false);

        JComboBox<String> monthBox = new JComboBox<>(MONTHS);
        monthBox.setSelectedIndex(month[0]);
        monthBox.setFont(calendarBold);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        Integer[] years = new Integer[11];
        for (int i = 0; i < years.length; i++) {
            years[i] = currentYear + i;
        }

        JComboBox<Integer> yearBox = new JComboBox<>(years);
        yearBox.setSelectedItem(year[0]);
        yearBox.setFont(calendarBold);

        selectors.add(monthBox);
        selectors.add(yearBox);

        center.add(selectors, BorderLayout.NORTH);

        // Calendar grid
        JPanel calendar = new JPanel(new BorderLayout(0, 9));
        calendar.setOpaque(false);

        JPanel weekdays = new JPanel(new GridLayout(1, 7, 4, 0));
        weekdays.setOpaque(false);

        String[] weekdayNames = {
            "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
        };

        for (String name : weekdayNames) {
            JLabel day = new JLabel(name, SwingConstants.CENTER);
            day.setFont(calendarBold);
            day.setForeground(PINK_700);
            weekdays.add(day);
        }

        JPanel days = new JPanel(new GridLayout(6, 7, 5, 6));
        days.setOpaque(false);

        calendar.add(weekdays, BorderLayout.NORTH);
        calendar.add(days, BorderLayout.CENTER);

        center.add(calendar, BorderLayout.CENTER);
        main.add(center, BorderLayout.CENTER);

        // Bottom controls
        JPanel bottom = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 12, 0)
        );
        bottom.setOpaque(false);

        PillButton todayButton = new PillButton(
                "Today", 13, 25, 9
        );
        todayButton.style(PINK_100, PINK_900, null, PINK_200);

        PillButton closeButton = new PillButton(
                "Close", 13, 25, 9
        );
        closeButton.style(PINK_800, Color.WHITE, null, PINK_900);

        bottom.add(todayButton);
        bottom.add(closeButton);

        main.add(bottom, BorderLayout.SOUTH);

        // Render dates as clickable labels, not Aqua buttons.
        Runnable render = () -> {

            days.removeAll();

            Calendar first = Calendar.getInstance();
            first.clear();
            first.set(year[0], month[0], 1);

            int offset = first.get(Calendar.DAY_OF_WEEK) - 1;
            int count = first.getActualMaximum(Calendar.DAY_OF_MONTH);

            Calendar today = Calendar.getInstance();
            clearTime(today);

            Calendar selected = Calendar.getInstance();
            selected.setTime(selectedDate);
            clearTime(selected);

            for (int i = 0; i < 42; i++) {

                int dayNumber = i - offset + 1;

                if (dayNumber < 1 || dayNumber > count) {
                    days.add(new JLabel(""));
                    continue;
                }

                Calendar cellDate = Calendar.getInstance();
                cellDate.clear();
                cellDate.set(year[0], month[0], dayNumber);

                boolean past = cellDate.before(today);
                boolean chosen = sameDay(cellDate, selected);

                JLabel cell = new JLabel(
                        String.valueOf(dayNumber),
                        SwingConstants.CENTER
                );

                cell.setFont(chosen ? calendarBold : calendarFont);
                cell.setOpaque(true);
                cell.setPreferredSize(new Dimension(40, 36));

                if (chosen) {
                    cell.setBackground(PINK_800);
                    cell.setForeground(Color.WHITE);
                } else if (past) {
                    cell.setBackground(new Color(247, 244, 245));
                    cell.setForeground(new Color(190, 180, 185));
                } else {
                    cell.setBackground(PINK_50);
                    cell.setForeground(PINK_950);
                }

                if (!past) {

                    cell.setCursor(Cursor.getPredefinedCursor(
                            Cursor.HAND_CURSOR
                    ));

                    cell.addMouseListener(new MouseAdapter() {

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            if (!chosen) {
                                cell.setBackground(PINK_200);
                            }
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            if (!chosen) {
                                cell.setBackground(PINK_50);
                            }
                        }

                        @Override
                        public void mouseClicked(MouseEvent e) {

                            Calendar picked = Calendar.getInstance();
                            picked.clear();
                            picked.set(year[0], month[0], dayNumber);

                            selectedDate = picked.getTime();

                            dateLabel.setText(
                                    displayDateFormat.format(selectedDate)
                            );

                            dialog.dispose();
                        }
                    });
                }

                days.add(cell);
            }

            days.revalidate();
            days.repaint();
        };

        monthBox.addActionListener(e -> {
            month[0] = monthBox.getSelectedIndex();
            render.run();
        });

        yearBox.addActionListener(e -> {
            year[0] = (Integer) yearBox.getSelectedItem();
            render.run();
        });

        todayButton.addActionListener(e -> {
            selectedDate = new Date();
            dateLabel.setText(displayDateFormat.format(selectedDate));
            dialog.dispose();
        });

        closeButton.addActionListener(e -> dialog.dispose());

        render.run();

        dialog.setContentPane(main);
        dialog.setSize(430, 490);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void clearTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private boolean sameDay(Calendar a, Calendar b) {
        return a.get(Calendar.YEAR) == b.get(Calendar.YEAR)
                && a.get(Calendar.MONTH) == b.get(Calendar.MONTH)
                && a.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH);
    }

    // ===================== Confirmation Dialog =====================

    private boolean showConfirmationDialog(String date, String time) {

        final boolean[] confirmed = {false};

        JDialog dialog = new JDialog(
                this, "Confirm Appointment", true
        );

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(490, 490);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(Color.WHITE);
        main.setBorder(new EmptyBorder(35, 40, 30, 40));

        JLabel title = label(
                "Almost There!", heading(29), PINK_950
        );
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = label(
                "Please review your appointment details.",
                text(14), PINK_800
        );
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.add(title);
        main.add(Box.createVerticalStrut(10));
        main.add(subtitle);
        main.add(Box.createVerticalStrut(25));

        RoundedPanel details = new RoundedPanel(20, PINK_50, true);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setBorder(new EmptyBorder(22, 22, 22, 22));
        details.setMaximumSize(new Dimension(410, 180));

        addDetail(details, "SERVICE", serviceName);
        addDetail(details, "SPECIALIST", employeeName);
        addDetail(details, "DATE", date);
        addDetail(details, "TIME", time);

        main.add(details);
        main.add(Box.createVerticalStrut(27));

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 12, 0)
        );
        buttons.setOpaque(false);

        PillButton back = new PillButton("Go Back", 14, 25, 10);
        back.style(PINK_100, PINK_900, null, PINK_200);

        PillButton confirm = new PillButton(
                "Confirm Booking", 14, 25, 10
        );
        confirm.style(PINK_800, Color.WHITE, null, PINK_900);

        back.addActionListener(e -> dialog.dispose());

        confirm.addActionListener(e -> {
            confirmed[0] = true;
            dialog.dispose();
        });

        buttons.add(back);
        buttons.add(confirm);
        main.add(buttons);

        dialog.setContentPane(main);
        dialog.setVisible(true);

        return confirmed[0];
    }

    private void addDetail(JPanel panel, String title, String value) {

        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(370, 30));

        JLabel titleLabel = label(title, text(12), PINK_700);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        valueLabel.setForeground(PINK_950);
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(titleLabel, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);

        panel.add(row);
        panel.add(Box.createVerticalStrut(9));
    }

    // ===================== Success Dialog =====================

    private void showSuccessDialog() {

        JDialog dialog = new JDialog(
                this, "Booking Successful", true
        );

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(440, 370);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(Color.WHITE);
        main.setBorder(new EmptyBorder(35, 35, 30, 35));

        JLabel confirmed = label(
                "BOOKING CONFIRMED", heading(15), PINK_700
        );
        confirmed.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = label(
                "You're All Set!", heading(30), PINK_950
        );
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = htmlLabel(
                "Your appointment has been booked successfully.<br>"
                + "We can't wait to see you!",
                330, "center", text(15), PINK_800
        );
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel service = label(
                serviceName + " with " + employeeName,
                text(14), PINK_700
        );
        service.setAlignmentX(Component.CENTER_ALIGNMENT);

        PillButton done = new PillButton("Done", 15, 35, 10);
        done.style(PINK_800, Color.WHITE, null, PINK_900);
        done.setAlignmentX(Component.CENTER_ALIGNMENT);
        done.addActionListener(e -> dialog.dispose());

        main.add(confirmed);
        main.add(Box.createVerticalStrut(20));
        main.add(title);
        main.add(Box.createVerticalStrut(12));
        main.add(subtitle);
        main.add(Box.createVerticalStrut(18));
        main.add(service);
        main.add(Box.createVerticalStrut(25));
        main.add(done);

        dialog.setContentPane(main);
        dialog.setVisible(true);
    }

    // ===================== Booking =====================

    private void confirmBooking() {

        String date = storageDateFormat.format(selectedDate);
        String time = (String) timeCombo.getSelectedItem();

        if (showConfirmationDialog(date, time)) {

            MyAppointments.addAppointment(
                    serviceName,
                    employeeName,
                    date,
                    time
            );

            showSuccessDialog();

            new MyAppointments().setVisible(true);
            dispose();
        }
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
