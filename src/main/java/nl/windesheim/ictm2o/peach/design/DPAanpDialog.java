package nl.windesheim.ictm2o.peach.design;

import nl.windesheim.ictm2o.peach.DesignPage;
import nl.windesheim.ictm2o.peach.components.ComponentIcon;
import nl.windesheim.ictm2o.peach.components.RegisteredComponent;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class DPAanpDialog extends JDialog implements ActionListener {

    private final JComboBox<String> options;
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
        final var optionsToChoose = new String[ComponentIcon.values().length];
        int counter = 0;
        for (@NotNull final var componentIcon : ComponentIcon.values()) {
            optionsToChoose[counter] = componentIcon.name();
            counter += 1;
        }
        options = new JComboBox<>(optionsToChoose);
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
        for (@NonNls @NotNull final var option : ComponentIcon.values()) {
            if (RC.getIcon() == option)
                break;
            counter2 += 1;
        }
        options.setSelectedIndex(counter2);
        naam.setText(RC.getName());
        prijs.setText(String.valueOf(RC.getCost()));
        beschikbaarheid.setText(String.valueOf(RC.getAvailability() * 100.0f));

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == toevoegen) {
            final var componentIcon = ComponentIcon.valueOf(Objects.requireNonNull(options.getSelectedItem()).toString());

            //Pas component aan
            RC.editComponent(naam.getText(), componentIcon, Float.parseFloat(prijs.getText()), Float.parseFloat(beschikbaarheid.getText()) / 100.0f);

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
