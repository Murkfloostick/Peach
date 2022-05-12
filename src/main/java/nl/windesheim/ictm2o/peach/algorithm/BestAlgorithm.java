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
    private Design D = null;

    public BestAlgorithm(Design d) {
        D = d;
    }

    public Map<JLabel, PlacedComponent> getMap() {
        return map;
    }

    public Map<PlacedComponent, ArrayList<PlacedComponent>> getLineMap() {
        return lineMap;

    }


        float CA = D.getAvailbility(D.getPlacedComponents()); //Current availbility
        List<PlacedComponent> ARC; //Nieuwe components
        List<PlacedComponent> PC = D.getPlacedComponents(); //Components waar we het mee moeten doen

    public void vindAv(){
        int counter= 0;
        CA = 0;

        //Terwijl availbility lager is dan target availbility
        while(CA < D.getTargetAvailability()){
            //Dit moet anders, maakt een nieuw placed component en voegt toe aan nieuw lijst
            Position pos = new Position(250, 250);
            PlacedComponent NPC = new PlacedComponent(PC.get(counter).getRegisteredComponent(), PC.get(counter).getRegisteredComponent().getName(), pos);
            ARC.add(NPC);

            //Check of availbility nu lager is dan vorige
            if(D.getAvailbility(ARC) < CA || D.getAvailbility(ARC) > CA ){
                //Als dat zo is, gaan we 1 stap terug en gaan we naar de volgende component.
                ARC.remove(NPC);
                counter += 1;
            } else if(D.getAvailbility(ARC) == CA){
                //Als ze hetzelfde zijn, stop en geef de nieuwe components door aan Design
                D.deletePlacComponentList();
                D.newPlacComponentList(ARC);
            }
        }
    }




}


