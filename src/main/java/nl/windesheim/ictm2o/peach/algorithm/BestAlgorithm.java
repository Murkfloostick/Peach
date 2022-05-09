package nl.windesheim.ictm2o.peach.algorithm;

import nl.windesheim.ictm2o.peach.components.PlacedComponent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BestAlgorithm {
    private final Map<JLabel, PlacedComponent> map = new HashMap<>();
    private final Map<PlacedComponent, ArrayList<PlacedComponent>> lineMap = new HashMap<>();

    public Map<JLabel, PlacedComponent> getMap() {
        return map;
    }

    public Map<PlacedComponent, ArrayList<PlacedComponent>> getLineMap() {
        return lineMap;
    }


    //Hier komt uiteraad straks verder een geweldig algorithm


}
