package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.components.PlacedComponent;
import nl.windesheim.ictm2o.peach.components.Position;
import nl.windesheim.ictm2o.peach.components.RegisteredComponent;


import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class DPWorkPanel extends JPanel{
    private Design D;
    private ArrayList<JLabel> labels = new ArrayList<>(); //Dit wordt afbeeldingen
    private Map<JLabel, PlacedComponent> map = new HashMap<>();
    private Dimension dim = new Dimension(500, 550);//Workplace

    private static Insets insets = null;
    private static Dimension size = null;

    public DPWorkPanel(Design D) {
        this.D = D;
        setBackground(Color.lightGray);
        ComponentDragger dragger = new ComponentDragger();
        addMouseListener(dragger);
        addMouseMotionListener(dragger);
        setLayout(null);
        setPreferredSize(dim);
        refreshWP();
        setVisible(true);
    }

    public void refreshWP(){
        //Alles leeghalen
        removeAll();
        updateUI();
        labels.clear();
        map.clear();

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
            map.put(label, PC);
            add(label);
            //Breedte en hoogte moet vast staan
            //Wilt niet ophalen als het niet gerenderd wordt
            label.setBounds(Math.toIntExact(PC.getPosition().getX()), Math.toIntExact(PC.getPosition().getY()), 50, 50);
        }
        setVisible(false);
        setVisible(true);

        invalidate();
        validate();
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
            JLabel jl = (JLabel) target;
            PlacedComponent PC = null;
            //Werkt niet met meerdere labels met dezelfde naam
//            for (PlacedComponent PCfind:D.getPlacedComponents()
//            ) {
//                if(jl.getText().equals(PCfind.getName())){
//                    PC = PCfind;
//                    break;
//                }
//            }

            PC = map.get(target);

            //Memory leak?
            Position pos = new Position(jl.getX(), jl.getY());
            PC.setPosition(pos);
            target = null;
        }
    }
}
