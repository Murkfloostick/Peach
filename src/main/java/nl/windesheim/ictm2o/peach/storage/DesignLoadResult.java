package nl.windesheim.ictm2o.peach.storage;

import nl.windesheim.ictm2o.peach.components.Design;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DesignLoadResult {

    @Nullable
    private final Design design;

    @Nullable
    private final String errorMessage;

    public DesignLoadResult(@NotNull Design design) {
        this.design = design;
        this.errorMessage = null;
    }

    public DesignLoadResult(@NotNull String errorMessage) {
        this.design = null;
        this.errorMessage = errorMessage;
    }

    @Nullable
    public Design getDesign() {
        return design;
    }

    @Nullable
    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isFailure() {
        return errorMessage != null;
    }

}
