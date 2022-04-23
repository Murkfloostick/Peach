package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.components.PlacedComponent;


import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class DPWorkPanel extends JPanel{
    private Design D;
    Rectangle area; //Dit is het gedeelte waar je dingen kan slepen
    ArrayList<JLabel> labels = new ArrayList<>(); //Dit wordt afbeeldingen
    private Dimension dim = new Dimension(500, 550);//Workplace

    public DPWorkPanel(Design D) {
        this.D = D;
        setBackground(Color.lightGray);
        setPreferredSize(dim);
        ComponentDragger dragger = new ComponentDragger();
        addMouseListener(dragger);
        addMouseMotionListener(dragger);
        refreshWP();
        setVisible(true);
    }

    public void refreshWP(){
        removeAll();
        updateUI();
        labels.clear();
        for (PlacedComponent PC:D.getPlacedComponents()
             ) {
            ImageIcon icon = null;
            try{
                icon = new ImageIcon("src/main/resources/Peach.png");//Wordt icon van dinges later
            } catch (Exception e) {
                e.printStackTrace();
            }
            JLabel label = new JLabel(PC.getName(), JLabel.CENTER);
            labels.add(label);
            add(label);
        }
        setVisible(false);
        setVisible(true);
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return dim;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        //Later
    }

    private class ComponentDragger extends MouseAdapter {

        private Component target;

        /**
         * {@inheritDoc}
         */
        @Override
        public void mousePressed(MouseEvent e) {
            Container container = (Container) e.getComponent();
            for (Component c : container.getComponents()) {
                if (c.getBounds().contains(e.getPoint())) {
                    target = c;
                    break;
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            if (target != null) {
                target.setBounds(e.getX(), e.getY(), target.getWidth(), target.getHeight());
                e.getComponent().repaint();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            target = null;
        }
    }
}
