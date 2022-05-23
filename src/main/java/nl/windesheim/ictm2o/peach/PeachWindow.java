package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.ComponentRegistry;
import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.storage.Configuration;
import nl.windesheim.ictm2o.peach.windows.CopyrightWindow;
import nl.windesheim.ictm2o.peach.windows.ThemedWindow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Objects;

public class PeachWindow extends ThemedWindow {

    @NotNull
    private final ComponentRegistry componentRegistry = new ComponentRegistry();
    private final Configuration configuration = new Configuration(this);

    private JMenuItem saveMenuItem;
    @NotNull
    private JPanel currentPage;

    public PeachWindow() throws IOException {
        super("NerdyGadgets Peach v" + BuildInfo.getVersion());

        setThemeToSystem();

        addMenuBar();

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // This is only called when the user releases the mouse button.
//                System.out.println("componentResized");
//
                Dimension screenSize = getSize();
                double width = screenSize.getWidth();
                double height = screenSize.getHeight();
                System.out.println(width + "," + height);

            }
        });

        setSize(1280, 720);
        setMinimumSize(new Dimension(1280, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        configuration.load();

        add(currentPage = new StartPage(this));
    }

    public void run() {
        setVisible(true);
    }

    public void setPageTitle(@NotNull String pageTitle) {
        super.setTitle("NerdyGadgets Peach v" + BuildInfo.getVersion() + " - " + pageTitle);
    }

    public void openStartPage(@NotNull JPanel originPanel) {
        super.setTitle("NerdyGadgets Peach v" + BuildInfo.getVersion());

        remove(originPanel);

        try {
            add(new StartPage(this));
        } catch (IOException e) {
            e.printStackTrace();
        }

        invalidate();
        revalidate();
        repaint();
    }

    public void openPage(@NotNull JPanel origin, @NotNull String title, @NotNull JPanel panel) {
        saveMenuItem.setEnabled(panel instanceof DesignPage);

        setPageTitle(title);
        remove(origin);
        add(panel);
        invalidate();
        revalidate();
        repaint();

        currentPage = panel;
    }

    private void setMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("Bestand");
        JMenuItem item = new JMenuItem("Nieuw Model");
        JMenuItem item1 = new JMenuItem("Opslaan");
        JMenuItem item2 = new JMenuItem("Opslaan Als");
        JMenuItem item3 = new JMenuItem("Dienstmonitoring");
        JMenuItem afsluiten = new JMenuItem("Afsluiten");
        afsluiten.addActionListener(e -> {
            // TODO check of het ontwerp is opgeslagen.
            System.exit(0);
        });

        file.add(item);
        file.add(item1);
        file.add(item2);
        file.add(item3);
        file.add(afsluiten);
        menuBar.add(file);
        setJMenuBar(menuBar);
    }

    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Bestand");

//    menu.setMnemonic(KeyEvent.VK_S);
        menu.getAccessibleContext().setAccessibleDescription("Het menu waarmee de bestanden kunnen worden opgeslagen enzo");
        menuBar.add(menu);

        JMenuItem menuItem = new JMenuItem("Nieuw", KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(ev -> {
            if (currentPage instanceof DesignPage designPage && !designPage.getDesign().isDesignSavedToFile())
                designPage.saveDesign(false);
            openPage(currentPage, "Nieuw Ontwerp", new DesignPage(this, this, new Design(null)));
        });
        menu.add(menuItem);

//a group of JMenuItems
        menuItem = new JMenuItem("Opslaan", KeyEvent.VK_S);
        saveMenuItem = menuItem;
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
//    menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        menuItem.addActionListener(ev -> {
            if (currentPage instanceof DesignPage designPage)
                designPage.saveDesign(false);
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Opslaan Als", KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        menuItem.addActionListener(ev -> {
            if (currentPage instanceof DesignPage designPage)
                designPage.saveDesign(true);
        });
        menu.add(menuItem);

        menu = new JMenu("Help");
        menuItem = new JMenuItem("Auteursrecht");
        menuItem.addActionListener(ev -> CopyrightWindow.open());
        menu.add(menuItem);
        menuBar.add(menu);

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
