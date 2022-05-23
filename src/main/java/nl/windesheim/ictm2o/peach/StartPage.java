package nl.windesheim.ictm2o.peach;

import net.miginfocom.swing.MigLayout;
import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.monitor.MonitorPage;
import nl.windesheim.ictm2o.peach.storage.DesignFile;
import nl.windesheim.ictm2o.peach.storage.ResourceManager;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Method;
import java.io.File;

public class StartPage extends JPanel implements ActionListener {

    private final PeachWindow m_parent;
    private final Button monitorServicesButton;
    private final Button newDesignButton;
    private final Button openDesignButton;
    private final JButton logoImageLabel;


    private static class Button extends JPanel {

        @NotNull
        public static ImageIcon getResizedImage(@NotNull BufferedImage bufferedImage, int size) {
            final var loadedImage = new ImageIcon(bufferedImage);
            final var scaledImage = loadedImage.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT);
            return new ImageIcon(scaledImage);
        }

        private final JButton jButton;

        public Button(@NotNull Dimension windowDimensions, @NotNull String title, @NotNull BufferedImage image) {
//            setLayout(new MigLayout("al center center, wrap, gapy 20"));
            setLayout(new MigLayout("", "[grow,fill]"));


            jButton = new JButton(getResizedImage(image, windowDimensions.width / 3));
            add(jButton, "wrap");

            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
            add(titleLabel, "al center");
        }

        public void installMouseListener(MouseListener mouseListener) {
            jButton.addMouseListener(mouseListener);
        }

    }

    public StartPage(PeachWindow parent) throws IOException {
        m_parent = parent;

        System.out.println("StartPage: " + parent.getSize());

        //setLayout(new MigLayout("insets 0 10% 0 10%", "[grow,fill]"));

        JPanel logoPanel = new JPanel();
        //logoPanel.setBorder(new EmptyBorder(new Insets(30, 0, 30, 0)));

        logoImageLabel = new JButton(loadIcon(90));
        logoImageLabel.setContentAreaFilled(true);
        logoImageLabel.addActionListener(this);
        logoPanel.add(logoImageLabel);

        JLabel logoTextLabel = new JLabel("Peach");
        logoTextLabel.setFont(new Font("Inter", Font.BOLD, 60));
        logoPanel.add(logoTextLabel);


        //add(logoPanel, "wrap");
        add(logoPanel, "wrap");
        setBorder(new EmptyBorder(new Insets(30, 0, 30, 0)));
        setLayout(new FlowLayout(FlowLayout.LEFT));





//        .setHorizontalAlignment(SwingConstants.LEFT);
//        pan1 = new JPanel( new FlowLayout(FlowLayout.LEFT) );

//        JLabel logoImageLabel = new JLabel(loadIcon());
//        logoPanel.add(logoImageLabel);
//
//        JLabel logoTextLabel = new JLabel("Peach");
//        logoTextLabel.setFont(new Font("Inter", Font.BOLD, 60));
//        logoPanel.add(logoTextLabel);




        final var dimensions = parent.getSize();
        monitorServicesButton = new Button(dimensions, "Monitor Services", ImageIO.read(ResourceManager.load("IconPack/Monitor.png")));
        newDesignButton = new Button(dimensions, "Nieuw Ontwerp", ImageIO.read(ResourceManager.load("IconPack/Setting.png")));
        openDesignButton = new Button(dimensions, "Ontwerp Openen", ImageIO.read(ResourceManager.load("IconPack/OpenIcon.png")));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new MigLayout("center", "[grow,fill]"));

        buttonPanel.add(monitorServicesButton);
        buttonPanel.add(newDesignButton);
        buttonPanel.add(openDesignButton);

        add(buttonPanel, "wrap");

        /*
        Bij 1X iets openen en dan terug naar 'Startpage', worden de icoontjes groter? Dit moet worden gefixt
         */
        installMouseListeners();
    }

    private void installMouseListeners() {
        StartPage startPage = this;

        monitorServicesButton.installMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m_parent.openPage(startPage, "Dienstmonitor", new MonitorPage(m_parent));
                e.consume();
            }
        });

        newDesignButton.installMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                m_parent.openPage(startPage, "Ontwerper - Nieuw Ontwerp",
                        new DesignPage(m_parent, m_parent, new Design(null)));
                e.consume();
            }
        });

        openDesignButton.installMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                e.consume();

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(DesignFile.getFileFilter());

                int option = fileChooser.showOpenDialog(m_parent);
                if (option != JFileChooser.APPROVE_OPTION)
                    return;

                var file = fileChooser.getSelectedFile();
                assert file != null;

                final var designFile = new DesignFile(file);
                final var result = designFile.load(m_parent, m_parent.getComponentRegistry());

                if (result.isFailure()) {
                    JOptionPane.showMessageDialog(m_parent, result.getErrorMessage(), "Windesheim Peach - Fout",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                assert result.getDesign() != null;
                m_parent.openPage(startPage, "Ontwerper", new DesignPage(m_parent, m_parent, result.getDesign()));
            }
        });
    }

    @NotNull
    public static ImageIcon loadIcon(int size) throws IOException {
        final var loadedImage = new ImageIcon(ImageIO.read(ResourceManager.load("Peach.png")));
        final var scaledImage = loadedImage.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT);
        return new ImageIcon(scaledImage);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoImageLabel){
            System.exit(0);
        }
    }
}