package nl.windesheim.ictm2o.peach.design;

import nl.windesheim.ictm2o.peach.DesignPage;
import nl.windesheim.ictm2o.peach.components.ComponentIcon;
import nl.windesheim.ictm2o.peach.components.RegisteredComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DPAanpDialog extends JDialog implements ActionListener {

    private final JComboBox options;
    private final JTextField naam = new JTextField(5);
    private final JTextField prijs = new JTextField(5);
    private final JTextField beschikbaarheid = new JTextField(5);
    private final JButton toevoegen = new JButton("Aanpassen");
    private final JButton cancel = new JButton("Annuleren");

    private final DesignPage mainFrame;
    private final RegisteredComponent RC;

    public DPAanpDialog(JFrame frame, boolean modal, DesignPage mainFrame, RegisteredComponent RC) {
        super(frame, modal);
        this.mainFrame = mainFrame;
        this.RC = RC;

        setSize(350, 110);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Aanpassen component");

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

        //Vul gegevens in van component
        int counter2 = 0;
        for (String IC : optionsToChoose
        ) {
            if (RC.getIcon().toString().equals(IC)) {
                break;
            }
            counter2+=1;
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
            ComponentIcon CI = ComponentIcon.GENERIC;

            for (ComponentIcon IC:ComponentIcon.values()
            ) {
                if(IC.name().equals(options.getSelectedItem())){
                    CI = IC;
                    break;
                }
            }
            //Pas component aan
            RC.editComponent(naam.getText(), CI, Float.parseFloat(prijs.getText()), Float.parseFloat(beschikbaarheid.getText())/100);

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
