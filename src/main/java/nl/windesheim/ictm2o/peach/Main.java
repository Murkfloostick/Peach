package nl.windesheim.ictm2o.peach;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Test");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        PeachWindow peachWindow = new PeachWindow();
        peachWindow.run();
    }
}
