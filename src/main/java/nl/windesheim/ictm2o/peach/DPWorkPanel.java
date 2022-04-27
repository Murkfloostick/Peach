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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class DPWorkPanel extends JPanel{
    private final Design D;
    private final Map<JLabel, PlacedComponent> map = new HashMap<>();
    private final Dimension dim = new Dimension(500, 550);//Workplace

    @NotNull
    private final DesignPage designPage;

    public DPWorkPanel(Design D, @NotNull DesignPage designPage) {
        //TODO Dynamic dim instellen
        this.D = D;
        this.designPage = designPage;
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
            //label.addMouseListener(new DPWorkPanel.PopClickListener());

            //Breedte en hoogte moet vast staan
            //Label wordt niet geplaatst omdat breedte en hoogte 0 is als het nog niet gerenderd is
            label.setBounds(Math.toIntExact(PC.getPosition().getX()), Math.toIntExact(PC.getPosition().getY()), 90, 65);
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
            DPWorkPanel.PopUp menu = new DPWorkPanel.PopUp();
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    class PopUp extends JPopupMenu implements ActionListener {
        JMenuItem anItem;
        public PopUp() {
            anItem = new JMenuItem("Verwijder");
            add(anItem);
            anItem.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == anItem);{
                //Haal component op die verwijdert wilt worden
                Component invoker = getInvoker();
                PlacedComponent PC;
                PC = map.get(invoker);

                //En dat component verwijderen
                //TODO Verwijder functie maken
            }
        }
    }
}
