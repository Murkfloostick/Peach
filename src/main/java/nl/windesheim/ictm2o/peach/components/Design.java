package nl.windesheim.ictm2o.peach.components;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Array;
import java.util.*;

public class Design {

    @Nullable
    private String filePath;

    private float targetAvailability = 99.99f;
    private float totalCost = 0;

    @NotNull
    private final List<PlacedComponent> placedComponents;

    private boolean isDesignSavedToFile = false;

    public Design(@Nullable String filePath) {
        this.filePath = filePath;
        this.placedComponents = new ArrayList<>();
    }

    public Design(@Nullable String filePath, float targetAvailability, @NotNull List<PlacedComponent> placedComponents) {
        this.filePath = filePath;
        this.targetAvailability = targetAvailability;
        this.placedComponents = placedComponents;
    }

    @Nullable
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(@Nullable String path) {
        this.filePath = path;
    }

    public float getTargetAvailability() {
        return targetAvailability;
    }

    public float getTotalCost() {
        return totalCost;
    }

    @NotNull
    public List<PlacedComponent> getPlacedComponents() {
        return placedComponents;
    }

    public void setTargetAvailability(float targetAvailability) {
        this.targetAvailability = targetAvailability;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public void calculateTotalCost() {
        this.totalCost = 0;
        for (PlacedComponent placedComponent : placedComponents)
            this.totalCost += placedComponent.getRegisteredComponent().getCost();
    }

    public boolean isDesignSavedToFile() {
        return isDesignSavedToFile;
    }

    public void setSavedToFile() {
        this.isDesignSavedToFile = true;
    }

    public void setModified() {
        this.isDesignSavedToFile = false;
    }

    @NotNull
    public void delPlacComponent(PlacedComponent PC) {
        placedComponents.remove(PC);
    }

    public float getAvailbility() {
        float[] array = {0, 0, 0, 0, 0};
        float total = 0;

        for (PlacedComponent PC : placedComponents
        ) {
            if (PC.getRegisteredComponent().getIcon().toString().equals("GENERIC")) {
                if (array[4] != 0) {
                    array[4] = array[4] * PC.getRegisteredComponent().getAvailability();
                } else {
                    array[4] = array[4] + PC.getRegisteredComponent().getAvailability();
                }
            }
            if (PC.getRegisteredComponent().getIcon().toString().equals("ROUTER")) {
                if (array[3] != 0) {
                    array[3] = array[3] * PC.getRegisteredComponent().getAvailability();
                } else {
                    array[3] = array[3] + PC.getRegisteredComponent().getAvailability();
                }
            }
            if (PC.getRegisteredComponent().getIcon().toString().equals("FIREWALL")) {
                if (array[2] != 0) {
                    array[2] = array[2] * PC.getRegisteredComponent().getAvailability();
                } else {
                    array[2] = array[2] + PC.getRegisteredComponent().getAvailability();
                }
            }
            if (PC.getRegisteredComponent().getIcon().toString().equals("SERVER_DATABASE")) {
                if (array[1] != 0) {
                    array[1] = array[1] * PC.getRegisteredComponent().getAvailability();
                } else {
                    array[1] = array[1] + PC.getRegisteredComponent().getAvailability();
                }
            }
            if (PC.getRegisteredComponent().getIcon().toString().equals("SERVER_WEB")) {
                if (array[0] != 0) {
                    array[0] = array[0] * PC.getRegisteredComponent().getAvailability();
                } else {
                    array[0] = array[0] + PC.getRegisteredComponent().getAvailability();
                }
            }
        }

        for (float av : array
        ) {
            if (av != 0) {
                if (total != 0) {
                    total *= av;
                } else {
                    total += av;
                }
            }
        }
        return total;
    }

    public float[] getKosten(){
        float[] array = {0,0,0,0,0,0};

        for (PlacedComponent PC:placedComponents
        ) {
            if(PC.getRegisteredComponent().getIcon().toString().equals("GENERIC")){
                array[4] = array[4] + PC.getRegisteredComponent().getCost();
            }
            if(PC.getRegisteredComponent().getIcon().toString().equals("ROUTER")){
                array[3] = array[3] + PC.getRegisteredComponent().getCost();
            }
            if(PC.getRegisteredComponent().getIcon().toString().equals("FIREWALL")){
                array[2] = array[2] + PC.getRegisteredComponent().getCost();
            }
            if(PC.getRegisteredComponent().getIcon().toString().equals("SERVER_DATABASE")){
                array[1] = array[1] + PC.getRegisteredComponent().getCost();
            }
            if(PC.getRegisteredComponent().getIcon().toString().equals("SERVER_WEB")){
                array[0] = array[0] + PC.getRegisteredComponent().getCost();
            }
            array[5] = array[5] + PC.getRegisteredComponent().getCost();
        }
        return array;
    }

}
