package nl.windesheim.ictm2o.peach.windows;

import nl.windesheim.ictm2o.peach.PeachWindow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ThemedWindow extends JFrame {

    public static boolean isAppleSystem() {
        @Nullable final String osName = System.getProperty("os.name");
        if (osName == null)
            return false;

        return osName.toLowerCase().contains("mac");
    }

    public ThemedWindow(@NotNull String title) {
        super(title);

        if (isAppleSystem()) {
            setAppleIcon();
        } else {
            setIconNormal();
        }
    }

    public static void setThemeToSystem() {
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

    private void setIconNormal() {
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

}
