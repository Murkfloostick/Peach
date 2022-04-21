package nl.windesheim.ictm2o.peach;

import javax.swing.*;

import static nl.windesheim.ictm2o.peach.DPWorkPanel.setLocationRelativeTo;

public class DesignerPage extends JPanel {

    public DesignerPage() {
        DesignPage DP = new DesignPage();


        //Misschien een goed idee? Bij new DesignerPage gelijk fullscreen:
        DP.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Deze alleen voor zonder borders: (nu false)
        DP.setUndecorated(false);
        DP.setVisible(true);
    }

}
