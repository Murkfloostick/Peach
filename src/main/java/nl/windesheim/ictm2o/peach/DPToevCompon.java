package nl.windesheim.ictm2o.peach;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DPToevCompon extends JPanel implements ActionListener {
    private JButton toevoegen;
    public DPToevCompon(){
        setBackground(Color.gray);
        setPreferredSize(new Dimension(200, 550));
        toevoegen = new JButton("Component toevoegen");
        add(toevoegen);
        toevoegen.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //DPToevDialog dialoog = new DPToevDialog(this, true);
    }
}
