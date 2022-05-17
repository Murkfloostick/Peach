package nl.windesheim.ictm2o.peach.monitor;

import net.miginfocom.swing.MigLayout;
import nl.windesheim.ictm2o.peach.Graph;
import nl.windesheim.ictm2o.peach.PeachWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MonitorPage extends JPanel {

    private class Tab extends JPanel {
        private final Graph cpuGraph = new Graph(30, Color.green, 1000);
        private final Graph memoryGraph = new Graph(30, Color.red, 100);
        private final Graph diskGraph = new Graph(30, Color.blue, 100);

        private final JLabel cpuGraphLabel = new JLabel("Processorgebruik");
        private final JLabel memoryGraphLabel = new JLabel("Werkgeheugengebruik");
        private final JLabel diskGraphLabel = new JLabel("Schijfruimteverbruik");

        public Tab() {
            setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));

            addCPUGraph();
            addMemoryGraph();
            addDiskGraph();
        }

        private void addCPUGraph() {
            cpuGraphLabel.setFont(new Font("Inter", Font.BOLD, 26));
            cpuGraphLabel.setBorder(new EmptyBorder(new Insets(5, 0, 5, 0)));
            add(cpuGraphLabel, "wrap");

            add(cpuGraph, "wrap");
        }

        private void addMemoryGraph() {
            memoryGraphLabel.setFont(new Font("Inter", Font.BOLD, 26));
            memoryGraphLabel.setBorder(new EmptyBorder(new Insets(25, 0, 5, 0)));
            add(memoryGraphLabel, "wrap");

            add(memoryGraph, "wrap");
        }

        private void addDiskGraph() {
            diskGraphLabel.setFont(new Font("Inter", Font.BOLD, 26));
            diskGraphLabel.setBorder(new EmptyBorder(new Insets(25, 0, 5, 0)));
            add(diskGraphLabel, "wrap");

            add(diskGraph, "wrap");
        }
    }

    private final HashMap<String, Tab> tabs = new HashMap<>();
    private final JTabbedPane tabbedPane = new JTabbedPane();

    public MonitorPage(PeachWindow m_parent) {
        setLayout(new MigLayout("insets 0 10% 0 10%", "[grow,fill]", "[grow,fill]"));
        setBorder(new EmptyBorder(new Insets(0, 30, 0, 30)));

        JLabel titleLabel = new JLabel("Dienstmonitoring");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 40));
        titleLabel.setBorder(new EmptyBorder(new Insets(10, 0, 10, 0)));
        add(titleLabel, "wrap");

//        fillGraphWithRandomValues(cpuGraph, 0, 100);
//        fillGraphWithRandomValues(memoryGraph, 0, 100);
//        fillGraphWithRandomValues(diskGraph, 8, 16);

        add(tabbedPane, "wrap");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(new Insets(40, 0, 0, 0)));

        final var backButton = new JButton("Terug");
        backButton.setFont(new Font("Inter", Font.BOLD, 20));
        backButton.addActionListener(ev -> m_parent.openStartPage(this));
        backButton.setToolTipText("Terug naar de hoofdpagina");
        buttonPanel.add(backButton);

        final var quitButton = new JButton("Afsluiten");
        quitButton.setFont(new Font("Inter", Font.BOLD, 20));
        quitButton.addActionListener(ev -> System.exit(0));
        quitButton.setToolTipText("Peach afsluiten");
        buttonPanel.add(quitButton);

        add(buttonPanel);

//        Random random = new Random();

        Timer timer = new Timer(100, (ev) -> {
//            cpuGraph.pushBack(random.nextInt(0, 100));
//            memoryGraph.pushBack(random.nextInt(0, 100));
//            diskGraph.pushBack(random.nextInt(8, 16));
//
            for (Map.Entry<String, MonitorDataManager.Instance> entry : MonitorDataManager.getData().entrySet()) {
                var tab = tabs.get(entry.getKey());
                if (tab == null) {
                    tab = new Tab();
                    tabs.put(entry.getKey(), tab);
                    tabbedPane.add(entry.getKey(), tab);
                }

                if (entry.getValue().newData.isEmpty())
                    continue;

                synchronized(entry.getValue().newData) {
                    for (MonitorData dataEntry : entry.getValue().newData) {
                        tab.cpuGraph.pushBack(dataEntry.getCPUPercentage());
                        tab.memoryGraph.pushBack((int) ((double)dataEntry.getMemoryUsed() / (double)dataEntry.getMemoryTotal() * 100.0));
                        tab.diskGraph.pushBack((int) ((double)dataEntry.getDiskUsed() / (double)dataEntry.getDiskTotal() * 100.0));
                    }

                    final var lastEntry = entry.getValue().newData.get(entry.getValue().newData.size() - 1);
                    tab.cpuGraphLabel.setText(String.format("Processorgebruik: %.1f%%", lastEntry.getCPUPercentage() / 10f));

                    tab.memoryGraphLabel.setText(String.format("Werkgeheugengebruik: %s van %s (%.1f%%)" , formatBytes(lastEntry.getMemoryUsed()), formatBytes(lastEntry.getMemoryTotal()),
                            ((double)lastEntry.getMemoryUsed() / (double)lastEntry.getMemoryTotal() * 100.0)));

                    tab.diskGraphLabel.setText(String.format("Schijfruimteverbruik: %s van %s (%.1f%%)" , formatBytes(lastEntry.getDiskUsed()), formatBytes(lastEntry.getDiskTotal()),
                            ((double)lastEntry.getDiskUsed() / (double)lastEntry.getDiskTotal() * 100.0)));

                    entry.getValue().newData.clear();
                }
            }

            this.repaint();
        });

        timer.start();
    }

    @NotNull
    private static String formatBytes(long input) {
        final long gigaByte = 1073741824;
        if (input > gigaByte * 2)
            return String.format("%.1f GB", (float)input / (float)gigaByte);

        final long megaByte = 1048576;
        if (input > megaByte * 2)
            return String.format("%.1f MB", (float)input / (float)gigaByte);

        return input + " bytes";
    }

//    private static void fillGraphWithRandomValues(@NotNull Graph graph, int min, int max) {
//        Random random = new Random();
//
//        for (int i = 0; i < graph.getMaxItems(); ++i) {
//            graph.pushBack(random.nextInt(min, max));
//        }
//    }

}
