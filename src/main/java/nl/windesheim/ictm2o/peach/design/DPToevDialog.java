package nl.windesheim.ictm2o.peach.design;

import nl.windesheim.ictm2o.peach.DesignPage;
import nl.windesheim.ictm2o.peach.components.ComponentIcon;
import nl.windesheim.ictm2o.peach.components.ComponentRegistry;
import nl.windesheim.ictm2o.peach.components.RegisteredComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class DPToevDialog extends JDialog implements ActionListener {

    private final JComboBox options;
    private final JTextField naam = new JTextField(5);
    private final JTextField prijs = new JTextField(5);
    private final JTextField beschikbaarheid = new JTextField(5);
    private final JButton toevoegen = new JButton("Toevoegen");
    private final JButton cancel = new JButton("Annuleren");

    private final ComponentRegistry CR;
    private final DesignPage mainFrame;

    public DPToevDialog(JFrame frame, boolean modal, ComponentRegistry CR, DesignPage mainFrame) {
        super(frame, modal);
        this.CR = CR;
        this.mainFrame = mainFrame;
        setSize(350, 110);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Toevoegen component");

        //Haal icoontjes op
        //Options moet worden opgehaald uit iconenlijst?
        String[] optionsToChoose = new String[ComponentIcon.values().length];
        int counter = 0;
        for (ComponentIcon IC : ComponentIcon.values()
        ) {
            optionsToChoose[counter] = IC.name();
            counter += 1;
        }
        options = new JComboBox(optionsToChoose);
        add(options);

        JLabel labelNaam = new JLabel("Naam");
        add(labelNaam);
        add(naam);
        JLabel labelPrijs = new JLabel("Prijs");
        add(labelPrijs);
        add(prijs);
        JLabel labelBeschikbaarheid = new JLabel("Beschikbaarheid");
        add(labelBeschikbaarheid);
        add(beschikbaarheid);
        add(toevoegen);
        toevoegen.addActionListener(this);
        add(cancel);
        cancel.addActionListener(this);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == toevoegen) {
            UUID uuid = UUID.randomUUID();
            ComponentIcon CI = ComponentIcon.GENERIC;
            for (ComponentIcon IC : ComponentIcon.values()
            ) {
                if (IC.name().equals(options.getSelectedItem())) {
                    CI = IC;
                    break;
                }
            }
            RegisteredComponent newComponent = new RegisteredComponent(uuid, naam.getText(), CI, Float.parseFloat(prijs.getText()), Float.parseFloat(beschikbaarheid.getText()));
            CR.getRegisteredComponents().add(newComponent);
            mainFrame.getPeachWindow().getConfiguration().save();
            mainFrame.getComponPanel().refreshPanel();
            dispose();
            mainFrame.setDesignModified();
        }
        if (e.getSource() == cancel) {
            dispose();
        }
    }
}
