package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.algorithm.BestAlgorithm;
import nl.windesheim.ictm2o.peach.components.ComponentRegistry;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nl.windesheim.ictm2o.peach.components.RegisteredComponent;
import javax.swing.border.LineBorder;
import java.io.IOException;

public class DPToevCompon extends JPanel implements ActionListener {
    private JButton toevoegen;
    private ComponentRegistry CR;
    private DesignPage mainFrame;
    private JButton terugKnop;
    private JButton optimaliseren;
    private PeachWindow m_parent;


    Font font1 = new Font("Inter", Font.BOLD, 15);

    public DPToevCompon(ComponentRegistry CR, DesignPage mainFrame, PeachWindow m_parent) {
        this.m_parent = m_parent;
        this.CR = CR;
        this.mainFrame = mainFrame;
        this.setVisible(true);
        setBackground(Color.gray);
        //OG: 200-550
        setPreferredSize(new Dimension(350, 600));

        JTextField beschikbaarheidField = new JTextField();
        beschikbaarheidField.setText("99,99%");
        add(beschikbaarheidField);

        toevoegen = new JButton("Component toevoegen");
        toevoegen.setFont(font1);
        toevoegen.setContentAreaFilled(true);
        add(toevoegen);
        terugKnop = new JButton("Terug");
        terugKnop.setFont(font1);
        terugKnop.setContentAreaFilled(true);
        add(terugKnop);
        optimaliseren = new JButton("Optimaliseren");
        optimaliseren.setFont(font1);
        optimaliseren.setContentAreaFilled(true);
        add(optimaliseren);
        toevoegen.addActionListener(this);
        terugKnop.addActionListener(this);
        optimaliseren.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == toevoegen) {
            //DPToevDialog dialoog = new DPToevDialog(this, true);
            //Anders werkt hij niet, dialoog moet op parent frame worden attached
            Window parentWindow = SwingUtilities.windowForComponent(this);
            JFrame parentFrame = null;
            if (parentWindow instanceof Frame) {
                parentFrame = (JFrame) parentWindow;
            }
            DPToevDialog dialoog = new DPToevDialog(parentFrame, true, CR, mainFrame);
            dialoog.setLocationRelativeTo(null);

        } else if (e.getSource() == terugKnop) {
            m_parent.openStartPage(mainFrame);
        } else if(e.getSource() == optimaliseren){
            //Algorithm hieronder
            //Zie nieuwe aangemaakte class voor toekomstige uitwerking
            new BestAlgorithm();
        }

    }
}
