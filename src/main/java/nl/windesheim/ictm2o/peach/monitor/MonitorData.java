package nl.windesheim.ictm2o.peach.monitor;

public class MonitorData {

    private final int cpuPercentage;

    private final long memoryTotal;
    private final long memoryUsed;

    private final long diskTotal;
    private final long diskUsed;

    public MonitorData(int cpuPercentage, long memoryTotal, long memoryUsed, long diskTotal, long diskUsed) {
        this.cpuPercentage = cpuPercentage;

        this.memoryTotal = memoryTotal;
        this.memoryUsed = memoryUsed;

        this.diskTotal = diskTotal;
        this.diskUsed = diskUsed;
    }

    public int getCPUPercentage() {
        return cpuPercentage;
    }

    public long getMemoryTotal() {
        return memoryTotal;
    }

    public long getMemoryUsed() {
        return memoryUsed;
    }

    public long getDiskTotal() {
        return diskTotal;
    }

    public long getDiskUsed() {
        return diskUsed;
    }
}
