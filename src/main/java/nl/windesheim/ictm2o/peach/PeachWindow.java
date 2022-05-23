package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.ComponentRegistry;
import nl.windesheim.ictm2o.peach.storage.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.Objects;

public class PeachWindow extends JFrame {

    @NotNull
    private final ComponentRegistry componentRegistry = new ComponentRegistry();
    private final Configuration configuration = new Configuration(this);

    public static boolean isAppleSystem() {
        @Nullable final String osName = System.getProperty("os.name");
        if (osName == null)
            return false;

        return osName.toLowerCase().contains("mac");
    }

    public PeachWindow() throws IOException {
        super("NerdyGadgets Peach v" + BuildInfo.getVersion());

        setThemeToSystem();
        if (isAppleSystem()) {
            setAppleIcon();
        } else {
            setIcon();
        }

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

        add(new StartPage(this));
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
        setPageTitle(title);
        remove(origin);
        add(panel);
        invalidate();
        revalidate();
        repaint();
    }

    private void setThemeToSystem() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (info.getClassName().equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private void setIcon() {
        try {
            setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Peach.png"))).getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAppleIcon() {
        final Taskbar taskbar = Taskbar.getTaskbar();
        taskbar.setIconImage(new ImageIcon(Objects.requireNonNull(PeachWindow.class.getResource("/Peach.png"))).getImage());
        setIconImage(new ImageIcon(Objects.requireNonNull(PeachWindow.class.getResource("/Peach.png"))).getImage());
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

    @NotNull
    public ComponentRegistry getComponentRegistry() {
        return componentRegistry;
    }

    @NotNull
    public Configuration getConfiguration() {
        return configuration;
    }

}
