package nl.windesheim.ictm2o.peach;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PeachWindow extends JFrame {

    public PeachWindow() {
        super("Windesheim Peach");

        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new StartPage(this));
    }

    public void run() {
        setVisible(true);
    }

}
