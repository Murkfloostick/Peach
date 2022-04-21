package nl.windesheim.ictm2o.peach;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MonitorPage extends JPanel implements ActionListener {
    JButton knopTestHelloWorld;

    public MonitorPage() {
        knopTestHelloWorld = new JButton("Hello Test World Ok");
        add(knopTestHelloWorld);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == knopTestHelloWorld){
          System.exit(0);
      }
    }
}
