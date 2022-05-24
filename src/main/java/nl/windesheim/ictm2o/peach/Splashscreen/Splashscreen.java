package nl.windesheim.ictm2o.peach.Splashscreen;
import nl.windesheim.ictm2o.peach.Main;
import nl.windesheim.ictm2o.peach.PeachWindow;
import nl.windesheim.ictm2o.peach.StartPage;
import nl.windesheim.ictm2o.peach.storage.ResourceManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Splashscreen extends JPanel {

    JLabel logo;
    JLabel percentage;
    JProgressBar progressBar;

    static final int MY_MINIMUM = 0;
    static final int MY_MAXIMUM = 100;




    public Splashscreen() throws IOException {
        //Initializing
        progressBar = new JProgressBar();
        progressBar.setMinimum(MY_MINIMUM);
        progressBar.setMaximum(MY_MAXIMUM);

        logo = new JLabel(new ImageIcon("src/main/resources/Peach.png"));

        add(logo);
//        percentage = new JLabel("%0");
//        for (int i = 0; i <= 100; i++) {
//            percentage.setText("%"+i);
//            add(percentage);
//        }
        add(progressBar);

    }

    public void updateBar(int newValue) {
        progressBar.setValue(newValue);
    }

    public static void main(String args[]) throws IOException {

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
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        j.updateBar(percent);
                    }
                });
                java.lang.Thread.sleep(100);
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