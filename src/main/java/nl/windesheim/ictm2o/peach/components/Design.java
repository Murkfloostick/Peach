package nl.windesheim.ictm2o.peach.components;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Design {

    @Nullable
    private String filePath;

    private float targetAvailability = 99.99f;
    private float totalCost = 0;

    @NotNull
    private final List<PlacedComponent> placedComponents = new ArrayList<>();

    public Design(@Nullable String filePath) {
        this.filePath = filePath;
    }

    public Design(@Nullable String filePath, float targetAvailability, @Nullable List<PlacedComponent> placedComponents) {
        this.filePath = filePath;
    }

    @Nullable
    public String getFilePath() {
        return filePath;
    }

    public float getTargetAvailability() {
        return targetAvailability;
    }

    public float getTotalCost() {
        return totalCost;
    }

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

    @NotNull
    public void delPlacComponent(PlacedComponent PC){
        placedComponents.remove(PC);
    }
}
