package nl.windesheim.ictm2o.peach.design;

import nl.windesheim.ictm2o.peach.DesignPage;
import nl.windesheim.ictm2o.peach.PeachWindow;
import nl.windesheim.ictm2o.peach.algorithm.BestAlgorithm;
import nl.windesheim.ictm2o.peach.components.ComponentRegistry;

import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.components.PlacedComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


public class DPToevCompon extends JPanel implements ActionListener {
    private final JButton toevoegen;
    private final ComponentRegistry CR;
    private final DesignPage mainFrame;
    private final Design D;
    private final JButton terugKnop;
    private final JButton optimaliseren;
    private final PeachWindow m_parent;

    private final JLabel beschikbaarheid = new JLabel("Beschikbaarheid: 0%");

    private final String[] columnNames = {
            "SERVER_WEB",
            "SERVER_DATABASE",
            "FIREWALL",
            "ROUTER",
            "GENERIC",
            "TOTAAL"};
    private Object[][] data = {
            {0,0,0,0,0,0},
    };

    JTable table = new JTable(data, columnNames);
    JScrollPane scrollPane = new JScrollPane();

    Font font1 = new Font("Inter", Font.BOLD, 15);

    public DPToevCompon(ComponentRegistry CR, DesignPage mainFrame, PeachWindow m_parent, Design D) {
        this.m_parent = m_parent;
        this.CR = CR;
        this.mainFrame = mainFrame;
        this.D = D;
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

        beschikbaarheid.setFont(font1);
        beschikbaarheid.setText("Beschikbaarheid: " + 100*D.getAvailbility(D.getPlacedComponents()) + "%");
        add(beschikbaarheid);

        scrollPane.setViewportView(table);
        scrollPane.setPreferredSize(new Dimension(400,50));
        add(scrollPane);

    }

    public void refreshGegevens(){
        beschikbaarheid.setText("Beschikbaarheid: " + 100*D.getAvailbility(D.getPlacedComponents()) + "%");

        data = new Object[][]{
                {D.getKosten(D.getPlacedComponents())[0],
                        D.getKosten(D.getPlacedComponents())[1],
                        D.getKosten(D.getPlacedComponents())[2],
                        D.getKosten(D.getPlacedComponents())[3],
                        D.getKosten(D.getPlacedComponents())[4],
                        D.getKosten(D.getPlacedComponents())[5]}
        };
        remove(table);
        JTable table = new JTable(data, columnNames);

        scrollPane.setViewportView(table);
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
            BestAlgorithm BA = new BestAlgorithm(D);
            //BA.vindAv(); OUD ALGORITME

            BA.optiMalisatie();
            //Update alles
            refreshGegevens();
            mainFrame.getComponPanel().refreshPanel();
            mainFrame.getWorkPanel().refreshWP();
        }

    }
}
