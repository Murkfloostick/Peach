package nl.windesheim.ictm2o.peach.monitor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitorDataManager {

    public static class Instance {
        public final List<MonitorData> allData = new ArrayList<>();
        public final List<MonitorData> newData = new ArrayList<>();

        public String address = "onbekend";

        public Instant lastHeartbeat = null;
        public boolean offlineAcknowledged = true;
        public boolean onlineAcknowledged = false;

        public int ticksAvailable = 3590;
        public int ticksUnavailable = 0;
        public int totalTicksSinceSubscription = 3591;
    }

    public interface InstanceAlertListener {
        void onAlert(@NotNull String identifier, @NotNull Instance instance);
    }

    @NotNull
    private static final HashMap<String, Instance> DATA = new HashMap<>();

    private static InstanceAlertListener instanceOfflineAlert = null;
    private static InstanceAlertListener instanceOnlineAlert = null;

    public static void append(@NotNull String identifier, @NotNull MonitorData monitorData, @Nullable Socket origin) {
        @Nullable var data = DATA.get(identifier);

        if (data == null) {
            data = new Instance();
            DATA.put(identifier, data);
        }

        if (origin != null)
            data.address = origin.getInetAddress().getHostAddress() + ":" + origin.getPort();

        if (!data.onlineAcknowledged) {
            data.onlineAcknowledged = true;
            if (instanceOnlineAlert != null)
                instanceOnlineAlert.onAlert(identifier, data);
        }

        data.allData.add(monitorData);
        synchronized(data.newData) {
            data.newData.add(monitorData);
            data.lastHeartbeat = Instant.now();
            data.offlineAcknowledged = false;
        }
    }

    // Should be called every second.
    public static void tick() {
        final var now = Instant.now();

        for (@NotNull Map.Entry<String, Instance> entry : DATA.entrySet()) {
            final var identifier = entry.getKey();
            final var instance = entry.getValue();

            if (instance.lastHeartbeat == null)
                continue;

            if (now.getEpochSecond() - instance.lastHeartbeat.getEpochSecond() < 3) {
                ++instance.ticksAvailable;
            } else if (!instance.offlineAcknowledged) {
                ++instance.ticksUnavailable;
                instance.offlineAcknowledged = true;
                instance.onlineAcknowledged = false;
                if (instanceOfflineAlert != null)
                    instanceOfflineAlert.onAlert(identifier, instance);
            } else
                ++instance.ticksUnavailable;
            ++instance.totalTicksSinceSubscription;
        }
    }

    @NotNull
    public static HashMap<String, Instance> getData() {
        return DATA;
    }

    public static void attachInstanceOfflineAlertCallback(InstanceAlertListener instanceOfflineAlert) {
        MonitorDataManager.instanceOfflineAlert = instanceOfflineAlert;
    }

    public static void attachInstanceOnlineAlertCallback(InstanceAlertListener instanceOfflineAlert) {
        MonitorDataManager.instanceOnlineAlert = instanceOfflineAlert;
    }
}
