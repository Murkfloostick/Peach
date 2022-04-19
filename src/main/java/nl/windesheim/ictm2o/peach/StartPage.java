package nl.windesheim.ictm2o.peach;

import javax.swing.*;
import java.awt.*;

public class StartPage extends JPanel {

    private static class Button extends JPanel {
        private static class Image extends JComponent {
            public Image() {
                setPreferredSize(new Dimension(200, 200));
                setSize(new Dimension(200, 200));
                setBackground(Color.BLUE);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        }

        public Button(String title) {
            add(new Image());

            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            add(titleLabel);
            setPreferredSize(new Dimension(250, 250));
        }
    }

    public StartPage(PeachWindow parent) {
        JPanel buttonContainer = new JPanel();
        buttonContainer.add(new Button("Monitor Services"));
        buttonContainer.add(new Button("Nieuw Ontwerp"));
        buttonContainer.add(new Button("Open Ontwerp"));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(Box.createVerticalGlue());
        add(buttonContainer);
        add(Box.createVerticalGlue());

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

}
