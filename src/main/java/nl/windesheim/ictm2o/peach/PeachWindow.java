package nl.windesheim.ictm2o.peach;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class PeachWindow extends JFrame {

    public PeachWindow() throws IOException {
        super("Windesheim Peach");

        setThemeToSystem();
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            setAppleIcon();
        } else {
            setIcon();
        }

        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (info.getClassName().equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private void setIcon() {
        try {
            setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Peach.png"))).getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAppleIcon() {
        final Taskbar taskbar = Taskbar.getTaskbar();
        taskbar.setIconImage(new ImageIcon(Objects.requireNonNull(PeachWindow.class.getResource("/Peach.png"))).getImage());
        setIconImage(new ImageIcon(Objects.requireNonNull(PeachWindow.class.getResource("/Peach.png"))).getImage());
    }

    private void setMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("Bestand");
        JMenuItem item = new JMenuItem("Nieuw Model");
        JMenuItem item1 = new JMenuItem("Opslaan");
        JMenuItem item2 = new JMenuItem("Opslaan Als");
        JMenuItem item3 = new JMenuItem("Dienstmonitoring");
        JMenuItem afsluiten = new JMenuItem("Afsluiten");
        afsluiten.addActionListener(e -> {
            // TODO check of het ontwerp is opgeslagen.
            System.exit(0);
        });

        file.add(item);
        file.add(item1);
        file.add(item2);
        file.add(item3);
        file.add(afsluiten);
        menuBar.add(file);
        setJMenuBar(menuBar);
    }
}
