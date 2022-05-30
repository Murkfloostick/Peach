package nl.windesheim.ictm2o.peach.components;

import org.jetbrains.annotations.NotNull;

public enum ComponentIcon {

    SERVER_WEB("Webserver"),
    SERVER_DATABASE("Database Server"),
    FIREWALL("Firewall");


    @NotNull
    private final String displayName;

    ComponentIcon(@NotNull String displayName) {
        this.displayName = displayName;
    }

    @NotNull
    public String getDisplayName() {
        return displayName;
    }


}
