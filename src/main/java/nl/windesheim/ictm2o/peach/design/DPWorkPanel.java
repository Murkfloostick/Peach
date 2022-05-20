package nl.windesheim.ictm2o.peach.design;

import nl.windesheim.ictm2o.peach.DesignPage;
import nl.windesheim.ictm2o.peach.components.Design;
import nl.windesheim.ictm2o.peach.components.PlacedComponent;
import nl.windesheim.ictm2o.peach.components.Position;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.*;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class DPWorkPanel extends JPanel {
    private final Design D;
    private final Map<JLabel, PlacedComponent> map = new HashMap<>();
    private final Map<PlacedComponent, ArrayList<PlacedComponent>> lineMap = new HashMap<>();
    private final Dimension dim = new Dimension(500, 550);//Workplace
    private static boolean accept = false;

    private boolean selectieModus = false;
    private PlacedComponent firstSelection;
    private boolean verwijderModus = false;

    private final JLabel beschikbaarheid = new JLabel("Beschikbaarheid: 0%");

    @NotNull
    private final DesignPage designPage;
    private final DPToevCompon toevCompon;
    private JLabel firstSelectionLabel;

    Rectangle bounds;

    public DPWorkPanel(Design D, @NotNull DesignPage designPage, DPToevCompon toevCompon) {
        //TODO In selectie modus alle andere input uit
        this.D = D;
        this.designPage = designPage;
        this.toevCompon = toevCompon;
        setBackground(Color.lightGray);


        //Slepen aanzetten

        setLayout(null);

        ComponentDragger dragger = new ComponentDragger();
        addMouseListener(dragger);
        addMouseMotionListener(dragger);
        this.setTransferHandler(new ValueImportTransferHandler());
        refreshWP();
        setVisible(true);

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
//                // This is only called when the user releases the mouse button.
//                System.out.println("componentResized");
//
//                Dimension screenSize = getSize();
//                double width = screenSize.getWidth();
//                double height = screenSize.getHeight();
//                System.out.println(width + "," + height);
                //^Wtf is deze size van?

                keepComponentsInside();
            }
        });
    }

    public void keepComponentsInside(){
        for (PlacedComponent PC:D.getPlacedComponents()
        ) {
            //Haal bounds op van werkpaneel. Moet via andere jpanels omdat deze de bounds van de workpanel niet kloppen.
            bounds = new Rectangle(designPage.getComponPanel().getX(), 0, toevCompon.getX()-toevCompon.getWidth(), toevCompon.getHeight());

            //Check of placed component binnen NIET binnen zit
            if(!bounds.contains(PC.getPosition().getX(), PC.getPosition().getY())){
                //TODO plaats op outer border
                Position Pos = new Position(bounds.width/2, bounds.height/2);
                PC.setPosition(Pos);
            }
        }
        //Zodat de nieuwe posities worden geupdate
        refreshWP();
    }
    public void refreshWP() {
        //Alles leeghalen
        removeAll();
        updateUI();
        map.clear();

        add(beschikbaarheid);

        toevCompon.refreshGegevens();

        for (PlacedComponent PC : D.getPlacedComponents()) {
            ImageIcon icon;
            try {
                String iconnaam = PC.getRegisteredComponent().getIcon().name();
                icon = new ImageIcon("src/main/resources/IconPack/IconComponents/" + iconnaam + ".png");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Hó daar: " + ex.getCause(), JOptionPane.INFORMATION_MESSAGE);
                icon = new ImageIcon("src/main/resources/IconPack/IconComponents/GENERIC.png");
            }

            JLabel label = new JLabel(PC.getName(), icon, JLabel.CENTER);
            map.put(label, PC);
            add(label);

            //Breedte en hoogte moet vast staan
            //Label wordt niet geplaatst omdat breedte en hoogte 0 is als het nog niet gerenderd is
            label.setBounds(Math.toIntExact(PC.getPosition().getX()), Math.toIntExact(PC.getPosition().getY()), 150, 65);
        }
        setVisible(false);
        setVisible(true);

        invalidate();
        validate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return dim;
    }

    public static boolean isAccept() {
        return accept;
    }

    public static void setAccept(boolean accept) {
        DPWorkPanel.accept = accept;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        lineMap.forEach((k, v) -> {
            for (PlacedComponent pc : v
            ) {
                g.drawLine(Math.toIntExact(k.getPosition().getX()) + 30, Math.toIntExact(k.getPosition().getY()) + 30, Math.toIntExact(pc.getPosition().getX()) + 30, Math.toIntExact(pc.getPosition().getY() + 30));
            }
        });

        //Gebruik dit code om de border te laten zien
//        try{
//            //Voor debug.
//            g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
//        } catch (Exception E){
//            //fuck
//        }
    }

    private class ComponentDragger extends MouseAdapter {

        private Component target;

        /**
         * {@inheritDoc}
         */
        @Override
        public void mousePressed(MouseEvent e) {
            Container container = (Container) e.getComponent();
            for (Component c : container.getComponents()) {
                if (c.getBounds().contains(e.getPoint())) {
                    target = c;
                    break;
                }
            }

            if (e.isPopupTrigger())
                doPop(e);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            if (target != null && target != beschikbaarheid) {
                target.setBounds(e.getX(), e.getY(), target.getWidth(), target.getHeight());
                e.getComponent().repaint();

                designPage.setDesignModified();
            }

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            JLabel jl = (JLabel) target;
            PlacedComponent PC;

            PC = map.get(target);

            //Memory leak?
            Position pos = new Position(jl.getX(), jl.getY());
            PC.setPosition(pos);
            repaint();
            target = null;
            designPage.setDesignModified();
            keepComponentsInside();

            if (e.isPopupTrigger())
                doPop(e);
        }

        private void doPop(MouseEvent e) {
            //TODO functie van maken
            //Haal Jlabel op die is geklikt
            Container container = (Container) e.getComponent();
            for (Component c : container.getComponents()) {
                if (c.getBounds().contains(e.getPoint())) {
                    target = c;
                    break;
                }
            }

            //TODO normaal klik van maken
            if (selectieModus && !verwijderModus) {
                PlacedComponent secondSelection = map.get(target);
                ArrayList<PlacedComponent> pcList = lineMap.get(firstSelection);
                if (pcList == null) {
                    pcList = new ArrayList<>();
                    pcList.add(secondSelection);
                } else {
                    AtomicBoolean bestaatAl = new AtomicBoolean(false);
                    lineMap.forEach((key, value) -> {
                        if (value.equals(secondSelection)) {
                            bestaatAl.set(true);
                        }
                    });
                    if (!bestaatAl.get()) {
                        pcList.add(secondSelection);
                    } else {
                        JOptionPane.showMessageDialog(null, "Een component kan niet meer dan een lijn per component hebben", "Ho daar: Component kan geen lijn trekken", JOptionPane.ERROR_MESSAGE);
                    }
                }
                lineMap.put(firstSelection, pcList);

                //TODO Functie van maken
                firstSelectionLabel.setBorder(null);
                selectieModus = false;
                verwijderModus = false;
                firstSelection = null;
                firstSelectionLabel = null;
                return;
            } else if (verwijderModus) {
                PlacedComponent secondSelection = map.get(target);
                ArrayList<PlacedComponent> pcList = lineMap.get(firstSelection);
                //TODO Functie van maken
                ArrayList<PlacedComponent> v = lineMap.get(firstSelection);
                for (PlacedComponent pc : v
                ) {
                    map.forEach((key, value) -> {
                        if (value.equals(pc)) {
                            key.setBorder(null);
                        }
                    });
                }
                pcList.remove(secondSelection);
                lineMap.put(firstSelection, pcList);
                firstSelectionLabel.setBorder(null);
                selectieModus = false;
                verwijderModus = false;
                firstSelection = null;
                firstSelectionLabel = null;
            }


            PopUp menu = new PopUp(target);
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    class PopUp extends JPopupMenu {
        JMenuItem anItem;
        JMenuItem selecteren;
        JMenuItem verwijderLijn;
        JLabel target;

        public PopUp(Component target) {
            this.target = (JLabel) target;
            anItem = new JMenuItem("Verwijder");
            selecteren = new JMenuItem("Selecteren");
            verwijderLijn = new JMenuItem("Verwijder lijn(en)");

            add(selecteren);
            //Check of component lijnen heeft
            PlacedComponent PC;
            PC = map.get(target);
            ArrayList<PlacedComponent> v = lineMap.get(PC);
            AtomicBoolean lijnGevonden = new AtomicBoolean(false); //Intellij wou dit
            if (v != null) {
                for (PlacedComponent pc : v
                ) {
                    map.forEach((key, value) -> {
                        if (value.equals(pc)) {
                            lijnGevonden.set(true);
                        }
                    });
                    //Anders werkt break niet, optimalisatie
                    if (lijnGevonden.get()) {
                        add(verwijderLijn);
                        verwijderLijn.addActionListener(ev -> {
                            selectieModusAan();
                            verwijderenAan();
                        });
                        break;
                    }
                }
            }
            add(anItem);
            anItem.addActionListener(ev -> verwijderComponent());
            selecteren.addActionListener(ev -> selectieModusAan());

        }

        public void verwijderComponent() {
            //Haal component op die verwijdert wilt worden
            PlacedComponent PC;
            PC = map.get(target);

            //En dat component verwijderen
            D.delPlacComponent(PC);
            refreshWP();
        }

        public void selectieModusAan() {
            //this.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 30)); werkt niet
            target.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
            selectieModus = true;
            firstSelection = map.get(target);
            firstSelectionLabel = target;
        }

        public void verwijderenAan() {
            verwijderModus = true;
            ArrayList<PlacedComponent> v = lineMap.get(firstSelection);
            //TODO Functie van maken
            for (PlacedComponent pc : v
            ) {
                map.forEach((key, value) -> {
                    if (value.equals(pc)) {
                        key.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    }
                });
            }
        }
    }

    protected static class ValueImportTransferHandler extends TransferHandler {

        public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;

        public ValueImportTransferHandler() {
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            return support.isDataFlavorSupported(SUPPORTED_DATE_FLAVOR);
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            accept = false;
            if (canImport(support)) {
                try {
                    Transferable t = support.getTransferable();
                    Object value = t.getTransferData(SUPPORTED_DATE_FLAVOR);
                    if (value instanceof String) { // Ensure no errors
                        System.out.println(value);
                        accept = true;
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
            return accept;
        }
    }
}
