package nl.windesheim.ictm2o.peach;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

public class MonitorPage extends JPanel implements ActionListener {

    private final Graph cpuGraph = new Graph(20, Color.green);
    private final Graph memoryGraph = new Graph(20, Color.red);
    private final PeachWindow m_parent;
    private final JButton terugKnop;


    public MonitorPage(PeachWindow m_parent) {
        this.m_parent = m_parent;
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

        terugKnop = new JButton("Terug");
        terugKnop.setFont(new Font("Inter", Font.BOLD, 20));
        terugKnop.addActionListener(this);
        add(terugKnop);

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == terugKnop) {
            try {
                m_parent.dispose();
                PeachWindow peachWindow = new PeachWindow();
                peachWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
                peachWindow.setUndecorated(false);
                peachWindow.setVisible(true);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.exit(0);
            }
        }
    }
}
