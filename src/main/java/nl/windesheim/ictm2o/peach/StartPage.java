package nl.windesheim.ictm2o.peach;

import net.miginfocom.swing.MigLayout;
import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.storage.DesignFile;
import nl.windesheim.ictm2o.peach.storage.ResourceManager;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class StartPage extends JPanel {

    private final PeachWindow m_parent;
    private final Button monitorServicesButton;
    private final Button newDesignButton;
    private final Button openDesignButton;

    private static class Button extends JPanel {

        private final JButton jButton;

        public Button(@NotNull String title, @NotNull BufferedImage image) {
            setLayout(new MigLayout("al center center, wrap, gapy 20"));

            jButton = new JButton(new ImageIcon(image));
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

        setLayout(new MigLayout("insets 0 10% 0 10%"));

        JPanel logoPanel = new JPanel();
        logoPanel.setBorder(new EmptyBorder(new Insets(30, 0, 30, 0)));

        JLabel logoImageLabel = new JLabel(loadIcon());
        logoPanel.add(logoImageLabel);

        JLabel logoTextLabel = new JLabel("Peach");
        logoTextLabel.setFont(new Font("Inter", Font.BOLD, 60));
        logoPanel.add(logoTextLabel);

        add(logoPanel, "wrap");

        monitorServicesButton = new Button("Monitor Services", ImageIO.read(ResourceManager.load("IconPack/Monitor.png")));
        newDesignButton = new Button("Nieuw Ontwerp", ImageIO.read(ResourceManager.load("IconPack/Setting.png")));
        openDesignButton = new Button("Ontwerp Openen", ImageIO.read(ResourceManager.load("IconPack/OpenIcon.png")));

        add(monitorServicesButton);
        add(newDesignButton);
        add(openDesignButton);

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
    private ImageIcon loadIcon() throws IOException {
        final var loadedImage = new ImageIcon(ImageIO.read(ResourceManager.load("Peach.png")));
        final var scaledImage = loadedImage.getImage().getScaledInstance(90, 90, Image.SCALE_DEFAULT);
        return new ImageIcon(scaledImage);
    }

}