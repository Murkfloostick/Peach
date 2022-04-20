package nl.windesheim.ictm2o.peach;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PeachWindow extends JFrame {

    public PeachWindow() {
        super("Windesheim Peach");

        setThemeToSystem();

        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new StartPage(this));
    }

    public void run() {
        setVisible(true);
    }

    public void openPage(JPanel origin, String title, JPanel panel) {
        setTitle("Windesheim Peach - " + title);

        remove(origin);
        add(panel);
        invalidate();
        revalidate();
        repaint();
    }

    private void setThemeToSystem() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

}
