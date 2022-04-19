package nl.windesheim.ictm2o.peach;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DesignPage extends JFrame implements ActionListener {
    private DPComponPanel componPanel;
    private DPWorkPanel workPanel;
    private DPToevCompon toevCompon;

    public DesignPage(){
    componPanel = new DPComponPanel();
    workPanel = new DPWorkPanel();
    toevCompon = new DPToevCompon();

    setTitle("Ontwerp");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new FlowLayout());
    setSize(750, 300);

    add(componPanel);
    add(workPanel);
    add(toevCompon);

    setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
