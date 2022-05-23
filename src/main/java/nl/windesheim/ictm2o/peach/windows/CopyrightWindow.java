package nl.windesheim.ictm2o.peach.windows;

import net.miginfocom.swing.MigLayout;
import nl.windesheim.ictm2o.peach.BuildInfo;
import nl.windesheim.ictm2o.peach.StartPage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CopyrightWindow extends ThemedWindow {

    public static void open() {
        new Thread(() -> {
            try {
                new CopyrightWindow().setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e, "NerdyGadgets Peach - Auteursrecht - Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    private static class Entry {

        @NotNull
        private final String name;

        @NotNull
        private final String license;

        public Entry(@NotNull String name, @NotNull String license) {
            this.name = name;
            this.license = license;
        }

        @NotNull
        public String getName() {
            return name;
        }

        @NotNull
        public String getLicense() {
            return license;
        }
    }

    @NotNull
    private static final ArrayList<Entry> ITEMS = new ArrayList<>();
    static {
        ITEMS.add(new Entry("NerdyGadgets Peach", Licenses.SOFTWARE_PEACH));
        ITEMS.add(new Entry("Annotations (JetBrains)", Licenses.LIBRARY_JETBRAINS_ANNOTATIONS));
        ITEMS.add(new Entry("Inter Lettertype (door Rasmus Andersson)", Licenses.FONT_INTER));
        ITEMS.add(new Entry("JSON-java (org.json)", Licenses.LIBRARY_JSON_JAVA));
        ITEMS.add(new Entry("JUnit", Licenses.LIBRARY_JUNIT));
        ITEMS.add(new Entry("MigLayout", Licenses.LIBRARY_MIG_LAYOUT));
    }


    private final JScrollPane licenseScrollPane;
    private final Font licenseFieldFont = new Font("Inter", Font.PLAIN, 16);

    public CopyrightWindow() throws IOException {
        super("NerdyGadgets Peach v" + BuildInfo.getVersion() + " - Auteursrecht");

        setSize(800, 600);

        final var contentPanel = this;
//        final var contentPanel = new JPanel();
//        contentPanel.
        setLayout(new MigLayout("insets 10%", "[grow,fill]"));

//        final var scrollPane = new JScrollPane(contentPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.setMaximumSize(new Dimension(600, 400));
//        setContentPane(scrollPane);

        JPanel logoPanel = new JPanel();
        logoPanel.setBorder(new EmptyBorder(new Insets(0, 0, 18, 0)));

        //JLabel logoImageLabel = new JLabel(StartPage.loadIcon(60));
        //logoPanel.add(logoImageLabel);

        JLabel logoTextLabel = new JLabel("Peach - Auteursrecht");
        logoTextLabel.setFont(new Font("Inter", Font.BOLD, 50));
        logoPanel.add(logoTextLabel);

        contentPanel.add(logoPanel, "wrap");

        final var descriptionText = new JLabel("<html>We hebben met plezier de volgende werken gebruikt tijdens het maken van NerdyGadgets Peach.</html");
        descriptionText.setFont(new Font("Inter", Font.PLAIN, 20));
        descriptionText.setBorder(new EmptyBorder(new Insets(0, 0, 18, 0)));
        contentPanel.add(descriptionText, "wrap");

        final var selection = new JComboBox<>(ITEMS.stream().map(Entry::getName).toArray());
        selection.addItemListener(e -> changeTo(selection.getSelectedIndex()));
        contentPanel.add(selection, "wrap");


        licenseScrollPane = new JScrollPane(null, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        licenseScrollPane.setMaximumSize(null);
        contentPanel.add(licenseScrollPane, "pushy, growy");

        changeTo(0);
    }

    private void changeTo(int item) {
        final var licenseField = new JTextArea();
        licenseField.setFont(licenseFieldFont);
        licenseField.setText(ITEMS.get(item).getLicense());
        licenseField.invalidate();
        licenseField.setLineWrap(true);
        licenseField.setWrapStyleWord(true);

        licenseField.setBorder(null);
        licenseField.setOpaque(false);
        licenseField.setEditable(false);
        licenseField.setLayout(new BorderLayout());

        licenseScrollPane.setViewportView(licenseField);
        licenseField.setSelectionStart(0);
        licenseField.setSelectionEnd(0);
    }

}
