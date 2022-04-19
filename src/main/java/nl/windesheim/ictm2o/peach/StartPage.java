package nl.windesheim.ictm2o.peach;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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

    private final PeachWindow m_parent;
    private final Button m_monitorServicesButton = new Button("Monitor Services");
    private final Button m_newDesignButton = new Button("Nieuw Ontwerp");
    private final Button m_openDesignButton = new Button("Open Ontwerp");

    public StartPage(PeachWindow parent) {
        m_parent = parent;

        JPanel buttonContainer = new JPanel();
        buttonContainer.add(m_monitorServicesButton);
        buttonContainer.add(m_newDesignButton);
        buttonContainer.add(m_openDesignButton);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        add(Box.createVerticalGlue());
        add(buttonContainer);
        add(Box.createVerticalGlue());

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        installMouseListeners();
    }

    private void installMouseListeners() {
        StartPage startPage = this;

        m_monitorServicesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m_parent.openPage(startPage, "Dienstmonitor", new MonitorPage());
                e.consume();
            }
        });

        m_newDesignButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m_parent.openPage(startPage, "Ontwerper", new DesignerPage());
                e.consume();
            }
        });

        m_openDesignButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m_parent.openPage(startPage, "Ontwerper", new DesignerPage());
                e.consume();
            }
        });
    }

}
