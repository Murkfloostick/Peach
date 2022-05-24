package nl.windesheim.ictm2o.peach.monitor;

import net.miginfocom.swing.MigLayout;
import nl.windesheim.ictm2o.peach.Graph;
import nl.windesheim.ictm2o.peach.PeachWindow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MonitorPage extends JPanel {

    @Nullable
    public static MonitorPage instance = null;

    private class Tab extends JPanel {
        private final Graph cpuGraph = new Graph(30, Color.green, 1000);
        private final Graph memoryGraph = new Graph(30, Color.red, 100);
        private final Graph diskGraph = new Graph(30, Color.blue, 100);


        private final Font labelFont = new Font("Inter", Font.BOLD, 26);

        private final JLabel availabilityLabel = new JLabel("Beschikbaarheid");
        private final JLabel cpuGraphLabel = new JLabel("Processorgebruik");
        private final JLabel memoryGraphLabel = new JLabel("Werkgeheugengebruik");
        private final JLabel diskGraphLabel = new JLabel("Schijfruimteverbruik");
        private final JLabel otherInformationLabel = new JLabel("Overige Gegevens:");

        private final JProgressBar availabilityProgressBar = new JProgressBar();
        private final JTextArea otherInformationArea = new JTextArea();
        private final JLabel uptimeTitleLabel = new JLabel("Uptime:");
        private final JLabel uptimeValueLabel = new JLabel("");
        private final JLabel downtimeTitleLabel = new JLabel("Downtime:");
        private final JLabel downtimeValueLabel = new JLabel("");

        @NotNull
        private final String title;

        public Tab(@NotNull String title) {
            this.title = title;

            setLayout(new MigLayout("left", "[grow,fill]", "[grow,fill]"));

            addAvailabilityBar();
            addCPUGraph();
            addMemoryGraph();
            addDiskGraph();
            addOtherInformationArea();
        }

        private void addAvailabilityBar() {
            availabilityLabel.setFont(labelFont);
            availabilityLabel.setBorder(new EmptyBorder(new Insets(5, 0, 5, 0)));
            add(availabilityLabel, "wrap");

            uptimeTitleLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 18));
            uptimeValueLabel.setFont(new Font(labelFont.getName(), Font.PLAIN, 18));
            downtimeTitleLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 18));
            downtimeValueLabel.setFont(new Font(labelFont.getName(), Font.PLAIN, 18));
            JPanel panel = new JPanel();
            panel.setLayout(new MigLayout());
            panel.add(uptimeTitleLabel);
            panel.add(uptimeValueLabel, "wrap");
            panel.add(downtimeTitleLabel);
            panel.add(downtimeValueLabel);
            add(panel, "wrap");

            availabilityProgressBar.setMaximum(100);
            add(availabilityProgressBar, "span");
        }

        private void addCPUGraph() {
            cpuGraphLabel.setFont(labelFont);
            cpuGraphLabel.setBorder(new EmptyBorder(new Insets(5, 0, 5, 0)));
            add(cpuGraphLabel, "wrap");

            add(cpuGraph, "wrap");
        }

        private void addMemoryGraph() {
            memoryGraphLabel.setFont(labelFont);
            memoryGraphLabel.setBorder(new EmptyBorder(new Insets(25, 0, 5, 0)));
            add(memoryGraphLabel, "wrap");

            add(memoryGraph, "wrap");
        }

        private void addDiskGraph() {
            diskGraphLabel.setFont(labelFont);
            diskGraphLabel.setBorder(new EmptyBorder(new Insets(25, 0, 5, 0)));
            add(diskGraphLabel, "wrap");

            add(diskGraph, "wrap");
        }

        private void addOtherInformationArea() {
            otherInformationLabel.setFont(labelFont);
            add(otherInformationLabel, "wrap");

            otherInformationArea.setFont(new Font(labelFont.getName(), Font.PLAIN, 18));
            otherInformationArea.setEditable(false);
            add(otherInformationArea, "wrap");
        }

        public void updateAvailability(@NotNull final MonitorDataManager.Instance instance) {
            var availability = (float) instance.ticksAvailable / (float) instance.totalTicksSinceSubscription * 100.0f;
            if (!Float.isNaN(availability)) {
                availabilityLabel.setText(String.format(Locale.ITALIAN, "Beschikbaarheid: %.1f%%", availability));
                availabilityProgressBar.setValue((int) availability);
                availabilityProgressBar.setForeground(Color.GREEN);
            } else {
                availabilityLabel.setText("Beschikbaarheid: berekenen...");
                availabilityProgressBar.setForeground(Color.RED);
            }
        }

        public void updateLabels(@NotNull MonitorData monitorData, @NotNull MonitorDataManager.Instance instance) {
            updateAvailability(instance);

            cpuGraphLabel.setText(String.format("Processorgebruik: %.2f%%", monitorData.getCPUPercentage() / 10f));

            memoryGraphLabel.setText(String.format("Werkgeheugengebruik: %s van %s (%.1f%%)", formatBytes(monitorData.getMemoryUsed()), formatBytes(monitorData.getMemoryTotal()),
                    ((double) monitorData.getMemoryUsed() / (double) monitorData.getMemoryTotal() * 100.0)));

            diskGraphLabel.setText(String.format("Schijfruimteverbruik: %s van %s (%.1f%%)", formatBytes(monitorData.getDiskUsed()), formatBytes(monitorData.getDiskTotal()),
                    ((double) monitorData.getDiskUsed() / (double) monitorData.getDiskTotal() * 100.0)));

            uptimeValueLabel.setText(formatElapsedSeconds(instance.ticksAvailable));
            downtimeValueLabel.setText(formatElapsedSeconds(instance.ticksUnavailable));

            final var otherInformation = "IP-Adres: " + instance.address + "\n"
                    + "\n"
                    + "Processen: " + monitorData.getProcessCount() + "\n"
                    + "Windows Services: " + monitorData.getWindowsServicesCount() + "\n"
                    + "\n"
                    + "Bytes Verzonden: " + formatBytes(monitorData.getBytesSentCount()) + "\n"
                    + "Bytes Ontvangen: " + formatBytes(monitorData.getBytesReceivedCount()) + "\n";

            if (!otherInformationArea.getText().equals(otherInformation))
                otherInformationArea.setText(otherInformation);
        }
    }

    private final HashMap<String, Tab> tabs = new HashMap<>();
    private final JTabbedPane tabbedPane = new JTabbedPane();

    @NotNull
    private final PeachWindow m_parent;

    @NotNull
    private final Timer m_timer;

    public MonitorPage(@NotNull PeachWindow parent) {
        if (instance != null)
            throw new IllegalStateException("Instance is already set!");

        instance = this;
        m_parent = parent;

        if (!MonitorServer.getInstance().isStarted())
            MonitorServer.startInBackground();

        setLayout(new MigLayout("insets 0 10% 0 10%", "[grow,fill]", "[grow,fill]"));
        setBorder(new EmptyBorder(new Insets(0, 30, 0, 30)));

        JLabel titleLabel = new JLabel("Dienstmonitoring");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 40));
        titleLabel.setBorder(new EmptyBorder(new Insets(10, 0, 10, 0)));
        add(titleLabel, "wrap");

        add(tabbedPane, "wrap");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(new Insets(40, 0, 0, 0)));

        final var backButton = new JButton("Terug");
        backButton.setFont(new Font("Inter", Font.BOLD, 20));
        backButton.addActionListener(ev -> backToStartPage());
        backButton.setToolTipText("Terug naar de hoofdpagina");
        buttonPanel.add(backButton);

        final var quitButton = new JButton("Afsluiten");
        quitButton.setFont(new Font("Inter", Font.BOLD, 20));
        quitButton.addActionListener(ev -> System.exit(0));
        quitButton.setToolTipText("Peach afsluiten");
        buttonPanel.add(quitButton);

        add(buttonPanel);

        m_timer = new Timer(100, (ev) -> {
            tick();
            this.repaint();
        });

        fillInitialTabs();
        m_timer.start();
    }

    @NotNull
    private static String formatBytes(long input) {
        final long gigaByte = 1073741824;
        if (input > gigaByte * 2)
            return String.format("%.1f GB", (float) input / (float) gigaByte);

        final long megaByte = 1048576;
        if (input > megaByte * 2)
            return String.format("%.1f MB", (float) input / (float) megaByte);

        return input + " bytes";
    }

    public void backToStartPage() {
        m_timer.stop();
        m_parent.openStartPage(this);
        instance = null;
    }

    @NotNull
    private Tab getTabByIdentifier(@NotNull String identifier) {
        var tab = tabs.get(identifier);
        if (tab != null)
            return tab;

        tab = new Tab(identifier);
        tabs.put(identifier, tab);
        final var components = tabbedPane.getComponents();
        int i = 0;
        for (; i < components.length; ++i)
            if (((Tab)components[i]).title.compareTo(identifier) > 0)
                break;
        tabbedPane.insertTab(identifier, null, tab, null, i);
        return tab;
    }

    private void tick() {
        for (Map.Entry<String, MonitorDataManager.Instance> entry : MonitorDataManager.getData().entrySet()) {
            final var tab = getTabByIdentifier(entry.getKey());

            synchronized (entry.getValue().newData) {
                for (MonitorData dataEntry : entry.getValue().newData) {
                    tab.cpuGraph.pushBack(dataEntry.getCPUPercentage());
                    tab.memoryGraph.pushBack((int) ((double) dataEntry.getMemoryUsed() / (double) dataEntry.getMemoryTotal() * 100.0));
                    tab.diskGraph.pushBack((int) ((double) dataEntry.getDiskUsed() / (double) dataEntry.getDiskTotal() * 100.0));
                }

                tab.updateLabels(entry.getValue().allData.get(entry.getValue().allData.size() - 1),
                        entry.getValue());
                entry.getValue().newData.clear();
            }
        }
    }

    // Als er al MonitorData is, wat hoogst waarschijnlijk het geval is, moeten
    // we ervoor zorgen dat deze dat al gevuld is voordat de pagina zichtbaar
    // wordt.
    private void fillInitialTabs() {
        for (Map.Entry<String, MonitorDataManager.Instance> entry : MonitorDataManager.getData().entrySet()) {
            final var tab = getTabByIdentifier(entry.getKey());

            if (entry.getValue().newData.isEmpty())
                continue;

            synchronized (entry.getValue().allData) {
                for (MonitorData dataEntry : entry.getValue().allData) {
                    tab.cpuGraph.pushBack(dataEntry.getCPUPercentage());
                    tab.memoryGraph.pushBack((int) ((double) dataEntry.getMemoryUsed() / (double) dataEntry.getMemoryTotal() * 100.0));
                    tab.diskGraph.pushBack((int) ((double) dataEntry.getDiskUsed() / (double) dataEntry.getDiskTotal() * 100.0));
                }

                tab.updateLabels(entry.getValue().allData.get(entry.getValue().allData.size() - 1),
                        entry.getValue());
            }
        }
    }

    @NotNull
    private static String formatElapsedSeconds(int value) {
        final int MINUTE = 60;
        final int HOUR = MINUTE * 60;
        final int DAY = HOUR * 24;
        final int WEEK = DAY * 7;
        String result = "";

        if (value >= WEEK) {
            if (value < 2 * WEEK)
                result += "1 week, ";
            else
                result += (value / WEEK) + " weken, ";
            value %= WEEK;
        }

        if (value >= DAY) {
            if (value < 2 * DAY)
                result += "1 dag, ";
            else
                result += (value / DAY) + " dagen, ";
            value %= DAY;
        }

        if (value >= HOUR) {
            if (value < 2 * HOUR)
                result += "1 uur, ";
            else
                result += (value / HOUR) + " uren, ";
            value %= HOUR;
        }

        if (value >= MINUTE) {
            if (value < 2 * MINUTE)
                result += "1 minuut, ";
            else
                result += (value / MINUTE) + " minuten, ";
            value %= MINUTE;
        }

        if (value == 0)
            if (result.isEmpty())
                return "0 seconden";
            else
                return result.substring(0, result.length() - 2);

        return result + value + " seconde" + (value == 1 ? "" : "n");
    }

}
