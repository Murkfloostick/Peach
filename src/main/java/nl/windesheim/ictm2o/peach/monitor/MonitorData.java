package nl.windesheim.ictm2o.peach.monitor;

public class MonitorData {

    private final int cpuPercentage;

    private final long memoryTotal;
    private final long memoryUsed;

    private final long diskTotal;
    private final long diskUsed;

    private final long processCount;
    private final long windowsServicesCount;

    private final long bytesSentCount;
    private final long bytesReceivedCount;

    public MonitorData(int cpuPercentage, long memoryTotal, long memoryUsed, long diskTotal, long diskUsed,
                       long processCount, long windowsServicesCount, long bytesSentCount, long bytesReceivedCount) {
        this.cpuPercentage = cpuPercentage;

        this.memoryTotal = memoryTotal;
        this.memoryUsed = memoryUsed;

        this.diskTotal = diskTotal;
        this.diskUsed = diskUsed;

        this.processCount = processCount;
        this.windowsServicesCount = windowsServicesCount;

        this.bytesSentCount = bytesSentCount;
        this.bytesReceivedCount = bytesReceivedCount;
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

    public long getProcessCount() {
        return processCount;
    }

    public long getWindowsServicesCount() {
        return windowsServicesCount;
    }

    public long getBytesSentCount() {
        return bytesSentCount;
    }

    public long getBytesReceivedCount() {
        return bytesReceivedCount;
    }
}
