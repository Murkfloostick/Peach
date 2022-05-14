package nl.windesheim.ictm2o.peach.algorithm;

import nl.windesheim.ictm2o.peach.DesignPage;
import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.components.PlacedComponent;
import nl.windesheim.ictm2o.peach.components.Position;
import org.w3c.dom.Node;

import java.util.*;
import java.util.stream.Collectors;

public class BestAlgorithm {
    private Design D;

    public BestAlgorithm(Design d) {
        D = d;
    }

    float CA; //Current availbility
    float TA; //Target availbility

    List<PlacedComponent> ARC; //Nieuwe components
    List<PlacedComponent> PC; //Components waar we het mee moeten doen

    public void vindAv() {
        //Variabelen initalisatie
        CA = 0;
        PC = D.getPlacedComponents();
        ARC = new ArrayList();
        ArrayList<List<PlacedComponent>> masterARC = new ArrayList<>(); //Lijst met alle oplossingen
        TA = D.getTargetAvailability();

        //Plaats eerst alle componenten
        for (PlacedComponent PC : PC
        ) {
            Position pos = new Position(250, 250);
            PlacedComponent NPC = new PlacedComponent(PC.getRegisteredComponent(), PC.getRegisteredComponent().getName(), pos);
                    ARC.add(NPC);
        }

        //Nog meer variabelen die logic loops regelen
        int counter = 0;
        int componentCounter = 0;
        int max = 0;

        //De brein MAXIMAAL 32 keer
        while (max < 32) {
            //Maximaal 3 componenten per iteratie
            if (componentCounter == 3) {
                counter += 1;
            }

            //Als de counter er overheen zit, zet terug naar de eerste component
            if (PC.size() <= counter) {
                counter = 0;
            }

            //Als de component vaak genoeg is geplaatst, ga naar de volgende component
            int countComponents = 0;
            for (PlacedComponent PC2:PC
            ) {
                if (PC.get(counter).getRegisteredComponent() == PC2.getRegisteredComponent()) {
                    countComponents+=1;
                }

                if(countComponents == 8){
                    counter+=1;
                    break;
                }
            }

            //Plaats component
            Position pos = new Position(250, 250);
            PlacedComponent NPC = new PlacedComponent(PC.get(counter).getRegisteredComponent(), PC.get(counter).getRegisteredComponent().getName(), pos);
            ARC.add(NPC);
            componentCounter += 1;

            //Check dan of target availbility wordt gehaald
            CA = D.getAvailbility(ARC) * 100;
            if (CA >= TA) {
                System.out.println("SOLUTION FOUND");
                masterARC.add(ARC);
            } else {
                //Stap terug en ander component proberen
                ARC.remove(ARC.size() - 1);
                if (PC.size() <= counter) {
                    //Als we bij de einde zijn doe er nog een want anders komen we in een loop? Zet ook terug naar eerste component
                    counter = 0;
                } else {
                    counter += 1;
                }
                componentCounter = 0;
            }
            System.out.println(ARC.toString());
            max += 1;
        }

        //Bereken goedkoopste die target haalt
        float beste = 0;
        List besteList = new ArrayList<>();
        float inkomende;

        for (List ARK:masterARC
             ) {
            inkomende = D.getKosten(ARK)[5];
            if(inkomende < beste){
                beste = inkomende;
                besteList = ARK;
            }
        }

        //Zet het in Design
        D.deletePlacComponentList();
        D.newPlacComponentList(besteList);
    }

    //Dit genereert alle combinaties
    //TODO Voeg meerdere van dezelfde toe
    //TODO Vind of het target beschikbaarheid haalt en stop in lijst
    //TODO Vindt goedkoopste van bovenstelijst en zet het op het werkpaneel
    //TODO Backtracking toevoegen
    public static void combinations(PlacedComponent[] values, List<PlacedComponent> current, Set<Set<PlacedComponent>> accumulator, int size, int pos) {
        if (current.size() == size) {
            Set<PlacedComponent> toAdd = current.stream().collect(Collectors.toSet());
            if (accumulator.contains(toAdd)) {
                throw new RuntimeException("Duplicated value " + current);
            }
            accumulator.add(toAdd);
            return;
        }
        for (int i = pos; i <= values.length - size + current.size(); i++) {
            current.add(values[i]);
            combinations(values, current, accumulator, size, i + 1);
            current.remove(current.size() - 1);
        }
    }

    public void aaaaa(){
        //TODO Verhogen van maximaal aantal componenten
        //Variabelen initalisatie
        CA = 0;
        PC = D.getPlacedComponents();
        ARC = new ArrayList();
        List<PlacedComponent> tempArc = new ArrayList();
        ArrayList<List<PlacedComponent>> masterARC = new ArrayList<>(); //Lijst met alle oplossingen
        TA = D.getTargetAvailability();
        PlacedComponent main;
        PlacedComponent main2;//De tweede die wordt gebruikt
        int counter2 = 0;

        int max = 3; //Max aantal componenten van dezelfde RegisterdeComponent

        //Plaats eerst alle componenten
        for (PlacedComponent PC:PC
             ) {
            ARC.add(new PlacedComponent(PC.getRegisteredComponent(), PC.getName(), PC.getPosition()));
            tempArc.add(new PlacedComponent(PC.getRegisteredComponent(), PC.getName(), PC.getPosition()));
        }

        masterARC.add(ARC);//Dit is een oplossing

        for (int counter = 0; counter <= PC.size()-1; counter++) {
            //De eerste waar we het mee gaan doen
            main = PC.get(counter);

            //Eerst plaatsen en dan ze allemaal en dan een voor een
            ARC.add(new PlacedComponent(main.getRegisteredComponent(), main.getName(), main.getPosition()));
            masterARC.add(ARC); //Dit is een oplossing

            //En dan de rest
            for (int count = 0; count <= PC.size() - 1; count++) {
                if(count != counter){ //Voorkom dat we Main weer plaatsen
                    ARC.add(new PlacedComponent(PC.get(count).getRegisteredComponent(), PC.get(count).getName(), PC.get(count).getPosition()));
                }
            }
            masterARC.add(ARC); //Dit is een oplossing

            //Haal het leeg
            ARC = null;
            ARC = new ArrayList<>(tempArc);

            //Plaats de eerste
            ARC.add(new PlacedComponent(main.getRegisteredComponent(), main.getName(), main.getPosition()));

            //Dan de tweede, terug, derde enzovoort
            for (int count = 0; count <= PC.size() - 1; count++) {
                if(count != counter) { //Voorkom dat we Main weer plaatsen
                    main2 = PC.get(count);//Hou de tweede vast en doe de volgende componenten eerst

                    //Plaats max keer, dan minder tot er maar 1 is en dan volgende component die main2 wordt
                    //MAX begint bij 2 tot de echte max
                    if(main != main2){
                        //plaats max keer
                    }
                    //dan for elk ander component dat niet main en main2 is plaatsen
                    PlacedComponent plaats = new PlacedComponent(PC.get(count).getRegisteredComponent(), PC.get(count).getName(), PC.get(count).getPosition());
                    ARC.add(plaats);
                    masterARC.add(ARC);
                    ARC.remove(plaats);

                    //Volgende is main en hetzelfde
                }
            }

            //Haal het leeg
            ARC = null;
            ARC = new ArrayList<>(tempArc);

            //Nog een keer doen maar dan met een extra component totdat max is gehaald
            if(counter+1 <= PC.size()-1){

            }
        }

        //Print nu alles in masterarc
        for (List<PlacedComponent> ARC : masterARC
        ) {
            System.out.println(ARC.toString());
        }
    }
        }


