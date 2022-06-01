package nl.windesheim.ictm2o.peach.components;

import nl.windesheim.ictm2o.peach.storage.ResourceManager;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public enum ComponentIcon {

    SERVER_WEB("Webserver"),
    SERVER_DATABASE("Database Server"),
    FIREWALL("Firewall");


    @NotNull
    private final String displayName;

    private ImageIcon imageIcon = null;

    ComponentIcon(@NotNull String displayName) {
        this.displayName = displayName;
    }

    @NotNull
    public String getDisplayName() {
        return displayName;
    }

    @NotNull
    public ImageIcon getImageIcon() throws IOException {
        if (imageIcon == null) {
            synchronized (displayName) {
                try {
                    imageIcon = new ImageIcon(ImageIO.read(ResourceManager.load("IconPack/IconComponents/" + name() + ".png")));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Hó daar: " + ex.getCause(), JOptionPane.INFORMATION_MESSAGE);
                    imageIcon = new ImageIcon(ImageIO.read(ResourceManager.load("IconPack/IconComponents/GENERIC.png")));
                }
            }
        }

        return imageIcon;
    }

}
