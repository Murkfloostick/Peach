package nl.windesheim.ictm2o.peach.components;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class PlacedComponent {

    @NotNull
    private final RegisteredComponent registeredComponent;
    private String name;
    private Position position;

    public PlacedComponent(@NotNull RegisteredComponent registeredComponent, String name, Position position) {
        this.registeredComponent = registeredComponent;
        this.name = name;
        this.position = position;
    }

    @NotNull
    public RegisteredComponent getRegisteredComponent() {
        return registeredComponent;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public Position getPosition() {
        return position;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public void setPosition(@NotNull Position position) {
        this.position = position;
    }
}
