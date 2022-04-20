package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.ComponentIcon;
import nl.windesheim.ictm2o.peach.components.ComponentRegistry;
import nl.windesheim.ictm2o.peach.components.RegisteredComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class DPToevDialog extends JDialog implements ActionListener {
    //Options moet worden opgehaald uit iconenlijst?
    private String[] optionsToChoose;

    private JComboBox options;
    private JTextField naam = new JTextField(5);
    private JTextField prijs = new JTextField(5);
    private JButton toevoegen = new JButton("Toevoegen");
    private JButton cancel = new JButton("Annuleren");
    private JLabel labelNaam = new JLabel("Naam");
    private JLabel labelPrijs = new JLabel("Prijs");

    private ComponentRegistry CR;
    public DPToevDialog(JFrame frame, boolean modal, ComponentRegistry CR){
        super(frame, modal);
        setSize(350,110);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Toevoegen component");

        //Haal icoontjes op
        optionsToChoose = new String[ComponentIcon.values().length];
        int counter = 0;
        for (ComponentIcon IC:ComponentIcon.values()
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
        add(toevoegen);
        toevoegen.addActionListener(this);
        add(cancel);
        cancel.addActionListener(this);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == toevoegen){
            //Voeg component toe aan lijst en refresh JPanel
            //Eerst componentregistry hier helemaal naartoe halen en dan nieuw component toevoegen

            UUID uuid=UUID.randomUUID();
            //RegisteredComponent newComponent = new RegisteredComponent(uuid, naam.getText(), ComponentIcon.GENERIC, prijs.getText());
            //CR.getRegisteredComponents().add(newComponent);
            dispose();
        }
        if(e.getSource() == cancel){
            dispose();
        }
    }
}
