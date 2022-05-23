package nl.windesheim.ictm2o.peach;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class Toast extends JDialog {

    private static final int FADE_DELAY = 20;
    private static final int FADE_STEPS = 30;
    private static final int PAUSE_BETWEEN_ANIMATIONS = 60;

    private static final int NON_TRANSLUCENT_TIMEOUT = (FADE_DELAY + PAUSE_BETWEEN_ANIMATIONS + FADE_DELAY) * FADE_STEPS;

    private final Timer fadeOutTimer = new Timer(FADE_DELAY, new ActionListener() {
        private int fadeInCount = 0;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (fadeInCount >= FADE_STEPS) {
                dispose();
                fadeOutTimer.stop();
                return;
            }

            ++fadeInCount;
            setOpacity(1.0f - (float) fadeInCount / (float) FADE_STEPS);
        }
    });

    private final Timer fadeInTimer = new Timer(FADE_DELAY, new ActionListener() {
        private int fadeInCount = 0;

        @Override
        public void actionPerformed(ActionEvent e) {
            ++fadeInCount;

            if (fadeInCount >= FADE_STEPS) {
                if (fadeInCount - FADE_STEPS - PAUSE_BETWEEN_ANIMATIONS == 0) {
                    fadeOutTimer.start();
                    fadeInTimer.stop();
                }
                return;
            }

            setOpacity((float) fadeInCount / (float) FADE_STEPS);
        }
    });

    private final Timer nonTranslucentTimer = new Timer(NON_TRANSLUCENT_TIMEOUT, e -> {
        Toast.this.nonTranslucentTimer.stop();
        dispose();
    });

    public Toast(@NotNull JFrame window, @NotNull String message) {
        setUndecorated(true);
        setAlwaysOnTop(true);
        setFocusableWindowState(false);
        setLayout(new GridBagLayout());

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.setBackground(new Color(160, 160, 160));
        JLabel toastLabel = new JLabel(message);
        toastLabel.setForeground(Color.WHITE);
        panel.add(toastLabel);
        add(panel);
        pack();

//        int xcoord = window.getLocationOnScreen().x + window.getWidth() / 2 - getWidth() / 2;
//        int ycoord = window.getLocationOnScreen().y + (int)((double)window.getHeight() * 0.75) - getHeight() / 2;
        int xcoord = window.getLocationOnScreen().x + window.getWidth() - (int) (getWidth() * 1.5f);
        int ycoord = window.getLocationOnScreen().y + window.getHeight() - (int) (getHeight() * 1.5f);
        setLocation(xcoord, ycoord);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));

        if (isOpacitySupported()) {
            setOpacity(0.0f);
            fadeInTimer.start();
        } else {
            nonTranslucentTimer.start();
        }

        setVisible(true);
    }

    private boolean isOpacitySupported() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);
    }
}
