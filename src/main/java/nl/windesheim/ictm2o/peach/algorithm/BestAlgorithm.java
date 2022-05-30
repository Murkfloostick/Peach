package nl.windesheim.ictm2o.peach.algorithm;

import nl.windesheim.ictm2o.peach.Main;
import nl.windesheim.ictm2o.peach.components.ComponentIcon;
import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.components.PlacedComponent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class BestAlgorithm {
    private final Design D;

    public BestAlgorithm(Design d) {
        D = d;
    }

    float CA; //Current availbility
    float TA; //Target availbility

    List<PlacedComponent> ARC; //Nieuwe components
    ArrayList<List<PlacedComponent>> masterARC; //Meesterlijst met lijsten die voldoet aan target avail.
    List<PlacedComponent> PC; //Components waar we het mee moeten doen

    //Dit genereert alle combinaties
    //De werking zal zo moeten werken:
    //
    //       A      AB     ABC
    //ABC -> ABC -> ABC -> ABC
    //
    //En dan zo door met B en C als de 'hoofd' component in dat loop. (main)
    //Nu gaan we met 1 omhoog om ook daar alle combinaties te pakken
    //
    //       ABC    AB     AB
    //       ABC    ABC    AB
    //ABC -> ABC -> ABC -> ABC
    //
    //Dan is C de volgende die maximaal alles heeft (main2)

    public void optiMalisatie() {
        //Variabelen initalisatie
        CA = 0;
        TA = D.getTargetAvailability();
        PC = D.getPlacedComponents();

        ARC = new ArrayList<>();
        List<PlacedComponent> tempArc = new ArrayList<>();
        masterARC = new ArrayList<>(); //Lijst met alle oplossingen

        PlacedComponent main;
        PlacedComponent main2;//De tweede die wordt gebruikt

        int max = 3; //Max aantal componenten van dezelfde RegisterdeComponent
        int maxtemp = 1; // begin hier

        //Voor elk element in de componenten die zijn geplaatst
        for (int counter = 0; counter <= PC.size() - 1; counter++) {
            //De eerste waar we het mee gaan doen
            main = PC.get(counter);


            //Plaats de main, Begin bij max 2 en dan 3...
            for (int maxcount = 1; maxcount <= maxtemp; maxcount++) {
                ARC.add(new PlacedComponent(main.getRegisteredComponent(), main.getName(), main.getPosition()));
            }

            //Dan de tweede component, terug, derde enzovoort zodat we iedereen langs gaan
            for (int count = 0; count <= PC.size() - 1; count++) {
                if (count != counter) { //Voorkom dat we Main weer plaatsen
                    main2 = PC.get(count);//Hou de tweede vast en doe de volgende componenten eerst

                    //Plaats max keer, dan minder tot er maar 1 is en dan volgende component die main2 wordt
                    //MAX begint bij 2 tot de echte max
                    if (main != main2) {//Main is al maximaal geplaatst
                        for (int maxcount = 1; maxcount <= maxtemp; maxcount++) {
                            PlacedComponent plaats = new PlacedComponent(PC.get(count).getRegisteredComponent(), PC.get(count).getName(), PC.get(count).getPosition());
                            ARC.add(plaats);
                            checkAndAdd(ARC);
                        }
                    }

                    //Nu de volgende component steeds tot max en dan verwijderen
                    for (PlacedComponent PC : PC
                    ) {
                        if (PC != main && PC != main2) {
                            for (int maxcount = 1; maxcount <= maxtemp; maxcount++) {
                                //dan for elk ander component dat niet main en main2 is plaatsen
                                PlacedComponent plaats = new PlacedComponent(PC.getRegisteredComponent(), PC.getName(), PC.getPosition());
                                ARC.add(plaats);
                                checkAndAdd(ARC);
                            }
                            //Verwijder nu maxtemp keer
                            for (int maxremover = 1; maxremover <= maxtemp; maxremover++) {
                                ARC.remove(ARC.size() - maxremover);
                            }
                        }
                    }
                    maxtemp += 1; //Nu worden de volgende componenten met 1 max verhoogd zodat we elk combinatie vinden

                    //Zodat we maximaal enforcen
                    if (max == maxtemp) {
                        maxtemp = 1;
                    }
                }
            }

            //Haal het leeg
            ARC = null;
            ARC = new ArrayList<>(tempArc);

            if (maxtemp > max) {
                break;
            }
        }

        //Tijd om met de gegevens de goedkoopste te vinden en op het werkpaneel te zetten
        costsAndRefresh();
    }

    //Checkt of de ARC die wordt meegegeven voldoet aan target availbility en dan toevoegen aan masterArc.
    private void checkAndAdd(List<PlacedComponent> ARC) {
        final var stats = D.getStatistics();

        var data = new Object[2][1 + stats.availabilityPerCategory().length];
        var columnNames = new String[1 + stats.availabilityPerCategory().length];

        data[0][0] = "Kosten";
        data[1][0] = "Beschikbaarheid";
        columnNames[0] = "";

        for (int i = 0; i < stats.availabilityPerCategory().length; ++i) {
            columnNames[1 + i] = ComponentIcon.values()[i].getDisplayName();

            data[0][1 + i] = String.format(Main.LOCALE, "€ %.02f", stats.costsPerCategory()[i]);
            data[1][1 + i] = String.format(Main.LOCALE, "%.2f %%", stats.availabilityPerCategory()[i] * 100.0f);
        }

        CA = stats.totalAvailability() * 100.0f;
        if (CA >= TA) {
            masterARC.add(ARC);
        }
    }

    //Vind de goedkoopste en zet het op het werkpaneel
    private void costsAndRefresh() {
        //Bereken goedkoopste die target haalt

        float beste = 0;
        boolean firstTime = true;
        List besteList = null;
        float inkomende;
        for (var ARK : masterARC) {
            inkomende = D.getKosten(ARK)[5];
            if (inkomende < beste || firstTime) {
                beste = inkomende;
                besteList = ARK;
                firstTime = false;
            }
        }


        if (besteList == null) {
            JOptionPane.showMessageDialog(null, "Het is niet mogelijk om de beste setup te vinden met deze componenten.", "Ho daar: Optimalisatie niet gelukt", JOptionPane.INFORMATION_MESSAGE);
        } else {
            //Zet het in Design
            D.deletePlacComponentList();
            D.newPlacComponentList(besteList);
        }
    }
}