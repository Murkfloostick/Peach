package nl.windesheim.ictm2o.peach;

        import javax.imageio.ImageIO;
        import javax.swing.*;
        import java.awt.*;
        import java.awt.event.MouseAdapter;
        import java.awt.event.MouseEvent;
        import java.awt.image.BufferedImage;
        import java.io.File;
        import java.io.IOException;

public class StartPage extends JPanel {
    JPanel buttonContainer1 = new JPanel();
    JPanel buttonContainer2 = new JPanel();
    JPanel buttonContainer3 = new JPanel();

    private final PeachWindow m_parent;
    private final Button m_monitorServicesButton;
    private final Button m_newDesignButton;
    private final Button m_openDesignButton;

    private JButton button1;
    private JButton button2;
    private JButton button3;

    private static JLabel labelLinks;
    private static JLabel labelCenter;
    private static JLabel labelRechts;

    private static class Button extends JPanel {

        public Button(String title) {
            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            add(titleLabel);
            setPreferredSize(new Dimension(550, 550));
        }

    }

    public StartPage(PeachWindow parent) throws IOException {
        m_parent = parent;

        m_monitorServicesButton = new Button("Monitor Services");
        m_newDesignButton = new Button("Nieuw Ontwerp");
        m_openDesignButton = new Button("Open Ontwerp");

        BufferedImage buttonIcon1 = ImageIO.read(new File("src/main/resources/IconPack/Setting.png"));
        button1 = new JButton();
        button1.setPreferredSize(new Dimension(150, 150));
        button1.setBorder(BorderFactory.createEmptyBorder());
        button1.setContentAreaFilled(false);
        button1 = new JButton(new ImageIcon(buttonIcon1));
        add(button1);

        BufferedImage buttonIcon2 = ImageIO.read(new File("src/main/resources/IconPack/Monitor.png"));
        button2 = new JButton();
        button2.setPreferredSize(new Dimension(150, 150));
        button2.setBorder(BorderFactory.createEmptyBorder());
        button2.setContentAreaFilled(false);
        button2 = new JButton(new ImageIcon(buttonIcon2));
        add(button2);

        BufferedImage buttonIcon3 = ImageIO.read(new File("src/main/resources/IconPack/OpenIcon.png"));
        button3 = new JButton();
        button3.setPreferredSize(new Dimension(150, 150));
        button3.setBorder(BorderFactory.createEmptyBorder());
        button3.setContentAreaFilled(false);
        button3 = new JButton(new ImageIcon(buttonIcon3));
        add(button3);

        buttonContainer1.add(m_monitorServicesButton, button1);
        buttonContainer2.add(m_newDesignButton, button2);
        buttonContainer3.add(m_openDesignButton, button3);

        setLayout(new FlowLayout());

        add(buttonContainer1);
        add(buttonContainer2);
        add(buttonContainer3);

        installMouseListeners();
    }

    private void installMouseListeners() {
        StartPage startPage = this;

        //m_monitorServicesButton
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m_parent.openPage(startPage, "Dienstmonitor", new MonitorPage());
                e.consume();
            }
        });

        //buttonContainer2
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m_parent.openPage(startPage, "Ontwerper", new DesignPage(m_parent));
                e.consume();
            }
        });

        //m_openDesignButton
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m_parent.openPage(startPage, "Ontwerper", new DesignPage(m_parent));
                e.consume();
            }
        });
    }

}
