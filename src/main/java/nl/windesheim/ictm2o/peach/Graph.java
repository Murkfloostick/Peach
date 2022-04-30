/**
 * Copyright (C) 2022 Tristan Gerritsen
 * Alle Rechten Voorbehouden
 */

package nl.windesheim.ictm2o.peach;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Graph extends JPanel {

    private List<Integer> m_points = new ArrayList<>();
    private final int maxItems;
    private final Color pointColor;

    public Graph(int maxItems, @NotNull Color pointColor) {
        this.maxItems = maxItems;
        this.pointColor = pointColor;
    }

    public void popFront() {
        m_points.remove(0);
    }

    public void pushBack(int point) {
        if (m_points.size() == maxItems)
            popFront();
        m_points.add(point);
    }

    public void update() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension size = getSize();

        g.setColor(Color.black);
        g.fillRect(0, size.height - 2, size.width, 2);

        if (m_points.size() == 0)
            return;

        final int increaseX = size.width / maxItems;
        final float yScale = size.height / 100.0f;

        Integer previousY = null;
        int x = 0;

        g.setColor(pointColor);

        for (Integer y : m_points) {
            y = (int)(y * yScale);

            if (previousY != null) {
                g.drawLine(x - increaseX, previousY, x, y);
            }

            previousY = y;
            x += increaseX;
        }
    }

}
