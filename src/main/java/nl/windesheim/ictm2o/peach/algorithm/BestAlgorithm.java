package nl.windesheim.ictm2o.peach.algorithm;

import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.components.PlacedComponent;

import java.util.*;

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
    //TODO Backtracking toevoegen. (Is dit een vereiste?)

    public void optiMalisatie(){
        //Variabelen initalisatie
        CA = 0;
        TA = D.getTargetAvailability();
        PC = D.getPlacedComponents();

        ARC = new ArrayList();
        List<PlacedComponent> tempArc = new ArrayList();
        masterARC = new ArrayList<>(); //Lijst met alle oplossingen

        PlacedComponent main;
        PlacedComponent main2;//De tweede die wordt gebruikt

        int max = 3; //Max aantal componenten van dezelfde RegisterdeComponent
        int maxtemp = 1; // begin hier

        //Plaats eerst alle componenten
        for (PlacedComponent PC:PC
             ) {
            ARC.add(new PlacedComponent(PC.getRegisteredComponent(), PC.getName(), PC.getPosition()));
            tempArc.add(new PlacedComponent(PC.getRegisteredComponent(), PC.getName(), PC.getPosition()));
        }

        checkAndAdd(ARC);//Dit is een oplossing

        //Voor elk element in de componenten die zijn geplaatst
        for (int counter = 0; counter <= PC.size()-1; counter++) {
            //De eerste waar we het mee gaan doen
            main = PC.get(counter);

            //*** MOGELIJK NIET NODIG?
            //Eerst max keer plaatsen en dan ze allemaal en dan een voor een
            for (int maxcount = maxtemp; maxcount <= max; maxcount++) {
                ARC.add(new PlacedComponent(main.getRegisteredComponent(), main.getName(), main.getPosition()));
            }
            checkAndAdd(ARC); //Dit is een oplossing

            //En dan de rest 1 keer plaatsen
            for (int count = 0; count <= PC.size() - 1; count++) {
                if(count != counter){ //Voorkom dat we Main weer plaatsen
                    ARC.add(new PlacedComponent(PC.get(count).getRegisteredComponent(), PC.get(count).getName(), PC.get(count).getPosition()));
                }
            }
            checkAndAdd(ARC); //Dit is een oplossing
            //***

            //Haal het leeg en terug naar begin
            ARC = null;
            ARC = new ArrayList<>(tempArc);

            //Plaats de eerste, Begin bij max 2 en dan 3...
            for (int maxcount = maxtemp; maxcount <= max; maxcount++) {
                ARC.add(new PlacedComponent(main.getRegisteredComponent(), main.getName(), main.getPosition()));
            }

            //Dan de tweede, terug, derde enzovoort zodat we iedereen langs gaan
            for (int count = 0; count <= PC.size() - 1; count++) {
                if(count != counter) { //Voorkom dat we Main weer plaatsen
                    main2 = PC.get(count);//Hou de tweede vast en doe de volgende componenten eerst

                    //Plaats max keer, dan minder tot er maar 1 is en dan volgende component die main2 wordt
                    //MAX begint bij 2 tot de echte max
                    if(main != main2){//Main is al maximaal geplaatst
                        for (int maxcount = maxtemp; maxcount <= max; maxcount++) {
                            PlacedComponent plaats = new PlacedComponent(PC.get(count).getRegisteredComponent(), PC.get(count).getName(), PC.get(count).getPosition());
                            ARC.add(plaats);
                            checkAndAdd(ARC);
                        }
                    }

                    //Nu de volgende component steeds tot max en dan verwijderen
                    for (PlacedComponent PC:PC
                         ) {
                        if(PC != main && PC != main2){
                            for (int maxcount = maxtemp; maxcount <= max; maxcount++) {
                                //dan for elk ander component dat niet main en main2 is plaatsen
                                PlacedComponent plaats = new PlacedComponent(PC.getRegisteredComponent(), PC.getName(), PC.getPosition());
                                ARC.add(plaats);
                                checkAndAdd(ARC);
                            }
                            //Verwijder nu maxtemp keer
                            for (int maxremover = 1; maxremover <= maxtemp; maxremover++){
                                ARC.remove(ARC.size() - maxremover);
                            }
                        }
                    }
                    maxtemp += 1; //Nu worden de volgende componenten met 1 max verhoogd zodat we elk combinatie vinden
                }
            }

            //Haal het leeg
            ARC = null;
            ARC = new ArrayList<>(tempArc);
        }

        //Tijd om met de gegevens de goedkoopste te vinden en op het werkpaneel te zetten
        costsAndRefresh();
    }

    //Checkt of de ARC die wordt meegegeven voldoet aan target availbility en dan toevoegen aan masterArc.
    private void checkAndAdd(List<PlacedComponent> ARC){
        CA = D.getAvailbility(ARC);
        if(CA >= TA){
            masterARC.add(ARC);
        }
    }

    //Vind de goedkoopste en zet het op het werkpaneel
    private void costsAndRefresh(){
        //Bereken goedkoopste die target haalt

        float beste = 0;
        List besteList = new ArrayList<>();
        float inkomende;
        for (List ARK:masterARC
        ) {
            inkomende = D.getKosten(ARK)[5];
            if(beste < inkomende){
                beste = inkomende;
                besteList = ARK;
            }
        }

        //Zet het in Design
        D.deletePlacComponentList();
        D.newPlacComponentList(besteList);
    }
        }


