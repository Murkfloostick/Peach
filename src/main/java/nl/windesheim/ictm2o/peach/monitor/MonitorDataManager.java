package nl.windesheim.ictm2o.peach.monitor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonitorDataManager {

    public static class Instance {
        public boolean isNew = true;

        public final List<MonitorData> allData = new ArrayList<>();
        public final List<MonitorData> newData = new ArrayList<>();
    }

    @NotNull
    private static final HashMap<String, Instance> DATA = new HashMap<>();

    public static void append(@NotNull String identifier, @NotNull MonitorData monitorData) {
        @Nullable var data = DATA.get(identifier);

        if (data == null) {
            data = new Instance();
            DATA.put(identifier, data);
        }

        data.allData.add(monitorData);
        synchronized(data.newData) {
            data.newData.add(monitorData);
        }
    }

    @NotNull
    public static HashMap<String, Instance> getData() {
        return DATA;
    }

}
