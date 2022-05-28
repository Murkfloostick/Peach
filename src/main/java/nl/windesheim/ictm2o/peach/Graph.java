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

    private final List<Integer> m_points = new ArrayList<>();
    private final int maxItems;
    private final Color pointColor;
    private final int pointScale;

    public Graph(int maxItems, @NotNull Color pointColor, int pointScale) {
        this.maxItems = maxItems;
        this.pointColor = pointColor;
        this.pointScale = pointScale;
    }

    public void popFront() {
        m_points.remove(0);
    }

    public void pushBack(int point) {
        if (m_points.size() == maxItems)
            popFront();
        m_points.add(point);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final var graphics2D = (Graphics2D) g;
        Dimension size = getSize();

        final int bottomBorderWidth = 2;
        g.setColor(Color.black);
        g.fillRect(0, size.height - bottomBorderWidth, size.width, bottomBorderWidth);

        if (m_points.size() == 0)
            return;

        final int increaseX = size.width / (maxItems - 2);
        final float yScale = (size.height - bottomBorderWidth) / ((float) pointScale);

        Integer previousY = null;
        int x = 0;

        g.setColor(pointColor);
        graphics2D.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Integer y : m_points) {
            y = size.height - bottomBorderWidth - (int) (y * yScale);

            if (previousY != null) {
                g.drawLine(x - increaseX, previousY, x, y);
            }

            previousY = y;
            x += increaseX;
        }
    }
}
