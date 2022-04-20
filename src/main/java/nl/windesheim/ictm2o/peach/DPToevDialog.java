package nl.windesheim.ictm2o.peach;

import javax.swing.*;

public class DPToevDialog extends JDialog {
    public DPToevDialog(JFrame frame, boolean modal){
        super(frame, modal);
        setSize(200,200);
        setTitle("Mijn Dialoog");
        setVisible(true);
    }
}
