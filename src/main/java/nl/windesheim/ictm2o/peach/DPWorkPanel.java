package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.components.PlacedComponent;
import nl.windesheim.ictm2o.peach.components.Position;
import nl.windesheim.ictm2o.peach.components.RegisteredComponent;
import nl.windesheim.ictm2o.peach.components.RegisteredComponent;
import org.jetbrains.annotations.NotNull;


import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.*;
import java.awt.Component;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class DPWorkPanel extends JPanel{
    private final Design D;
    private final Map<JLabel, PlacedComponent> map = new HashMap<>();
    private final Map<PlacedComponent, PlacedComponent> lineMap = new HashMap<>();
    private final Dimension dim = new Dimension(500, 550);//Workplace

    private boolean selectieModus = false;
    private PlacedComponent firstSelection;

    @NotNull
    private final DesignPage designPage;

    public DPWorkPanel(Design D, @NotNull DesignPage designPage) {
        //TODO Dynamic dim instellen
        this.D = D;
        this.designPage = designPage;
        setBackground(Color.lightGray);

        //Slepen aanzetten
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
        map.clear();

        for (PlacedComponent PC : D.getPlacedComponents()) {
            ImageIcon icon = null;
            try {
                String iconnaam = PC.getRegisteredComponent().getIcon().name();
                icon = new ImageIcon("src/main/resources/IconPack/IconComponents/" + iconnaam + ".png");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Hó daar: " + ex.getCause(), JOptionPane.INFORMATION_MESSAGE);
                icon = new ImageIcon("src/main/resources/IconPack/IconComponents/GENERIC.png");
            }

            JLabel label = new JLabel(PC.getName(), icon, JLabel.CENTER);
            map.put(label, PC);
            add(label);

            //Breedte en hoogte moet vast staan
            //Label wordt niet geplaatst omdat breedte en hoogte 0 is als het nog niet gerenderd is
            label.setBounds(Math.toIntExact(PC.getPosition().getX()), Math.toIntExact(PC.getPosition().getY()), 150, 65);
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

        lineMap.forEach((k, v) -> g.drawLine(Math.toIntExact(k.getPosition().getX()),Math.toIntExact(k.getPosition().getY()),Math.toIntExact(v.getPosition().getX()),Math.toIntExact(v.getPosition().getY())));
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

            if (e.isPopupTrigger())
                doPop(e);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseDragged(MouseEvent e) {

            if (target != null) {
                target.setBounds(e.getX(), e.getY(), target.getWidth(), target.getHeight());
                e.getComponent().repaint();
                repaint();
                designPage.setDesignModified();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            JLabel jl = (JLabel) target;
            PlacedComponent PC;

            PC = map.get(target);

            //Memory leak?
            Position pos = new Position(jl.getX(), jl.getY());
            PC.setPosition(pos);
            target = null;
            designPage.setDesignModified();

            if (e.isPopupTrigger())
                doPop(e);
        }

        private void doPop(MouseEvent e) {
            //TODO functie van maken
            //Haal Jlabel op die is geklikt
            Container container = (Container) e.getComponent();
            for (Component c : container.getComponents()) {
                if (c.getBounds().contains(e.getPoint())) {
                    target = c;
                    break;
                }
            }
            //TODO normaal klik van maken
            if(selectieModus){
                PlacedComponent secondSelection = (PlacedComponent) map.get(target);
                lineMap.put(firstSelection, secondSelection);
                selectieModus = false;
                return;
            }
            PopUp menu = new PopUp(target);
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    class PopUp extends JPopupMenu {
        JMenuItem anItem;
        JMenuItem selecteren;
        JLabel target;

        public PopUp(Component target) {
            this.target = (JLabel) target;
            anItem = new JMenuItem("Verwijder");
            selecteren = new JMenuItem("Selecteren");
            add(selecteren);
            add(anItem);
            anItem.addActionListener(ev -> {
                verwijderComponent();
                    });
            selecteren.addActionListener(ev -> {
                selectieModusAan();
            });
        }

        public void verwijderComponent(){
            //Haal component op die verwijdert wilt worden
            PlacedComponent PC;
            PC = map.get(target);

            //En dat component verwijderen
            D.delPlacComponent(PC);
            refreshWP();
        }

        public void selectieModusAan(){
            //this.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 30)); werkt niet
            target.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
            selectieModus = true;
            firstSelection = map.get(target);
        }
    }
}
