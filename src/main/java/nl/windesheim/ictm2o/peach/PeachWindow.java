package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.ComponentRegistry;
import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.monitor.MonitorPage;
import nl.windesheim.ictm2o.peach.monitor.MonitorServer;
import nl.windesheim.ictm2o.peach.storage.Configuration;
import nl.windesheim.ictm2o.peach.windows.CopyrightWindow;
import nl.windesheim.ictm2o.peach.windows.ThemedWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class PeachWindow extends ThemedWindow {

    @NotNull
    private final ComponentRegistry componentRegistry = new ComponentRegistry();
    private final Configuration configuration = new Configuration(this);

    private JMenuItem startPageMenuBarItem;
    private JMenuItem saveMenuBarItem;
    private JMenuItem saveAsMenuBarItem;
    private JMenuItem serviceMonitorBarItem;

    @NotNull
    private JPanel currentPage;

    public PeachWindow() throws IOException {
        super("NerdyGadgets Peach v" + BuildInfo.getVersion());

        setThemeToSystem();

        addMenuBar();

        //OG:
        //setSize(1280, 720);
        setMinimumSize(new Dimension(1280, 720));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        configuration.load();

        saveMenuBarItem.setEnabled(false);
        saveAsMenuBarItem.setEnabled(false);
        currentPage = new StartPage(this);
        add(currentPage);
    }

    public void run() {
        setVisible(true);
    }

    public void setPageTitle(@NotNull String pageTitle) {
        setTitle("NerdyGadgets Peach v" + BuildInfo.getVersion() + " - " + pageTitle);
    }

    public void openStartPage(@NotNull JPanel originPanel) {
        setTitle("NerdyGadgets Peach v" + BuildInfo.getVersion());

        remove(originPanel);
        startPageMenuBarItem.setEnabled(false);
        saveMenuBarItem.setEnabled(false);
        serviceMonitorBarItem.setEnabled(true);

        try {
            currentPage = new StartPage(this);
            add(currentPage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        invalidate();
        revalidate();
        repaint();
    }

    public void openPage(@NotNull JPanel origin, @NotNull String title, @NotNull JPanel panel) {
        saveMenuBarItem.setEnabled(panel instanceof DesignPage);
        saveAsMenuBarItem.setEnabled(panel instanceof DesignPage);
        serviceMonitorBarItem.setEnabled(!(panel instanceof MonitorPage));
        startPageMenuBarItem.setEnabled(true);

        setPageTitle(title);
        remove(origin);
        add(panel);
        invalidate();
        revalidate();
        repaint();

        currentPage = panel;
    }

    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Bestand");

        fileMenu.getAccessibleContext().setAccessibleDescription("Het menu waarmee de bestanden kunnen worden opgeslagen enzo");
        menuBar.add(fileMenu);

        startPageMenuBarItem = new JMenuItem("Hoofdmenu");
        startPageMenuBarItem.setEnabled(false);
        startPageMenuBarItem.addActionListener(ev -> {
            if (currentPage instanceof StartPage)
                return;

            if (currentPage instanceof DesignPage designPage)
                designPage.saveDesign(false);
            openStartPage(currentPage);
        });

        fileMenu.add(startPageMenuBarItem);
        fileMenu.add(new JSeparator());

        JMenuItem menuItem = new JMenuItem("Nieuw", KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(ev -> {
            if (currentPage instanceof DesignPage designPage && !designPage.getDesign().isDesignSavedToFile())
                designPage.saveDesign(false);
            openPage(currentPage, "Nieuw Ontwerp", new DesignPage(this, this, new Design(null)));
        });
        fileMenu.add(menuItem);

//a group of JMenuItems
        menuItem = new JMenuItem("Opslaan", KeyEvent.VK_S);
        saveMenuBarItem = menuItem;
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(ev -> {
            if (currentPage instanceof DesignPage designPage)
                designPage.saveDesign(false);
        });
        fileMenu.add(menuItem);

        saveAsMenuBarItem = new JMenuItem("Opslaan Als", KeyEvent.VK_T);
        saveAsMenuBarItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        saveAsMenuBarItem.addActionListener(ev -> {
            if (currentPage instanceof DesignPage designPage)
                designPage.saveDesign(true);
        });
        fileMenu.add(saveAsMenuBarItem);

        serviceMonitorBarItem = new JMenuItem("Dienstmonitoring");
        serviceMonitorBarItem.addActionListener(ev -> {
            if (currentPage instanceof DesignPage designPage)
                designPage.saveDesign(false);
            openPage(currentPage, "Dienstmonitoring", new MonitorPage(this));
        });

        JMenuItem quitItem = new JMenuItem("Afsluiten");
        quitItem.addActionListener(ev -> {
            if (currentPage instanceof DesignPage designPage)
                designPage.saveDesign(false);
            MonitorServer.getInstance().stop();
            System.exit(0);
        });

        fileMenu.add(new JSeparator());
        fileMenu.add(serviceMonitorBarItem);
        fileMenu.add(quitItem);
        menuBar.add(fileMenu);

        final var helpMenu = new JMenu("Help");
        final var copyrightItem = new JMenuItem("Auteursrecht");
        copyrightItem.addActionListener(ev -> CopyrightWindow.open());
        helpMenu.add(copyrightItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    @NotNull
    public ComponentRegistry getComponentRegistry() {
        return componentRegistry;
    }

    @NotNull
    public Configuration getConfiguration() {
        return configuration;
    }

}
