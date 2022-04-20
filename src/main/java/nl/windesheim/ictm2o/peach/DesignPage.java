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
    JScrollPane scroller = new JScrollPane(componPanel);
    this.getContentPane().add(scroller);

    setTitle("Ontwerp");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    setSize(950, 600);

    c.fill = GridBagConstraints.VERTICAL;
    c.gridx = 0;
    c.gridy = 0;

    add(componPanel, c);
    c.gridx = 1;
    add(workPanel, c);
    c.gridx = 2;
    add(toevCompon, c);

    setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
