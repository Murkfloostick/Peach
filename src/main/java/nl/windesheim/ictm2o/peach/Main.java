package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.monitor.MonitorPage;
import nl.windesheim.ictm2o.peach.monitor.MonitorServer;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

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

        peachWindow.addWindowStateListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (MonitorPage.instance != null)
                    MonitorPage.instance.backToStartPage();
                MonitorServer.getInstance().stop();
                System.exit(0);
            }
        });

        peachWindow.run();
    }
}
