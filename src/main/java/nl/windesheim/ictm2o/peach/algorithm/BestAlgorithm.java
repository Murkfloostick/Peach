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
    float LA; // last availbility

    List<PlacedComponent> ARC; //Nieuwe components
    List<PlacedComponent> PC; //Components waar we het mee moeten doen

    //TODO mogelijke oplossingen opslaan
    //TODO Maximale aantalen per component instellen
    //TODO mogelijke oplossingen comparen (welk is goedkoopst)

    public void vindAv() {
        int counter = 0; //Main counter om componenten toe te voegen
        int counter2 = 0; //voor het toevoegen

        //Voor de check of we alle componenten in lijst zijn aggegeaan
        int totalSize = D.getPlacedComponents().size();
        int usedCounter = 0;

        //Initaliseer wat variabelen
        CA = 0;
        LA = 0;
        PC = D.getPlacedComponents();
        ARC = new ArrayList();
        TA = D.getTargetAvailability();

        //Terwijl availbility lager is dan target availbility
        while (CA < D.getTargetAvailability()) {
            //Zijn we aan einde van lijst? Voeg 1 component toe aan eerste en bij volgende de tweede...
            if (totalSize < usedCounter) {
                if (counter2 > totalSize) {
                    counter2 = 0; //reset naar begin en voeg weer toe.
                }

                //Voeg component toe
                Position pos = new Position(250, 250);
                PlacedComponent NPC = new PlacedComponent(PC.get(counter2).getRegisteredComponent(), PC.get(counter2).getRegisteredComponent().getName(), pos);
                ARC.add(NPC);

                //Reset wat counters zodat we als het ware opnieuw beginnen
                counter2 += 1;
                usedCounter = 0;
                counter = 0;
            } else {
                if (!(counter > totalSize - 1)) {
                    //Voeg component toe
                    Position pos = new Position(250, 250);
                    PlacedComponent NPC = new PlacedComponent(PC.get(counter).getRegisteredComponent(), PC.get(counter).getRegisteredComponent().getName(), pos);
                    ARC.add(NPC);
                    usedCounter += 1;

                    //Check of availbility nu lager is dan vorige
                    CA = D.getAvailbility(ARC) * 100;
                    if (CA < LA) {
                        //Als dat zo is, gaan we 1 stap terug en gaan we naar de volgende component.
                        ARC.remove(NPC);
                        counter += 1;
                    } else if (CA >= TA) {
                        //Anders gaan we kijken of we het kunnen toevoegen als oplossing
                        checkAndAdd();
                        LA = CA;
                    } else {
                        //Sla laatste availbility op
                        LA = CA;
                    }
                } else {
                    counter = 0;
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


