package nl.windesheim.ictm2o.peach.monitor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonitorDataManager {

    public static class Instance {
        public final List<MonitorData> allData = new ArrayList<>();
        public final List<MonitorData> newData = new ArrayList<>();

        public String address = "onbekend";

        public Instant lastHeartbeat = null;

        public int ticksAvailable = 0;
        public int totalTicksSinceSubscription = 0;
    }

    @NotNull
    private static final HashMap<String, Instance> DATA = new HashMap<>();

    public static void append(@NotNull String identifier, @NotNull MonitorData monitorData, @Nullable Socket origin) {
        @Nullable var data = DATA.get(identifier);

        if (data == null) {
            data = new Instance();
            DATA.put(identifier, data);
        }

        if (origin != null)
            data.address = origin.getInetAddress().getHostAddress() + ":" + origin.getPort();

        data.allData.add(monitorData);
        synchronized(data.newData) {
            data.newData.add(monitorData);
            data.lastHeartbeat = Instant.now();
        }
    }

    // Should be called every second.
    public static void tick() {
        final var now = Instant.now();

        for (@NotNull Instance instance : DATA.values()) {
            if (instance.lastHeartbeat == null)
                continue;

            if (now.getEpochSecond() - instance.lastHeartbeat.getEpochSecond() < 3)
                ++instance.ticksAvailable;
            ++instance.totalTicksSinceSubscription;
        }
    }

    @NotNull
    public static HashMap<String, Instance> getData() {
        return DATA;
    }

}