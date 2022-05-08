package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.ComponentRegistry;
import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.components.PlacedComponent;
import nl.windesheim.ictm2o.peach.components.Position;
import nl.windesheim.ictm2o.peach.storage.DesignFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class DesignPage extends JPanel implements ActionListener {
    private DPComponPanel componPanel;
    private DPWorkPanel workPanel;
    private DPToevCompon toevCompon;
    private JMenuBar menuBar;

    private JMenu menu, submenu;
    private JMenuItem menu1;

    private JMenuItem menuItem;
    private JRadioButtonMenuItem rbMenuItem;
    private JCheckBoxMenuItem cbMenuItem;

    private PeachWindow m_parent;

    private ComponentRegistry CR;
    private Design D;

    private Dimension originalSize;
    public DesignPage(PeachWindow peachWindow, PeachWindow m_parent, @NotNull Design design) {
        this.m_parent = m_parent;
        this.CR = peachWindow.getComponentRegistry();
        this.D = design;
        workPanel = new DPWorkPanel(D, this);
        componPanel = new DPComponPanel(CR, D, workPanel, this);

        toevCompon = new DPToevCompon(CR, this, this.m_parent);
        JScrollPane scroller = new JScrollPane(componPanel);

        scroller.setPreferredSize(componPanel.getDim());
        //peachWindow.getContentPane().add(scroller);
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // This is only called when the user releases the mouse button.
                System.out.println("componentResized");

                Dimension screenSize = getSize();
                double width = screenSize.getWidth();
                double height = screenSize.getHeight();
                System.out.println(width + "," + height);

                  //WERKT NIET component resized wordt op verkeerde momenten en te vaak opgeroepen
//                //Componenten en beschikbaarheid terugfluiten
//                long difWidth = Math.round(originalSize.getWidth()) - Math.round(workPanel.getX());
//                long difHeight = Math.round(originalSize.getHeight()) - Math.round(workPanel.getY());
//
//                for (PlacedComponent PC:D.getPlacedComponents()
//                     ) {
//                    Position oldPos = PC.getPosition();
//                    Position newPos = new Position(oldPos.getX()-difWidth, oldPos.getY()-difHeight);
//                    PC.setPosition(newPos);
//                }
//
//                originalSize = screenSize;
//                workPanel.refreshWP();
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        setSize(950, 650);

        c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 1;

    add(scroller, c);
    //peachWindow.getContentPane().add(scroller);
        //add(componPanel, c);
    c.gridx = 1;
        c.weightx = 1.5;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
    add(workPanel, c);
    c.gridx = 2;
        c.weightx = 0.5;
        c.weighty = 1;
    add(toevCompon, c);

    menuBar = new JMenuBar();

    menu = new JMenu("Bestand");
//    menu.setMnemonic(KeyEvent.VK_S);
    menu.getAccessibleContext().setAccessibleDescription("Het menu waarmee de bestanden kunnen worden opgeslagen enzo");
    menuBar.add(menu);

        menuItem = new JMenuItem("Nieuw", KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(ev -> {
            if (!D.isDesignSavedToFile())
                saveDesign(false);
            D = new Design(null);
        });
        menu.add(menuItem);

//a group of JMenuItems
    menuItem = new JMenuItem("Opslaan", KeyEvent.VK_S);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
//    menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
    menuItem.addActionListener(ev -> {
        saveDesign(false);
    });
    menu.add(menuItem);

    menuItem = new JMenuItem("Opslaan Als", KeyEvent.VK_T);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
    menuItem.addActionListener(ev -> {
        saveDesign(true);
    });
    menu.add(menuItem);

    //Terug knop
        menu1 = new JMenuItem("Terug");
        menuBar.add(menu1);
        menu1.addActionListener(this);

        menu1.addActionListener(e -> System.exit(0));

        peachWindow.setJMenuBar(menuBar);
        setVisible(true);
        originalSize = getSize();//Voor eerste keer
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getActionCommand().equals("Terug")) {
            System.exit(0);
        }

        menu1.addActionListener(e -> {
            System.exit(0);
            setVisible(false);
        });

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
        m_parent.setPageTitle("Ontwerper - " + D.getFilePath());
    }

    public DPComponPanel getComponPanel() {
        return componPanel;
    }

    @NotNull
    public PeachWindow getPeachWindow() {
        return m_parent;
    }
}
