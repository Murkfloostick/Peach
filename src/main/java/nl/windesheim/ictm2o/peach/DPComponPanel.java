package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DPComponPanel extends JPanel {

    class Button extends JLabel {

        @NotNull
        public RegisteredComponent registeredComponent;

        public Button(@NotNull RegisteredComponent registeredComponent) {
            super(registeredComponent.getName(), SwingConstants.CENTER);
            this.registeredComponent = registeredComponent;
        }

        @NotNull
        public RegisteredComponent getRegisteredComponent() {
            return registeredComponent;
        }
    }

    private int GLrows = 0;
    private ComponentRegistry CR;
    private Design D;
    private DPWorkPanel DPWP;

    private final DPComponPanel thisReference = this;

    //Dubbelklik om component toe te voegen aan sleeppaneel?
    MouseListener ml = new MouseAdapter(){
        public void mousePressed(MouseEvent me){
            if (me.getClickCount() == 2) {//double-click
                if (!(me.getSource() instanceof Button button)) {
                    JOptionPane.showMessageDialog(thisReference, "Een interne fout is opgetreden");
                    return;
                }

                Position pos = new Position(250,250);
                PlacedComponent PC = new PlacedComponent(button.getRegisteredComponent(),
                        button.getRegisteredComponent().getName(), pos);
                D.getPlacedComponents().add(PC);
                DPWP.refreshWP();
            }
        }
    };

    public DPComponPanel(ComponentRegistry CR, Design D, DPWorkPanel DPWP){
        this.CR = CR;
        this.D = D;
        this.DPWP = DPWP;
        setBackground(Color.gray);
        setPreferredSize(new Dimension(200, 550));
        setMinimumSize(new Dimension(200, 550));
        setLayout(new GridLayout(GLrows, 2));
        refreshPanel();
    }

    public void refreshPanel(){
        removeAll();
        updateUI();
        GLrows = 0;
        for (RegisteredComponent RC : CR.getRegisteredComponents()) {
            addButton(RC);
        }
    }

    private static class Image extends JComponent {
        private BufferedImage image;
        public Image(RegisteredComponent RC) {
            try {
                String iconnaam = RC.getIcon().name();
                //Uncommenten zodra wij icons hebben
                //image = ImageIO.read(new File("src/main/resources/Icons/" + iconnaam + ".png"));
                image = ImageIO.read(new File("src/main/resources/Peach.png"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Hó daar: " + ex.getCause(), JOptionPane.INFORMATION_MESSAGE);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);

        }
    }

    public void addButton(RegisteredComponent RC) {
        //Vergroot plek
        GLrows += 1;
        setLayout(new GridLayout(GLrows, 2));
        add(new Image(RC));
        Button button = new Button(RC);
        button.addMouseListener(ml);
        add(button);
    }
}
