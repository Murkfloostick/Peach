package nl.windesheim.ictm2o.peach;

import org.jetbrains.annotations.NotNull;

public class BuildInfo {

    @NotNull
    public static String getVersion() {
        return "${version}";
    }

}

