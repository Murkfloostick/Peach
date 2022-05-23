package nl.windesheim.ictm2o.peach.Splashscreen;
import nl.windesheim.ictm2o.peach.Main;
import nl.windesheim.ictm2o.peach.storage.ResourceManager;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public class splashscreen {
    private final Main main;

    public splashscreen(Main main) {
        this.main = main;
    }

    public static void main(String[] args) throws Exception {
        JWindow window = new JWindow();

        window.getContentPane().add(new JLabel("test", (Icon) ImageIO.read(ResourceManager.load("resources/Peach.png")), SwingConstants.CENTER));
        window.setBounds(500, 150, 300, 200);
        window.setVisible(true);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        window.setVisible(false);
        window.dispose();
        Main.main(null);

    }
}
