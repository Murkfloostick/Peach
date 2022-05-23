package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.ComponentRegistry;
import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.design.DPComponPanel;
import nl.windesheim.ictm2o.peach.design.DPToevCompon;
import nl.windesheim.ictm2o.peach.design.DPWorkPanel;
import nl.windesheim.ictm2o.peach.storage.DesignFile;
import nl.windesheim.ictm2o.peach.windows.CopyrightWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class DesignPage extends JPanel implements ActionListener {
    private final DPComponPanel componPanel;
    private final PeachWindow m_parent;
    private final DPWorkPanel workPanel;

    @NotNull
    private Design D;

    public DesignPage(PeachWindow peachWindow, PeachWindow m_parent, @NotNull Design design) {
        this.m_parent = m_parent;
        ComponentRegistry CR = peachWindow.getComponentRegistry();
        this.D = design;


        DPToevCompon toevCompon = new DPToevCompon(CR, this, this.m_parent, D);
        workPanel = new DPWorkPanel(D, this, toevCompon);
        componPanel = new DPComponPanel(CR, D, workPanel, this);
        JScrollPane scroller = new JScrollPane(componPanel);

        scroller.setPreferredSize(componPanel.getDim());
        //peachWindow.getContentPane().add(scroller);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setSize(950, 650);

        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 1;

        add(scroller, c);
        c.gridx = 1;
        c.weightx = 1.5;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        add(workPanel, c);
        c.gridx = 2;
        c.weightx = 0.5;
        c.weighty = 1;
        add(toevCompon, c);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getActionCommand().equals("Terug")) {
            System.exit(0);
        }
    }

    public void setDesignModified() {
        D.setModified();
        if (D.getFilePath() == null)
            m_parent.setPageTitle("Ontwerper - Nieuw Ontwerp*");
        else
            m_parent.setPageTitle("Ontwerper - " + D.getFilePath() + "*");
    }

    public void saveDesign(boolean forceFileDialog) {
        DesignFile designFile;

        if (!forceFileDialog && D.isDesignSavedToFile())
            return;

        if (forceFileDialog || D.getFilePath() == null) {
            var fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Ontwerp opslaan als");
            fileChooser.setFileFilter(DesignFile.getFileFilter());

            final var selection = fileChooser.showSaveDialog(m_parent);
            if (selection != JFileChooser.APPROVE_OPTION)
                return;

            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(".ngio"))
                path += ".ngio";

            D.setFilePath(path);
            designFile = new DesignFile(new File(path));
        } else {
            designFile = new DesignFile(new File(D.getFilePath()));
        }

        designFile.save(D);
        D.setSavedToFile();
        m_parent.setPageTitle("Ontwerper - " + D.getFilePath());
    }

    public DPComponPanel getComponPanel() {
        return componPanel;
    }

    @NotNull
    public PeachWindow getPeachWindow() {
        return m_parent;
    }

    public DPWorkPanel getWorkPanel() {
        return workPanel;
    }

    @NotNull
    public Design getDesign() {
        return D;
    }

    public void setDesign(@NotNull final Design d) {
        D = d;
    }
}
