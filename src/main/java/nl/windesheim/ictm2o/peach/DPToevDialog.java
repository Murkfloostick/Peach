package nl.windesheim.ictm2o.peach;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DPToevDialog extends JDialog implements ActionListener {
    //Options moet worden opgehaald uit iconenlijst?
    private String[] optionsToChoose = {"Apple", "Orange", "Banana", "Pineapple", "None of the listed"};

    private JComboBox options = new JComboBox(optionsToChoose);
    private JTextField naam = new JTextField(5);
    private JButton toevoegen = new JButton("Toevoegen");
    private JButton cancel = new JButton("Annuleren");

    public DPToevDialog(JFrame frame, boolean modal){
        super(frame, modal);
        setSize(200,100);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Toevoegen component");
        add(options);
        add(naam);
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
        }
        if(e.getSource() == cancel){
            dispose();
        }
    }
}
