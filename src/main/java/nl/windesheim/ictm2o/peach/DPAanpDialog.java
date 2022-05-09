package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.ComponentIcon;
import nl.windesheim.ictm2o.peach.components.ComponentRegistry;
import nl.windesheim.ictm2o.peach.components.RegisteredComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class DPAanpDialog extends JDialog implements ActionListener {
    //Options moet worden opgehaald uit iconenlijst?
    private String[] optionsToChoose;

    private JComboBox options;
    private JTextField naam = new JTextField(5);
    private JTextField prijs = new JTextField(5);
    private JTextField beschikbaarheid = new JTextField(5);
    private JButton toevoegen = new JButton("Aanpassen");
    private JButton cancel = new JButton("Annuleren");
    private JLabel labelNaam = new JLabel("Naam");
    private JLabel labelPrijs = new JLabel("Prijs");
    private JLabel labelBeschikbaarheid = new JLabel("Beschikbaarheid");

    private ComponentRegistry CR;
    private DesignPage mainFrame;
    private RegisteredComponent RC;

    public DPAanpDialog(JFrame frame, boolean modal, ComponentRegistry CR, DesignPage mainFrame, RegisteredComponent RC) {
        super(frame, modal);
        this.CR = CR;
        this.mainFrame = mainFrame;
        this.RC = RC;

        setSize(350, 110);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Aanpassen component");

        //Haal icoontjes op
        optionsToChoose = new String[ComponentIcon.values().length];
        int counter = 0;
        for (ComponentIcon IC : ComponentIcon.values()
        ) {
            optionsToChoose[counter] = IC.name();
            counter += 1;
        }
        options = new JComboBox(optionsToChoose);
        add(options);

        add(labelNaam);
        add(naam);
        add(labelPrijs);
        add(prijs);
        add(labelBeschikbaarheid);
        add(beschikbaarheid);
        add(toevoegen);
        toevoegen.addActionListener(this);
        add(cancel);
        cancel.addActionListener(this);
        setLocationRelativeTo(null);

        //Vul gegevens in van component
        int counter2 = 0;
        for (ComponentIcon IC : ComponentIcon.values()
        ) {
            if (RC.getIcon().equals(IC)) {
                break;
            }
            counter2 += 1;
        }
            options.setSelectedIndex(counter2);
            naam.setText(RC.getName());
            prijs.setText(String.valueOf(RC.getCost()));
            beschikbaarheid.setText(String.valueOf(RC.getAvailability()*100));

            setVisible(true);
        }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == toevoegen){
            UUID uuid=UUID.randomUUID();
            ComponentIcon CI = ComponentIcon.GENERIC;
            for (ComponentIcon IC:ComponentIcon.values()
            ) {
                if(IC.name().equals(options.getSelectedItem())){
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
        if(e.getSource() == cancel){
            dispose();
        }
    }
}
