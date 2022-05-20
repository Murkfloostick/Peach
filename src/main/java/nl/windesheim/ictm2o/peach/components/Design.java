package nl.windesheim.ictm2o.peach.components;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Design {

    @Nullable
    private String filePath;

//    private float targetAvailability = 99.99f;
    //TODO Verbind dit met dat label in DPToevCOmpon
    private float targetAvailability = 70.00f;
    private float totalCost = 0;

    @NotNull
    private List<PlacedComponent> placedComponents;

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

    public void delPlacComponent(PlacedComponent PC) {
        placedComponents.remove(PC);
    }

    //Voor optimalisatie
    public void deletePlacComponentList(){
        placedComponents = null;
    }

    public void newPlacComponentList(List NPC){
        placedComponents = NPC;
    }

    public static float[] calculateStatisticsPerCategory(@NotNull final List<PlacedComponent> placedComponents,
                                                         @NotNull ComponentIcon category) {
        float availability = 1;
        float costs = 0;

        for (@NotNull PlacedComponent placedComponent : placedComponents) {
            if (placedComponent.getRegisteredComponent().getIcon() != category)
                continue;

            System.out.println(placedComponent.getRegisteredComponent().getAvailability());
            availability *= 1 - placedComponent.getRegisteredComponent().getAvailability();
            costs += placedComponent.getRegisteredComponent().getCost();
        }

        return new float[]{1 - availability, costs};
    }

    @NotNull
    public ComponentsStatistics getStatistics() {
        float[] availabilities = new float[ComponentIcon.values().length];
        Float totalAvailability = null;

        float[] costs = new float[ComponentIcon.values().length];
        float totalCosts = 0;

        for (ComponentIcon componentIcon : ComponentIcon.values()) {
            final var categoryStats = calculateStatisticsPerCategory(placedComponents, componentIcon);

            availabilities[componentIcon.ordinal()] = categoryStats[0];
            if (categoryStats[0] != 0.0f) {
                if (totalAvailability == null)
                    totalAvailability = categoryStats[0];
                else
                    totalAvailability *= categoryStats[0];
            }

            costs[componentIcon.ordinal()] = categoryStats[1];
            totalCosts += categoryStats[1];
        }

        return new ComponentsStatistics(availabilities, totalAvailability == null ? 0.0f : totalAvailability, costs, totalCosts);
    }

    public float[] getKosten(List<PlacedComponent> placedComponents){
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
