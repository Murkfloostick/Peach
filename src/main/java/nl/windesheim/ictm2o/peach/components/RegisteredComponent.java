package nl.windesheim.ictm2o.peach.components;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class RegisteredComponent {

    private final @NotNull UUID id;
    private @NotNull String name;
    private @NotNull ComponentIcon icon;
    private float cost;
    private float availability;

    public RegisteredComponent(@NotNull UUID id, @NotNull String name, @NotNull ComponentIcon icon, float cost, float availability) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.cost = cost;
        this.availability = availability;
    }

    @NotNull
    public UUID getID() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public ComponentIcon getIcon() {
        return icon;
    }

    public float getCost() {
        return cost;
    }

    public float getAvailability() {
        return availability;
    }

    public void editComponent(@NotNull String name, @NotNull ComponentIcon icon, float cost, float availability) {
        this.name = name;
        this.icon = icon;
        this.cost = cost;
        this.availability = availability;
    }
}
