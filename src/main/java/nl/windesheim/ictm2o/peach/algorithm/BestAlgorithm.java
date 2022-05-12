package nl.windesheim.ictm2o.peach.algorithm;

import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.components.PlacedComponent;
import nl.windesheim.ictm2o.peach.components.Position;
import nl.windesheim.ictm2o.peach.components.RegisteredComponent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BestAlgorithm {
    private final Map<JLabel, PlacedComponent> map = new HashMap<>();
    private final Map<PlacedComponent, ArrayList<PlacedComponent>> lineMap = new HashMap<>();
    private Design D;

    public BestAlgorithm(Design d) {
        D = d;
    }

    public Map<JLabel, PlacedComponent> getMap() {
        return map;
    }

    public Map<PlacedComponent, ArrayList<PlacedComponent>> getLineMap() {
        return lineMap;

    }


    float CA; //Current availbility
    float TA; //Target availbility
    List<PlacedComponent> ARC; //Nieuwe components
    List<PlacedComponent> PC; //Components waar we het mee moeten doen

    public void vindAv() {
        int counter = 0;
        int counter2 = 0; //voor het toevoegen

        //Voor de check of we alle componenten in lijst zijn aggegeaan
        int totalSize = D.getPlacedComponents().size();
        int usedCounter = 0;

        CA = 0;
        PC = D.getPlacedComponents();
        ARC = new ArrayList();
        TA = D.getTargetAvailability();

        //Terwijl availbility lager is dan target availbility
        while (CA < D.getTargetAvailability()) {
            //Zijn we aan einde van lijst? Voeg 1 component toe aan eerste en bij volgende de tweede...
            if (totalSize == usedCounter) {
                //Voeg toe aan de eerste
                if (counter2 == totalSize) {
                    counter2 = 0; //reset naar begin en voeg weer toe.
                }
                Position pos = new Position(250, 250);
                PlacedComponent NPC = new PlacedComponent(PC.get(counter2).getRegisteredComponent(), PC.get(counter2).getRegisteredComponent().getName(), pos);
                ARC.add(NPC);
                counter2 += 1;
                usedCounter = 0;
            } else {
                Position pos = new Position(250, 250);
                PlacedComponent NPC = new PlacedComponent(PC.get(counter).getRegisteredComponent(), PC.get(counter).getRegisteredComponent().getName(), pos);
                ARC.add(NPC);
                usedCounter += 1;
                CA = D.getAvailbility(ARC) * 100;
                //Check of availbility nu lager is dan vorige
                if (CA < TA || CA > TA) {
                    //Als dat zo is, gaan we 1 stap terug en gaan we naar de volgende component.
                    ARC.remove(NPC);
                    counter += 1;
                } else if (CA == TA) {
                    checkAndAdd();
                }
            }
        }
        checkAndAdd();
    }


    public void checkAndAdd(){
        //Als ze hetzelfde zijn, stop en geef de nieuwe components door aan Design
        int allUsed = 0;
        for (PlacedComponent PC:D.getPlacedComponents()
        ) {
            //via een teller
            for (PlacedComponent arcPC:ARC
            ) {
                if(PC.getRegisteredComponent() == arcPC.getRegisteredComponent()){
                    allUsed +=1;
                }
            }
        }
        if(D.getPlacedComponents().size() == allUsed){
            D.deletePlacComponentList();
            D.newPlacComponentList(ARC);
        } else{
            vindAv();
        }
    }
}


