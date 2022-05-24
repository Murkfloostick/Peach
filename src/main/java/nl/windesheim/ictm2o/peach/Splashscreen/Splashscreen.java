package nl.windesheim.ictm2o.peach.Splashscreen;
import nl.windesheim.ictm2o.peach.BuildInfo;
import nl.windesheim.ictm2o.peach.PeachWindow;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import java.net.URL;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import nl.windesheim.ictm2o.peach.windows.ThemedWindow;
import org.jetbrains.annotations.NotNull;
import static nl.windesheim.ictm2o.peach.windows.ThemedWindow.setThemeToSystem;


public class Splashscreen extends JPanel {

    static JLabel logo;
    static JProgressBar progressBar;

    static ImageIcon logoVenster;

    static final int MY_MINIMUM = 0;
    static final int MY_MAXIMUM = 100;
    
    public Splashscreen() {
        //super("Launching PeachOS V" + BuildInfo.getVersion());
        //setThemeToSystem();
        
        try {
            UIManager.put("ProgressBar.background", (new Color(255, 255, 255)));
            UIManager.put("ProgressBar.foreground", (new Color(230, 155, 134)));
            UIManager.put("ProgressBar.selectionBackground", (new Color(0, 0, 0)));
            UIManager.put("ProgressBar.selectionForeground", (new Color(255, 255, 255)));

            //Initializing
            progressBar = new JProgressBar();
            progressBar.setMinimum(MY_MINIMUM);
            progressBar.setMaximum(MY_MAXIMUM);
            progressBar.setBorderPainted(false);
            progressBar.setStringPainted(true);
            progressBar.setFont(new Font("Inter", Font.BOLD, 15)); //Font percentage

            logo = new JLabel(new ImageIcon("src/main/resources/Peach.png"));
            JLabel tekst = new JLabel("PeachOS V0.2");
            tekst.setFont(new Font("Inter", Font.BOLD, 25));

            add(tekst);
            add(logo);
            add(progressBar);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateBar(int newValue) {
        progressBar.setValue(newValue);
    }

    public static void main(String[] args) throws Exception {

        final Splashscreen j = new Splashscreen();

        JFrame frame = new JFrame("Launching PeachOS V"+ BuildInfo.getVersion());
        frame.setFont(new Font("Inter", Font.BOLD, 25));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(j);
        frame.pack();
        frame.setSize(350,450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        logoVenster = new ImageIcon("src/main/resources/Peach.png");
        frame.setIconImage(logoVenster.getImage());

        for (int i = MY_MINIMUM; i <= MY_MAXIMUM; i++) {
            final int percent = i;
            try {
                SwingUtilities.invokeLater(() -> {
                    j.updateBar(percent);
                });
                java.lang.Thread.sleep(20);
                if (i == MY_MAXIMUM){
                    frame.dispose();
                    PeachWindow peachWindow = new PeachWindow();
                    peachWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    peachWindow.setUndecorated(false);
                    peachWindow.setVisible(true);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}