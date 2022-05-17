package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.monitor.MonitorServer;

import javax.swing.*;

public class Main {
    public static MonitorServer monitorServer = new MonitorServer();

    public static void main(String[] args) throws Exception {
        new Thread(() -> monitorServer.start()).start();

        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Test");

        PeachWindow peachWindow = new PeachWindow();
        //Fullscreen openen
        peachWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Deze alleen voor zonder borders: (nu false)
        peachWindow.setUndecorated(false);
        peachWindow.setVisible(true);

        peachWindow.run();
    }
}
