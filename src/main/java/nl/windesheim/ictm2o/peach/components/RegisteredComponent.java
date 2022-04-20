package nl.windesheim.ictm2o.peach.components;

import org.jetbrains.annotations.NotNull;

public class RegisteredComponent {

    private final @NotNull String name;
    private final @NotNull ComponentIcon icon;
    private final float cost;

    public RegisteredComponent(@NotNull String name, @NotNull ComponentIcon icon, float cost) {
        this.name = name;
        this.icon = icon;
        this.cost = cost;
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

}
