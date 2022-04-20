package nl.windesheim.ictm2o.peach;

import javax.swing.*;
import java.awt.*;

public class DPToevDialog extends JDialog {
    private String[] optionsToChoose = {"Apple", "Orange", "Banana", "Pineapple", "None of the listed"};
    private JComboBox options = new JComboBox(optionsToChoose);
    private JTextField naam = new JTextField(5);
    private JButton toevoegen = new JButton("Toevoegen");
    private JButton cancel = new JButton("Annuleren");

    public DPToevDialog(JFrame frame, boolean modal){
        super(frame, modal);
        setSize(200,100);
        setLayout(new FlowLayout());
        setTitle("Toevoegen component");
        add(options);
        add(naam);
        add(toevoegen);
        add(cancel);
        setVisible(true);
    }
}
