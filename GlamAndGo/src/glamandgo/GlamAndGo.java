package glamandgo;

import javax.swing.SwingUtilities;

/**
 * Glam & Go - application entry point.
 * Run this class: it opens the Login page.
 */
public class GlamAndGo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LogIn().setVisible(true));
    }
}
