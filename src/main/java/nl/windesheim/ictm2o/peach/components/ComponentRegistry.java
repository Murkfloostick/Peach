package nl.windesheim.ictm2o.peach.components;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ComponentRegistry {

    @NotNull
    private List<RegisteredComponent> registeredComponents;

    public ComponentRegistry() {
        this.registeredComponents = new ArrayList<>();
        //registeredComponents.add(new RegisteredComponent(UUID.randomUUID(), "Test", ComponentIcon.ROUTER, 100.0f));
    }

    @NotNull
    public List<RegisteredComponent> getRegisteredComponents() {
        return registeredComponents;
    }

    public void setRegisteredComponents(@NotNull List<RegisteredComponent> registeredComponents) {
        this.registeredComponents = registeredComponents;
    }

    @Nullable
    public RegisteredComponent findByID(@NotNull UUID uuid) {
        for (RegisteredComponent registeredComponent : this.registeredComponents)
            if (registeredComponent.getID().equals(uuid))
                return registeredComponent;
        return null;
    }

    public void delComponent(RegisteredComponent RC) {
        registeredComponents.remove(RC);
    }
}
