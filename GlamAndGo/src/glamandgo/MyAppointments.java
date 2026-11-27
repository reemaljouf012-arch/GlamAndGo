
package glamandgo;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static glamandgo.Theme.*;

public class MyAppointments extends JFrame {

    // Appointment Class
    static class Appointment {

        final String serviceName;
        final String employeeName;
        final String date;
        final String time;

        String status;

        Appointment(String serviceName, String employeeName,
                String date, String time) {

            this.serviceName = serviceName;
            this.employeeName = employeeName;
            this.date = date;
            this.time = time;
            this.status = "Upcoming";
        }
    }

    // Shared Appointment Storage
    static final ArrayList<Appointment> appointments =
            new ArrayList<>();

    // Add Appointment From Booking Page
    public static void addAppointment(
            String serviceName,
            String employeeName,
            String date,
            String time) {

        appointments.add(new Appointment(
                serviceName,
                employeeName,
                date,
                time
        ));
    }

    private final JPanel appointmentsPanel = vbox();

    // ===================== Constructor =====================

    public MyAppointments() {

        setTitle("Glam & Go | My Appointments");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(1280, 820);

        setMinimumSize(new Dimension(1100, 600));

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(navbar(this::navigate), BorderLayout.NORTH);

        PageBody body = new PageBody();

        body.add(buildTitle());

        body.add(buildAppointments());

        body.add(footer(this::navigate));

        JScrollPane scroll = new JScrollPane(
                body,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scroll.setBorder(null);

        scroll.getVerticalScrollBar().setUnitIncrement(24);

        add(scroll, BorderLayout.CENTER);

        refreshAppointments();
    }

    // ===================== Page Title =====================

    private JComponent buildTitle() {

        JPanel content = vbox();

        content.add(sectionTitle(
                "My Appointments",
                "Keep track of your beauty appointments.",
                PINK_900,
                PINK_800
        ));

        return new Section(
                PINK_50,
                55,
                1200,
                content
        );
    }

    // ===================== Appointments Section =====================

    private JComponent buildAppointments() {

        JPanel content = vbox();

        content.add(appointmentsPanel);

        return new Section(
                Color.WHITE,
                40,
                1000,
                content
        );
    }

    // ===================== Refresh Appointments =====================

    private void refreshAppointments() {

        appointmentsPanel.removeAll();

        if (appointments.isEmpty()) {

            appointmentsPanel.add(buildEmptyState());

        } else {

            for (int i = 0; i < appointments.size(); i++) {

                Appointment appointment = appointments.get(i);

                appointmentsPanel.add(
                        buildAppointmentCard(appointment)
                );

                if (i < appointments.size() - 1) {

                    appointmentsPanel.add(
                            Box.createVerticalStrut(22)
                    );
                }
            }
        }

        appointmentsPanel.revalidate();

        appointmentsPanel.repaint();
    }

    // ===================== Empty State =====================

    private JComponent buildEmptyState() {

        RoundedPanel card = new RoundedPanel(
                28,
                PINK_50,
                true
        );

        card.setLayout(new BoxLayout(
                card,
                BoxLayout.Y_AXIS
        ));

        card.setBorder(new EmptyBorder(
                55, 30, 55, 30
        ));

        JLabel title = label(
                "No Appointments Yet",
                heading(30f),
                PINK_900
        );

        JLabel message = htmlLabel(
                "You haven't booked any appointments yet. "
                + "Explore our services and book your next beauty session.",
                480,
                "center",
                text(17f),
                PINK_800
        );

        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        PillButton bookButton = new PillButton(
                "Explore Services",
                16f,
                28,
                11
        ).style(
                PINK_800,
                Color.WHITE,
                null,
                PINK_950
        );

        bookButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        bookButton.addActionListener(e -> {
            new Services().setVisible(true);
            dispose();
        });

        card.add(title);

        card.add(Box.createVerticalStrut(14));

        card.add(message);

        card.add(Box.createVerticalStrut(25));

        card.add(bookButton);

        return card;
    }

    // ===================== Appointment Card =====================

    private JComponent buildAppointmentCard(
            Appointment appointment) {

        RoundedPanel card = new RoundedPanel(
                26,
                PINK_50,
                true
        );

        card.setLayout(new BorderLayout(25, 0));

        int s = RoundedPanel.SH;

        card.setBorder(new EmptyBorder(
                s + 24,
                s + 28,
                s + 24,
                s + 28
        ));

        // Appointment Information

        JPanel info = vbox();

        JLabel service = label(
                appointment.serviceName,
                heading(25f),
                PINK_900
        );

        JLabel employee = label(
                "Specialist: " + appointment.employeeName,
                text(16f),
                PINK_800
        );

        JLabel date = label(
                "Date: " + appointment.date,
                text(16f),
                PINK_800
        );

        JLabel time = label(
                "Time: " + appointment.time,
                text(16f),
                PINK_800
        );

        for (JComponent component : new JComponent[]{
                service, employee, date, time
        }) {

            component.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        info.add(service);

        info.add(Box.createVerticalStrut(12));

        info.add(employee);

        info.add(Box.createVerticalStrut(8));

        info.add(date);

        info.add(Box.createVerticalStrut(8));

        info.add(time);

        card.add(info, BorderLayout.CENTER);

        // Status and Actions

        JPanel actions = vbox();

        JLabel status = label(
                appointment.status,
                heading(17f),
                appointment.status.equals("Upcoming")
                        ? PINK_800 : PINK_500
        );

        status.setAlignmentX(Component.CENTER_ALIGNMENT);

        actions.add(status);

        actions.add(Box.createVerticalStrut(22));

        if (appointment.status.equals("Upcoming")) {

            PillButton cancelButton = new PillButton(
                    "Cancel Booking",
                    14f,
                    20,
                    9
            ).style(
                    Color.WHITE,
                    PINK_800,
                    PINK_300,
                    PINK_100
            );

            cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            cancelButton.addActionListener(e ->
                    cancelAppointment(appointment)
            );

            actions.add(cancelButton);
        }

        card.add(actions, BorderLayout.EAST);

        return card;
    }

    // ===================== Cancel Appointment =====================

    private void cancelAppointment(Appointment appointment) {

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel this appointment?",
                "Cancel Appointment",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {

            appointment.status = "Cancelled";

            refreshAppointments();
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

    // ===================== Main =====================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new MyAppointments().setVisible(true)
        );
    }
}
