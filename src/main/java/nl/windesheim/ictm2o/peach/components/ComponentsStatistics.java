package nl.windesheim.ictm2o.peach.components;

import org.jetbrains.annotations.NotNull;

public class ComponentsStatistics {

    private final float @NotNull [] availabilityPerCategory;
    private final float totalAvailability;

    private final float @NotNull [] costsPerCategory;
    private final float totalCosts;

    public ComponentsStatistics(float @NotNull [] availabilityPerCategory, float totalAvailability,
                                float @NotNull [] costsPerCategory, float totalCosts) {
        this.availabilityPerCategory = availabilityPerCategory;
        this.totalAvailability = totalAvailability;
        this.costsPerCategory = costsPerCategory;
        this.totalCosts = totalCosts;
    }

    public float @NotNull [] getAvailabilityPerCategory() {
        return availabilityPerCategory;
    }

    public float getTotalAvailability() {
        return totalAvailability;
    }

    public float @NotNull [] getCostsPerCategory() {
        return costsPerCategory;
    }

    public float getTotalCosts() {
        return totalCosts;
    }
}
