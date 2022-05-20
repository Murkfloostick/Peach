package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.monitor.MonitorDataManager;
import nl.windesheim.ictm2o.peach.monitor.MonitorPage;
import nl.windesheim.ictm2o.peach.monitor.MonitorServer;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

public class Main {

    public static final Locale LOCALE = Locale.forLanguageTag("nl");

    public static void main(String[] args) throws Exception {
        MonitorServer.startInBackground();

        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Test");

        PeachWindow peachWindow = new PeachWindow();
        peachWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Fullscreen openen
        peachWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Deze alleen voor zonder borders: (nu false)
        peachWindow.setUndecorated(false);
        peachWindow.setVisible(true);

        MonitorDataManager.attachInstanceOfflineAlertCallback((identifier, instance) ->
            new Toast(peachWindow, identifier + " is offline!"));
        MonitorDataManager.attachInstanceOnlineAlertCallback((identifier, instance) ->
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
