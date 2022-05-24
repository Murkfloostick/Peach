package nl.windesheim.ictm2o.peach.Splashscreen;
import nl.windesheim.ictm2o.peach.PeachWindow;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import javax.imageio.ImageIO;
import nl.windesheim.ictm2o.peach.Main;
import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;
import java.io.File;
import nl.windesheim.ictm2o.peach.StartPage;
import nl.windesheim.ictm2o.peach.storage.ResourceManager;

public class Splashscreen extends JPanel {

    static JLabel logo;
    static JProgressBar progressBar;

    static final int MY_MINIMUM = 0;
    static final int MY_MAXIMUM = 100;
    
    public Splashscreen() {
        UIManager.put("ProgressBar.background", (new Color(255,255,255)));
        UIManager.put("ProgressBar.foreground", (new Color(230,155,134)));
        UIManager.put("ProgressBar.selectionBackground",(new Color(0,0,0)));
        UIManager.put("ProgressBar.selectionForeground",(new Color(255,255,255)));

        //Initializing
        progressBar = new JProgressBar();
        progressBar.setMinimum(MY_MINIMUM);
        progressBar.setMaximum(MY_MAXIMUM);
        progressBar.setBorderPainted(false);
        progressBar.setStringPainted(true);


        logo = new JLabel(new ImageIcon("src/main/resources/Peach.png"));
        JLabel tekst = new JLabel("PeachOS V0.3");
        tekst.setFont(new Font("Inter", Font.BOLD, 25));

        add(tekst);
        add(logo);
        add(progressBar);

    }

    public void updateBar(int newValue) {
        progressBar.setValue(newValue);
    }


    public static void main(String[] args) {

        final Splashscreen j = new Splashscreen();

        JFrame frame = new JFrame("Starting PEACH.OS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(j);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setSize(300,450);
        frame.setVisible(true);

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
            } catch (InterruptedException e) {
                System.out.println(e);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}