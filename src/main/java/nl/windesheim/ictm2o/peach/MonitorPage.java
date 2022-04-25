package nl.windesheim.ictm2o.peach;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MonitorPage extends JPanel {

    private Graph cpuGraph = new Graph(20, Color.green);
    private Graph memoryGraph = new Graph(20, Color.red);

    public MonitorPage() {
        cpuGraph.setPreferredSize(new Dimension(550, 200));
        memoryGraph.setPreferredSize(new Dimension(550, 200));

        setLayout(new MigLayout());
        setBorder(new EmptyBorder(new Insets(0, 30, 0, 30)));

        JLabel titleLabel = new JLabel("Dienstmonitoring");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 40));
        titleLabel.setBorder(new EmptyBorder(new Insets(10, 0, 10, 0)));
        add(titleLabel, "wrap");

        addCPUGraph();
        addMemoryGraph();

        Random random = new Random();

        Timer timer = new Timer(300, (ev) -> {
            cpuGraph.pushBack(random.nextInt(0, 100));
            cpuGraph.update();

            memoryGraph.pushBack(random.nextInt(0, 100));
            memoryGraph.update();
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

}
