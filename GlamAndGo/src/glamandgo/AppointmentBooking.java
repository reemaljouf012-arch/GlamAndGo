
package glamandgo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppointmentBooking extends JFrame {

    private final String serviceName;
    private final String employeeName;

    private final Color PINK = new Color(190, 95, 125);
    private final Color DARK = new Color(65, 45, 55);
    private final Color LIGHT = new Color(255, 244, 248);

    private JSpinner dateSpinner;
    private JComboBox<String> timeCombo;

    public AppointmentBooking(String serviceName, String employeeName) {
        this.serviceName = serviceName;
        this.employeeName = employeeName;

        setTitle("Glam & Go - Appointment Booking");
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildPage();
    }

    private void buildPage() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(LIGHT);

        JPanel header = new JPanel();
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(35, 20, 30, 20));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Book Your Appointment");
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Choose your preferred date and time."
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setForeground(PINK);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(12));
        header.add(subtitle);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(35, 45, 35, 45));

        form.add(createInfoLabel("Selected Service"));
        form.add(createValueLabel(serviceName));

        form.add(Box.createVerticalStrut(22));

        form.add(createInfoLabel("Selected Employee"));
        form.add(createValueLabel(employeeName));

        form.add(Box.createVerticalStrut(30));

        form.add(createInfoLabel("Choose Appointment Date"));

        SpinnerDateModel dateModel = new SpinnerDateModel(
                new Date(), new Date(), null,
                java.util.Calendar.DAY_OF_MONTH
        );

        dateSpinner = new JSpinner(dateModel);

 JSpinner.DateEditor dateEditor =
        new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");

dateEditor.getFormat().setCalendar(
        java.util.Calendar.getInstance(
                java.util.Locale.US
        )
);

dateSpinner.setEditor(dateEditor);
dateEditor.getTextField().setComponentOrientation(
        ComponentOrientation.LEFT_TO_RIGHT
);
        form.add(dateSpinner);

        form.add(Box.createVerticalStrut(25));

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
        timeCombo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        timeCombo.setMaximumSize(new Dimension(500, 42));
        timeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(timeCombo);

        form.add(Box.createVerticalStrut(35));

        JButton confirmButton = new JButton("Confirm Booking");
        confirmButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBackground(PINK);
        confirmButton.setFocusPainted(false);
        confirmButton.setBorder(new EmptyBorder(14, 35, 14, 35));
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmButton.addActionListener(e -> confirmBooking());

        form.add(confirmButton);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(LIGHT);
        center.setBorder(new EmptyBorder(30, 20, 30, 20));

        form.setPreferredSize(new Dimension(520, 480));
        center.add(form);

        main.add(header, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);

        setContentPane(main);
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(PINK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Serif", Font.BOLD, 19));
        label.setForeground(DARK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

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
}
