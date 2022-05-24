package nl.windesheim.ictm2o.peach.Splashscreen;
import javax.swing.*;

public class Splashscreen extends JPanel {

    JProgressBar progressBar;
    static final int MY_MINIMUM = 0;
    static final int MY_MAXIMUM = 100;

    public Splashscreen() {
        //Initializing
        progressBar = new JProgressBar();
        progressBar.setMinimum(MY_MINIMUM);
        progressBar.setMaximum(MY_MAXIMUM);

        add(progressBar);
    }

    public void updateBar(int newValue) {
        progressBar.setValue(newValue);
    }

    public static void main(String args[]) {

        final Splashscreen it = new Splashscreen();

        JFrame frame = new JFrame("Starting PEACH.OS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(it);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setSize(300,300);
        frame.setVisible(true);

        for (int i = MY_MINIMUM; i <= MY_MAXIMUM; i++) {
            final int percent = i;
            try {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        it.updateBar(percent);
                    }
                });
                java.lang.Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}