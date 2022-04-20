package nl.windesheim.ictm2o.peach;

import javax.swing.*;
import java.awt.*;

public class DPComponPanel extends JPanel {
    private int GLrows = 0;

    public DPComponPanel(){
        setBackground(Color.gray);
        setPreferredSize(new Dimension(200, 550));
        setMinimumSize(new Dimension(200, 550));
        setLayout(new GridLayout(GLrows, 2));
//        JScrollPane scrollFrame = new JScrollPane(this);
//        setAutoscrolls(true);
//        scrollFrame.setPreferredSize(new Dimension( 800,300));
//        this.add(scrollFrame);


    }


    private static class Image extends JComponent {
        public Image() {
            //setPreferredSize(new Dimension(10, 10));
            //setSize(new Dimension(10, 10));
            setBackground(Color.BLUE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(getBackground());
            g2.fillRect(getWidth()/2, getHeight()/2, 20, 20);
        }
    }

    public void Button(String title) {
        add(new Image());

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        add(titleLabel);
        //setPreferredSize(new Dimension(50, 50));
        GLrows += 1;
        setLayout(new GridLayout(GLrows, 2));
    }
}
