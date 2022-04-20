package nl.windesheim.ictm2o.peach.components;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ComponentRegistry {

    @NotNull
    private final List<RegisteredComponent> registeredComponents;

    public ComponentRegistry() {
        this.registeredComponents = new ArrayList<>();
    }

    public ComponentRegistry(@NotNull ArrayList<RegisteredComponent> registeredComponents) {
        this.registeredComponents = registeredComponents;
    }

    @NotNull
    public List<RegisteredComponent> getRegisteredComponents() {
        return registeredComponents;
    }
}
