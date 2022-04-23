package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.components.PlacedComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DPWorkPanel extends JPanel {
    private Design D;
    Rectangle area; //Dit is het gedeelte waar je dingen kan slepen
    ArrayList<Rectangle> rects = new ArrayList<>(); //Dit wordt afbeeldingen

    Rectangle rect = new Rectangle(0, 0, 100, 50);


    int preX, preY;
    boolean isFirstTime = true;

    boolean pressOut = false;
    private Dimension dim = new Dimension(500, 550);

    public DPWorkPanel(Design D) {
        this.D = D;
        setBackground(Color.lightGray);
        setPreferredSize(dim);
        addMouseMotionListener(new MyMouseAdapter());
        addMouseListener(new MyMouseAdapter());
        refreshWP();
        setVisible(true);
    }

    public void refreshWP(){
        for (PlacedComponent PC:D.getPlacedComponents()
             ) {
            Rectangle rect = new Rectangle(Math.toIntExact(PC.getPosition().getX()), Math.toIntExact(PC.getPosition().getY()), 100, 100);
            rects.add(rect);
        }
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
        if (isFirstTime) {
            area = new Rectangle(dim);
            isFirstTime = false;//Wat is dit voor???
        }

        g2d.setColor(Color.black);
        g2d.fill(rect);
        for (Rectangle rect:rects
        ) {
            g2d.setColor(Color.black);
            g2d.fill(rect);
        }
    }

    boolean checkRect() {
        if (area == null) {
            return false;
        }

        if (area.contains(rect.x, rect.y, rect.getWidth(), rect.getHeight())) {
            return true;
        }

        int new_x = rect.x;
        int new_y = rect.y;

        if ((rect.x + rect.getWidth()) > area.getWidth()) {
            new_x = (int) area.getWidth() - (int) (rect.getWidth() - 1);
        }
        if (rect.x < 0) {
            new_x = -1;
        }
        if ((rect.y + rect.getHeight()) > area.getHeight()) {
            new_y = (int) area.getHeight() - (int) (rect.getHeight() - 1);
        }
        if (rect.y < 0) {
            new_y = -1;
        }
        rect.setLocation(new_x, new_y);
        return false;
    }

    private class MyMouseAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            preX = rect.x - e.getX();
            preY = rect.y - e.getY();

            if (rect.contains(e.getX(), e.getY())) {
                updateLocation(e);
            } else {
                pressOut = true;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!pressOut) {
                updateLocation(e);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (rect.contains(e.getX(), e.getY())) {
                updateLocation(e);
            } else {
                pressOut = false;
            }
        }

        public void updateLocation(MouseEvent e) {
            rect.setLocation(preX + e.getX(), preY + e.getY());
            checkRect();
            repaint();
        }
    }
}
