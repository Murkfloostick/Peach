package nl.windesheim.ictm2o.peach.algorithm;

import nl.windesheim.ictm2o.peach.DesignPage;
import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.components.PlacedComponent;
import nl.windesheim.ictm2o.peach.components.Position;

import java.util.*;

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
                    ARC.remove(ARC.size() - 1);
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


