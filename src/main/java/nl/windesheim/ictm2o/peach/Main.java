package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.Splashscreen.Splashscreen;
import nl.windesheim.ictm2o.peach.components.ComponentIcon;
import nl.windesheim.ictm2o.peach.monitor.MonitorDataManager;
import nl.windesheim.ictm2o.peach.monitor.MonitorPage;
import nl.windesheim.ictm2o.peach.monitor.MonitorServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class Main extends JPanel {
    static JLabel logo;
    static JProgressBar progressBar;

    static ImageIcon logoVenster;

    static final int MY_MINIMUM = 0;
    static final int MY_MAXIMUM = 100;

    public Main() {
        try {
            UIManager.put("ProgressBar.background", (new Color(255, 255, 255)));
            UIManager.put("ProgressBar.foreground", (new Color(230, 155, 134)));
            UIManager.put("ProgressBar.selectionBackground", (new Color(0, 0, 0)));
            UIManager.put("ProgressBar.selectionForeground", (new Color(255, 255, 255)));

            //Initializing
            progressBar = new JProgressBar();
            progressBar.setMinimum(MY_MINIMUM);
            progressBar.setMaximum(MY_MAXIMUM);
            progressBar.setBorderPainted(false);
            progressBar.setStringPainted(true);
            progressBar.setFont(new Font("Inter", Font.BOLD, 15)); //Font percentage

            logo = new JLabel(new ImageIcon("src/main/resources/Peach.png"));
            JLabel tekst = new JLabel("PeachOS V" + BuildInfo.getVersion());
            tekst.setFont(new Font("Inter", Font.BOLD, 25));

            add(tekst);
            add(logo);
            add(progressBar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBar(int newValue) {
        progressBar.setValue(newValue);
    }

    public static final Locale LOCALE = Locale.forLanguageTag("nl");

    public static void main(String[] args) throws Exception {
        final Splashscreen j = new Splashscreen();
        PeachWindow peachWindow = new PeachWindow();

        JFrame frame = new JFrame("Launching PeachOS V" + BuildInfo.getVersion());
        frame.setFont(new Font("Inter", Font.BOLD, 25));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(j);
        frame.pack();
        frame.setSize(350, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        logoVenster = new ImageIcon("src/main/resources/Peach.png");
        frame.setIconImage(logoVenster.getImage());

        for (int i = MY_MINIMUM; i <= MY_MAXIMUM; i++) {
            final int percent = i;
            try {
                SwingUtilities.invokeLater(() -> j.updateBar(percent));
                java.lang.Thread.sleep(20);
                if (i >= 53 && i <= 78) {
                    java.lang.Thread.sleep(45);
                    SwingUtilities.invokeLater(() -> j.updateBar(percent));

                }
                if (i >= 78 && i <= 84) {
                    java.lang.Thread.sleep(200);
                    SwingUtilities.invokeLater(() -> j.updateBar(percent));

                }
                if (i == MY_MAXIMUM) {
                    frame.dispose();
                    peachWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    peachWindow.setUndecorated(false);
                    peachWindow.setVisible(true);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        MonitorServer.startInBackground();

        final var ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final var fonts = new String[]{"Black", "Bold", "ExtraBold", "ExtraLight", "Light", "Medium", "Regular", "SemiBold", "Thin"};
        for (String string : fonts) {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Main.class.getResourceAsStream("/Inter/static/Inter-" + string + ".ttf"))));
        }
        // pre-load images
        for (final var componentIcon : ComponentIcon.values()) {
            new Thread(() -> {
                try {
                    componentIcon.getImageIcon();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Test");

        //PeachWindow peachWindow = new PeachWindow();
        //peachWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Fullscreen openen
        //peachWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Deze alleen voor zonder borders: (nu false)
        //peachWindow.setUndecorated(false);
        //peachWindow.setVisible(true);

        MonitorDataManager.attachInstanceOfflineAlertCallback((identifier) -> new Toast(peachWindow, identifier + " is offline!"));
        MonitorDataManager.attachInstanceOnlineAlertCallback((identifier) -> new Toast(peachWindow, identifier + " is online!"));

        peachWindow.addWindowStateListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (MonitorPage.instance != null)
                    MonitorPage.instance.backToStartPage();
                MonitorServer.getInstance().stop();
                System.exit(0);
            }
        });

        Timer timer = new Timer(1000, ev -> MonitorDataManager.tick());
        timer.start();


        peachWindow.run();
    }


}
