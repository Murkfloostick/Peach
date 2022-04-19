package nl.windesheim.ictm2o.peach;

import javax.swing.*;
import java.awt.*;

public class StartPage extends JPanel {

    private static class Button extends JPanel {
        public Button() {
            setPreferredSize(new Dimension(200, 200));
        }
    }

    public StartPage() {
        add(new Button("Monitor Services"));
        add(new Button("Nieuw Ontwerp"));
        add(new Button("Open Ontwerp"));
    }

}
