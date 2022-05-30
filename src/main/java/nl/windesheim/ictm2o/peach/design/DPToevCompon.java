package nl.windesheim.ictm2o.peach.design;

import net.miginfocom.swing.MigLayout;
import nl.windesheim.ictm2o.peach.DesignPage;
import nl.windesheim.ictm2o.peach.Main;
import nl.windesheim.ictm2o.peach.PeachWindow;
import nl.windesheim.ictm2o.peach.algorithm.BestAlgorithm;
import nl.windesheim.ictm2o.peach.components.ComponentIcon;
import nl.windesheim.ictm2o.peach.components.ComponentRegistry;

import nl.windesheim.ictm2o.peach.components.Design;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.stream.IntStream;

public class DPToevCompon extends JPanel implements ActionListener {
    private final JButton toevoegen;
    private final ComponentRegistry CR;
    private final DesignPage mainFrame;
    private final Design D;
    private final JButton terugKnop;
    private final JButton afsluiten;
    private final JButton optimaliseren;
    private final PeachWindow m_parent;
    private final JTextField beschikbaarheidField;

    JTable table;
    private final JScrollPane scrollPane = new JScrollPane();

    public DPToevCompon(ComponentRegistry CR, DesignPage mainFrame, PeachWindow m_parent, Design D) {
        this.m_parent = m_parent;
        this.CR = CR;
        this.mainFrame = mainFrame;
        this.D = D;
        setVisible(true);
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
        Font font1 = new Font("Inter", Font.BOLD, 15);
        toevoegen.setFont(font1);
        toevoegen.setContentAreaFilled(true);
        //add(toevoegen);
        terugKnop = new JButton("Terug");
        terugKnop.setFont(font1);
        terugKnop.setContentAreaFilled(true);
        //add(terugKnop);
        afsluiten = new JButton("Afsluiten");
        afsluiten.setFont(font1);
        afsluiten.setContentAreaFilled(true);
        //add(afsluiten);
        optimaliseren = new JButton("Optimaliseren");
        optimaliseren.setFont(font1);
        optimaliseren.setContentAreaFilled(true);
        //add(optimaliseren);
        toevoegen.addActionListener(this);
        terugKnop.addActionListener(this);
        afsluiten.addActionListener(this);
        optimaliseren.addActionListener(this);

        refreshGegevens();

        JPanel paneel1 = new JPanel();
        paneel1.setLayout(new GridLayout(2,2));
        paneel1.add(toevoegen);
        paneel1.add(optimaliseren);
        paneel1.add(terugKnop);
        paneel1.add(afsluiten);
        paneel1.setVisible(true);

        add(paneel1);

        scrollPane.setViewportView(table);
//        scrollPane.setPreferredSize(new Dimension(400,100));
        scrollPane.setBackground(null);
        add(scrollPane);
    }

    public void refreshGegevens() {
        final var stats = D.getStatistics();

        JPanel panel = new JPanel();
        panel.setBackground(Color.gray);
        panel.setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));

        var data = new Object[2][1 + stats.availabilityPerCategory().length];
        var columnNames = new String[1 + stats.availabilityPerCategory().length];

        data[0][0] = "Kosten";
        data[1][0] = "Beschikbaarheid";
        columnNames[0] = "";

        for (int i = 0; i < stats.availabilityPerCategory().length; ++i) {
            columnNames[1 + i] = ComponentIcon.values()[i].getDisplayName();

            data[0][1 + i] = String.format(Main.LOCALE, "€ %.02f", stats.costsPerCategory()[i]);
            data[1][1 + i] = String.format(Main.LOCALE, "%.2f %%",stats.availabilityPerCategory()[i] * 100.0f);
        }

        table = new JTable(data, columnNames);
        panel.add(new JScrollPane(table), "wrap");

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

        labelTmp = new JLabel(String.format(Main.LOCALE, "€ %.02f", stats.totalCosts()));
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

        labelTmp = new JLabel(String.format(Main.LOCALE, "%.2f %%", stats.totalAvailability() * 100.0f));
        labelTmp.setFont(rightLabelFont);
        if (stats.totalAvailability() * 100.0f < D.getTargetAvailability())
            labelTmp.setForeground(Color.RED);
        else if (IntStream.range(0, stats.availabilityPerCategory().length)
                .mapToDouble(i -> stats.availabilityPerCategory()[i])
                .anyMatch(val -> val * 100.0f < D.getTargetAvailability())) {
            labelTmp.setForeground(Color.ORANGE);
        } else
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
            mainFrame.saveDesign(false);
            m_parent.openStartPage(mainFrame);
        } else if (e.getSource() == afsluiten) {
            System.exit(0);
        } else if (e.getSource() == optimaliseren) {
            //Algorithm hieronder
            //Zie nieuwe aangemaakte class voor toekomstige uitwerking
            BestAlgorithm BA = new BestAlgorithm(D);
            //BA.vindAv(); OUD ALGORITME
            //D.setTargetAvailability(Float.parseFloat(beschikbaarheidField.getText()));
            BA.optiMalisatie();
            //Update alles
            refreshGegevens();
            mainFrame.getComponPanel().refreshPanel();
            mainFrame.getWorkPanel().refreshWP();
        }

    }

    private void fillBeschikbaarheidFieldWithValueFromDesign() {
        beschikbaarheidField.setText(String.format(Locale.getDefault(), "%.02f %%", D.getTargetAvailability()));
        System.out.println(Locale.getDefault());
    }

}
