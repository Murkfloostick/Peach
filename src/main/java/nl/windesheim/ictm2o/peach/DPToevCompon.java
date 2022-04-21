package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.ComponentRegistry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DPToevCompon extends JPanel implements ActionListener {
    private JButton toevoegen;
    private ComponentRegistry CR;
    private DesignPage mainFrame;

    public DPToevCompon(ComponentRegistry CR, DesignPage mainFrame){
        this.CR = CR;
        this.mainFrame = mainFrame;
        setBackground(Color.gray);
        setPreferredSize(new Dimension(200, 550));
        toevoegen = new JButton("Component toevoegen");
        add(toevoegen);
        toevoegen.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //DPToevDialog dialoog = new DPToevDialog(this, true);
        //Anders werkt hij niet, dialoog moet op parent frame worden attached
        Window parentWindow = SwingUtilities.windowForComponent(this);
        JFrame parentFrame = null;
        if (parentWindow instanceof Frame) {
            parentFrame = (JFrame)parentWindow;
        }
        DPToevDialog dialoog = new DPToevDialog(parentFrame, true, CR, mainFrame);
        dialoog.setLocationRelativeTo(null);
    }
}
