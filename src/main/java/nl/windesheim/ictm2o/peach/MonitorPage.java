package nl.windesheim.ictm2o.peach;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MonitorPage extends JPanel implements ActionListener {
    private JButton knopTestHelloWorld;
    private JButton disposeKnop;

    public MonitorPage() {
        knopTestHelloWorld = new JButton("Hello Test World Ok");
        knopTestHelloWorld.setFont(new Font("Arial", Font.BOLD, 35));
        disposeKnop = new JButton("Terug");
        disposeKnop.setFont(new Font("Arial", Font.BOLD, 35));
        add(knopTestHelloWorld);
        add(disposeKnop);
        setLayout(new FlowLayout());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == disposeKnop){
          System.exit(0);
      }
    }
}
