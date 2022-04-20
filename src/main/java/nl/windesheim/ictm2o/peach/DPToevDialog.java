package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.ComponentIcon;
import nl.windesheim.ictm2o.peach.components.ComponentRegistry;
import nl.windesheim.ictm2o.peach.components.RegisteredComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DPToevDialog extends JDialog implements ActionListener {
    //Options moet worden opgehaald uit iconenlijst?
    private String[] optionsToChoose = {"Apple", "Orange", "Banana", "Pineapple", "None of the listed"};

    private JComboBox options = new JComboBox(optionsToChoose);
    private JTextField naam = new JTextField(5);
    private JTextField prijs = new JTextField(5);
    private JButton toevoegen = new JButton("Toevoegen");
    private JButton cancel = new JButton("Annuleren");

    private ComponentRegistry CR;
    public DPToevDialog(JFrame frame, boolean modal, ComponentRegistry CR){
        super(frame, modal);
        setSize(300,110);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Toevoegen component");

        //Hardcoded icoontjes of ophalen?

        add(options);
        add(naam);
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

            //RegisteredComponent newComponent = new RegisteredComponent(, naam.getText(), ComponentIcon.GENERIC, prijs.getText());
            //CR.getRegisteredComponents().add(newComponent);
            dispose();
        }
        if(e.getSource() == cancel){
            dispose();
        }
    }
}
