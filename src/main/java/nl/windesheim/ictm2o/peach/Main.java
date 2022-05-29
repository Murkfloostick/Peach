package nl.windesheim.ictm2o.peach;

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

public class Main {

    public static final Locale LOCALE = Locale.forLanguageTag("nl");


    public static void main(String[] args) throws Exception {
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

        PeachWindow peachWindow = new PeachWindow();
        peachWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Fullscreen openen
        peachWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Deze alleen voor zonder borders: (nu false)
        peachWindow.setUndecorated(false);
        peachWindow.setVisible(true);

        MonitorDataManager.attachInstanceOfflineAlertCallback((identifier) ->
                new Toast(peachWindow, identifier + " is offline!"));
        MonitorDataManager.attachInstanceOnlineAlertCallback((identifier) ->
                new Toast(peachWindow, identifier + " is online!"));

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
