package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.ComponentRegistry;
import nl.windesheim.ictm2o.peach.components.RegisteredComponent;
import static nl.windesheim.ictm2o.peach.DPWorkPanel.setLocationRelativeTo;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;



public class DPComponPanel extends JPanel {
    private int GLrows = 0;
    private ComponentRegistry CR;

    public DPComponPanel(ComponentRegistry CR){
        this.CR = CR;
        setBackground(Color.gray);
        setPreferredSize(new Dimension(200, 550));
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(200, 550));
        setLayout(new GridLayout(GLrows, 2));

        refreshPanel();
    }

    public void refreshPanel(){
        removeAll();
        updateUI();
        for (RegisteredComponent RC:CR.getRegisteredComponents()
             ) {
            Button(RC);
        }
    }

    private static class Image extends JComponent {
        public Image(RegisteredComponent RC) {
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

    public void Button(RegisteredComponent RC) {
        add(new Image(RC));

        JLabel titleLabel = new JLabel(RC.getName(), SwingConstants.CENTER);
        add(titleLabel);
        //setPreferredSize(new Dimension(50, 50));
        GLrows += 1;
        setLayout(new GridLayout(GLrows, 2));
    }
}
