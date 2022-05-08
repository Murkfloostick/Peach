package nl.windesheim.ictm2o.peach;

import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

public class MonitorPage extends JPanel {

    private final Graph cpuGraph = new Graph(30, Color.green);
    private final Graph memoryGraph = new Graph(30, Color.red);
    private final Graph diskGraph = new Graph(30, Color.blue);
    private final PeachWindow m_parent;

    public MonitorPage(PeachWindow m_parent) {
        this.m_parent = m_parent;
//        cpuGraph.setPreferredSize(new Dimension(550, 200));
//        memoryGraph.setPreferredSize(new Dimension(550, 200));

        setLayout(new MigLayout("insets 0 10% 0 10%", "[grow,fill]", "[grow,fill]"));
        setBorder(new EmptyBorder(new Insets(0, 30, 0, 30)));

        JLabel titleLabel = new JLabel("Dienstmonitoring");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 40));
        titleLabel.setBorder(new EmptyBorder(new Insets(10, 0, 10, 0)));
        add(titleLabel, "wrap");

        fillGraphWithRandomValues(cpuGraph, 0, 100);
        fillGraphWithRandomValues(memoryGraph, 0, 100);
        fillGraphWithRandomValues(diskGraph, 8, 16);

        addCPUGraph();
        addMemoryGraph();
        addDiskGraph();

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

        Random random = new Random();

        Timer timer = new Timer(350, (ev) -> {
            cpuGraph.pushBack(random.nextInt(0, 100));
            memoryGraph.pushBack(random.nextInt(0, 100));
            diskGraph.pushBack(random.nextInt(8, 16));

            this.repaint();
        });

        timer.start();
    }

    private void addCPUGraph() {
        JLabel label = new JLabel("Processorgebruik");
        label.setFont(new Font("Inter", Font.BOLD, 26));
        label.setBorder(new EmptyBorder(new Insets(5, 0, 5, 0)));
        add(label, "wrap");

        add(cpuGraph, "wrap");
    }

    private void addMemoryGraph() {
        JLabel label = new JLabel("Werkgeheugengebruik");
        label.setFont(new Font("Inter", Font.BOLD, 26));
        label.setBorder(new EmptyBorder(new Insets(25, 0, 5, 0)));
        add(label, "wrap");

        add(memoryGraph, "wrap");
    }

    private void addDiskGraph() {
        JLabel label = new JLabel("Schijfruimteverbruik");
        label.setFont(new Font("Inter", Font.BOLD, 26));
        label.setBorder(new EmptyBorder(new Insets(25, 0, 5, 0)));
        add(label, "wrap");

        add(diskGraph, "wrap");
    }

    private static void fillGraphWithRandomValues(@NotNull Graph graph, int min, int max) {
        Random random = new Random();

        for (int i = 0; i < graph.getMaxItems(); ++i) {
            graph.pushBack(random.nextInt(min, max));
        }
    }

}
