package nl.windesheim.ictm2o.peach;

import nl.windesheim.ictm2o.peach.components.*;
import nl.windesheim.ictm2o.peach.storage.ResourceManager;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DPComponPanel extends JPanel {
    private final Map<Button, RegisteredComponent> map = new HashMap<>(); //Voor het verwijderen

    class Button extends JLabel {
        private ImageIcon image;

        @NotNull
        public RegisteredComponent registeredComponent;

        public Button(@NotNull RegisteredComponent registeredComponent) throws IOException {
            super("<html><body>" + registeredComponent.getName() + "<br>" + 100 * registeredComponent.getAvailability() + "%<br>" + registeredComponent.getCost() + "</body></html>", SwingConstants.CENTER);
            this.registeredComponent = registeredComponent;
            try {
                String iconnaam = registeredComponent.getIcon().name();
                image = new ImageIcon(ImageIO.read(ResourceManager.load("IconPack/IconComponents/" + iconnaam + ".png")));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Hó daar: " + ex.getCause(), JOptionPane.INFORMATION_MESSAGE);
                image = new ImageIcon(ImageIO.read(ResourceManager.load("IconPack/IconComponents/GENERIC.png")));
            }
            this.setIcon(image);

            this.setTransferHandler(new ValueExportTransferHandler("A", registeredComponent));

            this.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    JLabel lbl = (JLabel) e.getSource();
                    TransferHandler handle = lbl.getTransferHandler();
                    handle.exportAsDrag(lbl, e, TransferHandler.COPY);
                }
            });


        }

        @NotNull
        public RegisteredComponent getRegisteredComponent() {
            return registeredComponent;
        }

        protected static class ValueExportTransferHandler extends TransferHandler {

            public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;
            private String value;
            private RegisteredComponent RC;

            public ValueExportTransferHandler(String value, RegisteredComponent RC) {
                this.value = value;
                this.RC = RC;
            }

            public String getValue() {
                return value;
            }

            @Override
            public int getSourceActions(JComponent c) {
                return DnDConstants.ACTION_COPY;
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                Transferable t = new StringSelection(getValue());
                return t;
            }

            @Override
            protected void exportDone(JComponent source, Transferable data, int action) {
                super.exportDone(source, data, action);
                if (DPWorkPanel.isAccept()) {
                    Position pos = new Position(250, 250);
                    PlacedComponent PC = new PlacedComponent(RC,
                            RC.getName(), pos);
                    D.getPlacedComponents().add(PC);
                    DPWP.refreshWP();
                    designPage.setDesignModified();
                    DPWorkPanel.setAccept(false);

                    //Clean up and remove the LayerItem that was moved
                    //((Button) source).setVisible(false);
                    //((Button) source).getParent().remove((Button) source);
                }
            }

            }
        }

        class PopUp extends JPopupMenu implements ActionListener {
            JMenuItem anItem;
            JMenuItem aanpassen;

            public PopUp() {
                aanpassen = new JMenuItem("Aanpassen");
                add(aanpassen);
                aanpassen.addActionListener(this);

                anItem = new JMenuItem("Verwijder");
                add(anItem);
                anItem.addActionListener(this);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == anItem)
                {
                    //Haal component op die verwijdert wilt worden
                    Component invoker = getInvoker();
                    Button button = (Button) invoker;
                    RegisteredComponent RC = button.getRegisteredComponent();

                    //Check of het geplaatst is op het workpanel
                    for (PlacedComponent PC : D.getPlacedComponents()
                    ) {
                        if (PC.getRegisteredComponent().getID() == RC.getID()) {
                            JOptionPane.showMessageDialog(null, "Component is geplaatst. Verwijder de geplaatste component eerst voordat je de component zelf verwijderd", "Ho daar: Component kan niet verwijderd worden", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    //En anders verwijden van de ComponentRegistry
                    CR.delComponent(RC);
                }

                if (e.getSource() == aanpassen){
                    Window parentWindow = SwingUtilities.windowForComponent(this);
                    JFrame parentFrame = null;
                    if (parentWindow instanceof Frame) {
                        parentFrame = (JFrame) parentWindow;
                    }
                    DPAanpDialog dialoog = new DPAanpDialog(parentFrame, true, CR, designPage);
                    dialoog.setLocationRelativeTo(null);
                }
                refreshPanel();
                designPage.getPeachWindow().getConfiguration().save();
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
        private static Design D;
        private static DPWorkPanel DPWP;
        private Dimension dim = new Dimension(350, 600);

        private final DPComponPanel thisReference = this;

        @NotNull
        private static DesignPage designPage;

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
                    //TODO functie van maken
                    Position pos = new Position(250, 250);
                    PlacedComponent PC = new PlacedComponent(button.getRegisteredComponent(),
                            button.getRegisteredComponent().getName(), pos);
                    D.getPlacedComponents().add(PC);
                    DPWP.refreshWP();
                    designPage.setDesignModified();
                }
            }
        };

        public DPComponPanel(ComponentRegistry CR, Design D, DPWorkPanel DPWP, @NotNull DesignPage designPage) {
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

        public void refreshPanel() {
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
            Button button;
            try {
                button = new Button(RC);
            } catch (Exception e) {
                System.exit(1);
                return;
            }

            button.addMouseListener(ml);
            map.put(button, RC);
            button.addMouseListener(new PopClickListener());
            add(button);
        }

        public Dimension getDim() {
            return dim;
        }
    }

