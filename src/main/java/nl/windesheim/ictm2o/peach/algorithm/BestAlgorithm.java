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
    List<PlacedComponent> masterARC; //Nieuwe components
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

    public void vindAv2(){
        CA = 0;
        LA = 0;
        PC = D.getPlacedComponents();
        ARC = new ArrayList();
        TA = D.getTargetAvailability();

        int counter = 0;
        int counter2 = 0;
        while(TA > CA){
            if(counter == D.getPlacedComponents().size()){
                //Voeg component toe
                Position pos2 = new Position(250, 250);
                PlacedComponent NPC2 = new PlacedComponent(PC.get(counter2).getRegisteredComponent(), PC.get(counter2).getRegisteredComponent().getName(), pos2);
                ARC.add(NPC2);

                counter2 += 1;
                if(counter2 == D.getPlacedComponents().size()){
                    counter2 = 0;
                }
                counter = 0;
            } else {
                Position pos = new Position(250, 250);
                PlacedComponent NPC = new PlacedComponent(PC.get(counter).getRegisteredComponent(), PC.get(counter).getRegisteredComponent().getName(), pos);
                ARC.add(NPC);
                counter += 1;

                CA = D.getAvailbility(ARC) * 100;
                if (CA < LA) {
                    ARC.remove(NPC);
                } else {
                    LA = CA;
                }
            }
        }
    }

    public void vindAv3(){
        CA = 0;
        LA = 0;
        PC = D.getPlacedComponents();
        ARC = new ArrayList();
        masterARC = new ArrayList();
        TA = D.getTargetAvailability();

        //Plaats eerst alle componenten
        for (PlacedComponent PC:PC
             ) {
            Position pos = new Position(250, 250);
            PlacedComponent NPC = new PlacedComponent(PC.getRegisteredComponent(), PC.getRegisteredComponent().getName(), pos);
            ARC.add(NPC);
        }
        CA = D.getAvailbility(ARC) * 100;
        masterARC = new ArrayList<>(ARC);

        //Maximaal 8 keer
        //TODO Dit moet nog zo werken:
//        1-1 -- Dit zijn componenten
//        Dan wordt het 2-1, 2-2, 4-1 totenmet 8 en dan 3-1, 3-2 en dan 1-2 2-2, 3-2
        //TODO Sla mogelijke oplossingen op en kijk naar de prijs

        int counter3 = 0;
        for(int totalCounter = 0; totalCounter < 7; totalCounter++){
                //Voor elk component
                for(int counter = 0; PC.size() > counter; counter++){
                    //Elk component 8 keer plaatsen
                    for(int counter2 = 0; counter2 < 7; counter2++) {
                        Position pos = new Position(250, 250);
                        PlacedComponent NPC = new PlacedComponent(PC.get(counter).getRegisteredComponent(), PC.get(counter).getRegisteredComponent().getName(), pos);
                        ARC.add(NPC);

                        //Check dan of target availbility wordt gehaald
                        CA = D.getAvailbility(ARC) * 100;
                        if (CA >= TA) {
                            System.out.println("SOLUTION FOUND");
                        } else {
                            //Continuu
                        }
                    }
                }
                ARC = null;
                //Voeg 1 component aan de eerste
            Position pos = new Position(250, 250);
            PlacedComponent NPC = new PlacedComponent(PC.get(counter3).getRegisteredComponent(), PC.get(counter3).getRegisteredComponent().getName(), pos);
            masterARC.add(NPC);
            ARC = new ArrayList<>(masterARC);
            counter3+=1;
            if(counter3 == PC.size()){
                counter3 = 0;
            }
            }
        }
    }


