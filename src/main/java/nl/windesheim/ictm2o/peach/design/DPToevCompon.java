package nl.windesheim.ictm2o.peach.design;

import net.miginfocom.swing.MigLayout;
import nl.windesheim.ictm2o.peach.DesignPage;
import nl.windesheim.ictm2o.peach.Main;
import nl.windesheim.ictm2o.peach.PeachWindow;
import nl.windesheim.ictm2o.peach.algorithm.BestAlgorithm;
import nl.windesheim.ictm2o.peach.components.ComponentIcon;
import nl.windesheim.ictm2o.peach.components.ComponentRegistry;

import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.components.PlacedComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
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
    private final JTextField beschikbaarheidField;

    JTable table = null;
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

        beschikbaarheidField = new JTextField();
        fillBeschikbaarheidFieldWithValueFromDesign();
        beschikbaarheidField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent event) {
                String value = beschikbaarheidField.getText().trim();
                if (value.endsWith("%"))
                    value = value.substring(0, value.length() - 1).trim();
                value = value.replace(',', '.');
                try {
                    D.setTargetAvailability(Float.parseFloat(value));
                    // Make the value look pretty
                    fillBeschikbaarheidFieldWithValueFromDesign();
                    refreshGegevens();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(DPToevCompon.this, "We begrijpen dit getal niet. Probeer het opnieuw alstublieft.");
                    beschikbaarheidField.grabFocus();
                }
            }
        });
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

        refreshGegevens();

        scrollPane.setViewportView(table);
//        scrollPane.setPreferredSize(new Dimension(400,100));
        scrollPane.setBackground(null);
        add(scrollPane);
    }

    public void refreshGegevens(){
        final var stats = D.getStatistics();

        JPanel panel = new JPanel();
        panel.setBackground(Color.gray);
        panel.setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));

        var data = new Object[2][1 + stats.getAvailabilityPerCategory().length];
        var columnNames = new String[1 + stats.getAvailabilityPerCategory().length];

        data[0][0] = "Kosten";
        data[1][0] = "Beschikbaarheid";
        columnNames[0] = "";

        for (int i = 0; i < stats.getAvailabilityPerCategory().length; ++i) {
            columnNames[1 + i] = ComponentIcon.values()[i].getDisplayName();

            data[0][1 + i] = String.format(Locale.ITALIAN, "€ %.02f", stats.getCostsPerCategory()[i]);
            data[1][1 + i] = String.format(Locale.ITALIAN, "%.2f %%",stats.getAvailabilityPerCategory()[i] * 100.0f);
        }

        table = new JTable(data, columnNames);
        panel.add(table, "wrap");

        JPanel totalPanel = new JPanel();
        totalPanel.setBackground(Color.gray);
        totalPanel.setLayout(new MigLayout("", "", "[grow,fill]"));
        panel.add(totalPanel);

        Font leftLabelFont = new Font("Inter", Font.BOLD, 18);
        Font rightLabelFont = new Font("Inter", Font.PLAIN, 18);

        //
        // Totale Kosten
        //
        JLabel labelTmp = new JLabel("Totale Kosten: ");
        labelTmp.setFont(leftLabelFont);
        labelTmp.setForeground(Color.WHITE);
        totalPanel.add(labelTmp);

        labelTmp = new JLabel(String.format(Locale.ITALIAN, "€ %.02f", stats.getTotalCosts()));
        labelTmp.setFont(rightLabelFont);
        labelTmp.setForeground(Color.WHITE);
        totalPanel.add(labelTmp, "wrap");

        //
        // Totale Beschikbaarheid
        //
        labelTmp = new JLabel("Totale Beschikbaarheid: ");
        labelTmp.setFont(leftLabelFont);
        labelTmp.setForeground(Color.WHITE);
        totalPanel.add(labelTmp);

        labelTmp = new JLabel(String.format(Locale.ITALIAN, "%.2f %%", stats.getTotalAvailability() * 100.0f));
        labelTmp.setFont(rightLabelFont);
        if (stats.getTotalAvailability() * 100.0f < D.getTargetAvailability())
            labelTmp.setForeground(Color.RED);
        else
            labelTmp.setForeground(Color.GREEN);
        totalPanel.add(labelTmp, "wrap");

        scrollPane.setViewportView(panel);
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

    private void fillBeschikbaarheidFieldWithValueFromDesign() {
        beschikbaarheidField.setText(String.format(Main.LOCALE, "%.02f %%", D.getTargetAvailability()));
    }

}
