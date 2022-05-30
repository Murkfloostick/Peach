package nl.windesheim.ictm2o.peach.design;

import nl.windesheim.ictm2o.peach.DesignPage;
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

    static class Button extends JLabel {

        @NotNull
        public final RegisteredComponent registeredComponent;

        public Button(@NotNull RegisteredComponent registeredComponent) throws IOException {
            super("<html><body>" + registeredComponent.getName() + "<br>" + 100 * registeredComponent.getAvailability() + "%<br>" + registeredComponent.getCost() + "</body></html>", SwingConstants.CENTER);
            this.registeredComponent = registeredComponent;
            //TODO Local variable?
            ImageIcon image;
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
    }

        protected static class ValueExportTransferHandler extends TransferHandler {

            @NotNull
            private final String value;
            private final RegisteredComponent RC;

            public ValueExportTransferHandler(@NotNull String value, RegisteredComponent RC) {
                this.value = value;
                this.RC = RC;
            }

            @Override
            public int getSourceActions(JComponent ignored) {
                return DnDConstants.ACTION_COPY;
            }

            @Override
            protected Transferable createTransferable(JComponent ignored) {
                return new StringSelection(value);
            }

            @Override
            protected void exportDone(JComponent source, Transferable data, int action) {
                super.exportDone(source, data, action);
                if (DPWorkPanel.isAccept()) {
                    Position pos = new Position(250L, 250L);
                    final var placedComponent = new PlacedComponent(RC, RC.getName(), pos);
                    D.getPlacedComponents().add(placedComponent);
                    DPWP.refreshWP();
                    designPage.setDesignModified();
                    DPWorkPanel.setAccept(false);
                }
            }
        }


        class PopUp extends JPopupMenu implements ActionListener {
            private final JMenuItem anItem;
            private final JMenuItem aanpassen;

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
                //Haal component op die verwijdert/aangepast wilt worden
                Component invoker = getInvoker();
                Button button = (Button) invoker;
                RegisteredComponent RC = button.getRegisteredComponent();

                if (e.getSource() == anItem) {
                    //Check of het geplaatst is op het workpanel
                    assert D.getPlacedComponents() != null;
                    for (@NotNull final var placedComponent : D.getPlacedComponents()) {
                        if (placedComponent.getRegisteredComponent().getID() == RC.getID()) {
                            JOptionPane.showMessageDialog(null, "Component is geplaatst. Verwijder de geplaatste component eerst voordat je de component zelf verwijderd", "Ho daar: Component kan niet verwijderd worden", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        refreshPanel();
                    }
                    //En anders verwijden van de ComponentRegistry
                    CR.delComponent(RC);
                }

                if (e.getSource() == aanpassen) {
                    Window parentWindow = SwingUtilities.windowForComponent(this);
                    JFrame parentFrame = null;
                    if (parentWindow instanceof JFrame) {
                        parentFrame = (JFrame) parentWindow;
                    }
                    button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
                    DPAanpDialog dialoog = new DPAanpDialog(parentFrame, true, designPage, RC);
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
        private final ComponentRegistry CR;
        private static Design D;
        private static DPWorkPanel DPWP;
        private final Dimension dim = new Dimension(350, 600);

        private final DPComponPanel thisReference = this;
        private static DesignPage designPage;

        //Dubbelklik om component toe te voegen aan sleeppaneel
        final MouseListener ml = new MouseAdapter() {
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
                    if (D.getPlacedComponents() == null)
                        throw new RuntimeException("PlacedComponents == null");
                    D.getPlacedComponents().add(PC);
                    DPWP.refreshWP();
                    designPage.setDesignModified();
                }
            }
        };

        public DPComponPanel(ComponentRegistry CR, Design D, DPWorkPanel DPWP, @NotNull DesignPage designPage) {
            this.CR = CR;
            DPComponPanel.D = D;
            DPComponPanel.DPWP = DPWP;
            DPComponPanel.designPage = designPage;

            setBackground(Color.gray);
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


