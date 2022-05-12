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
    private Design D;

    public BestAlgorithm(Design d) {
        D = d;
    }

    float CA; //Current availbility
    float TA; //Target availbility
    float LA; // last availbility

    List<PlacedComponent> ARC; //Nieuwe components
    List<PlacedComponent> masterARC; //Nieuwe components
    List<PlacedComponent> PC; //Components waar we het mee moeten doen


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


