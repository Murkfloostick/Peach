package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DPComponPanel extends JPanel {
    private final Map<Button, RegisteredComponent> map = new HashMap<>(); //Voor het verwijderen

    class Button extends JLabel {
        private ImageIcon image;

        @NotNull
        public RegisteredComponent registeredComponent;

        public Button(@NotNull RegisteredComponent registeredComponent) {
            super(registeredComponent.getName(), SwingConstants.CENTER);
            this.registeredComponent = registeredComponent;
            try {
                String iconnaam = registeredComponent.getIcon().name();
                image = new ImageIcon("src/main/resources/IconPack/IconComponents/" + iconnaam + ".png");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Hó daar: " + ex.getCause(), JOptionPane.INFORMATION_MESSAGE);
                image = new ImageIcon("src/main/resources/IconPack/IconComponents/GENERIC.png");
            }
            this.setIcon(image);
        }

        @NotNull
        public RegisteredComponent getRegisteredComponent() {
            return registeredComponent;
        }
    }

    class PopUp extends JPopupMenu implements ActionListener {
        JMenuItem anItem;
        public PopUp() {
            anItem = new JMenuItem("Verwijder");
            add(anItem);
            anItem.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == anItem);{
                //Haal component op die verwijdert wilt worden
                Component invoker = getInvoker();
                Button button = (Button) invoker;
                RegisteredComponent RC = button.getRegisteredComponent();

                //Check of het geplaatst is op het workpanel
                for (PlacedComponent PC:D.getPlacedComponents()
                     ) {
                    if(PC.getRegisteredComponent().getID() == RC.getID()){
                        JOptionPane.showMessageDialog(null, "Component is geplaatst. Verwijder de geplaatste component eerst voordat je de component zelf verwijderd", "Ho daar: Component kan niet verwijderd worden", JOptionPane.ERROR_MESSAGE);
                        refreshPanel();
                        break;
                    }
                }
                //En anders verwijden van de ComponentRegistry
                //TODO Aanmaken
                //CR.delComponent(RC.getID());
            }
        }
    }

    class PopClickListener extends MouseAdapter {
        //Popmenu
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
        }

        private void doPop(MouseEvent e) {
            PopUp menu = new PopUp();
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private int GLrows = 0;
    private ComponentRegistry CR;
    private Design D;
    private DPWorkPanel DPWP;
    private Dimension dim = new Dimension(350, 600);

    private final DPComponPanel thisReference = this;

    @NotNull
    private DesignPage designPage;
  
    //TODO Slepen om toe te voegen
    //Dubbelklik om component toe te voegen aan sleeppaneel
    MouseListener ml = new MouseAdapter() {
        public void mousePressed(MouseEvent me) {
            if (me.getClickCount() == 2) {//double-click
                //Voor als het fout gaat
                if (!(me.getSource() instanceof Button button)) {
                    JOptionPane.showMessageDialog(thisReference, "Een interne fout is opgetreden");
                    return;
                }

                //Maak een placedcomponent aan met de component die wordt toegevoegd.
                Position pos = new Position(250, 250);
                PlacedComponent PC = new PlacedComponent(button.getRegisteredComponent(),
                        button.getRegisteredComponent().getName(), pos);
                D.getPlacedComponents().add(PC);
                DPWP.refreshWP();
                designPage.setDesignModified();
            }
        }
    };

    public DPComponPanel(ComponentRegistry CR, Design D, DPWorkPanel DPWP, @NotNull DesignPage designPage){
        this.CR = CR;
        this.D = D;
        this.DPWP = DPWP;
        this.designPage = designPage;
        setBackground(Color.gray);
        //setPreferredSize(dim);
        //setMinimumSize(dim);
        setLayout(new GridLayout(GLrows, 2));
        refreshPanel();
    }

    public void refreshPanel(){
        removeAll();
        updateUI();
        map.clear();

        GLrows = 0;
        for (RegisteredComponent RC : CR.getRegisteredComponents()) {
            addButton(RC);
        }
    }

    public void addButton(RegisteredComponent RC) {
        //Vergroot plek
        GLrows += 1;

        //En voeg een component toe aan de lijst
        setLayout(new GridLayout(GLrows, 2));
        Button button = new Button(RC);
        button.addMouseListener(ml);
        map.put(button, RC);
        button.addMouseListener(new PopClickListener());
        add(button);
    }

    public Dimension getDim() {
        return dim;
    }
}
