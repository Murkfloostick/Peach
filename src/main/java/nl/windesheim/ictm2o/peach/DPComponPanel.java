package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.ComponentRegistry;
import nl.windesheim.ictm2o.peach.components.RegisteredComponent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DPComponPanel extends JPanel {
    private int GLrows = 0;
    private ComponentRegistry CR;

    //Dubbelklik om component toe te voegen aan sleeppaneel?
    MouseListener ml = new MouseAdapter(){
        public void mousePressed(MouseEvent me){
            if(me.getClickCount() == 2)//double-click
            {
                //Dit betekent nu dat meerdere componenten niet dezelfde naam kunnen hebben
                //Oplossing kan zijn kijken naar UUID, maar dan moet je ook kijken hoe je die gaat pakken
                JLabel jl = (JLabel) me.getSource();
                RegisteredComponent RC = null;
                for (RegisteredComponent RCfind:CR.getRegisteredComponents()
                ) {
                    if(jl.getText().equals(RCfind.getName())){
                        RC = RCfind;
                        break;
                    }
                }
                System.out.println(RC.getName());
            }
        }
    };

    public DPComponPanel(ComponentRegistry CR){
        this.CR = CR;
        setBackground(Color.gray);
        setPreferredSize(new Dimension(200, 550));
        setMinimumSize(new Dimension(200, 550));
        setLayout(new GridLayout(GLrows, 2));
        refreshPanel();
    }

    public void refreshPanel(){
        removeAll();
        updateUI();
        GLrows = 0;
        for (RegisteredComponent RC:CR.getRegisteredComponents()
             ) {
            Button(RC);
        }
    }

    private static class Image extends JComponent {
        private BufferedImage image;
        public Image(RegisteredComponent RC) {
            try {
                String iconnaam = RC.getIcon().name();
                //Uncommenten zodra wij icons hebben
                //image = ImageIO.read(new File("src/main/resources/Icons/" + iconnaam + ".png"));
                image = ImageIO.read(new File("src/main/resources/Peach.png"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Hó daar: " + ex.getCause(), JOptionPane.INFORMATION_MESSAGE);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);

        }
    }

    public void Button(RegisteredComponent RC) {
        //Vergroot plek
        GLrows += 1;
        setLayout(new GridLayout(GLrows, 2));
        add(new Image(RC));
        JLabel titleLabel = new JLabel(RC.getName(), SwingConstants.CENTER);
        titleLabel.addMouseListener(ml);
        add(titleLabel);
    }
}
