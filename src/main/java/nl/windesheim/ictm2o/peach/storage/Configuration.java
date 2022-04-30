package nl.windesheim.ictm2o.peach.storage;

import nl.windesheim.ictm2o.peach.PeachWindow;
import nl.windesheim.ictm2o.peach.components.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Configuration {

    @NotNull
    private final PeachWindow peachWindow;

    public Configuration(@NotNull PeachWindow peachWindow) {
        this.peachWindow = peachWindow;
    }

    /**
     * Separate function to reduce the impact that the SuppressWarnings
     * annotation can make.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean createSubdirectoriesForFile(@NotNull final File file) {
        try {
            file.getParentFile().mkdirs();
            return true;
        } catch (SecurityException securityException) {
            JOptionPane.showMessageDialog(peachWindow, "Geen toegang tot configuratiemap!", "NerdyGadgets Peach", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    public void load() {
        File file = getFile();

        if (!createSubdirectoriesForFile(file))
            return;

        if (!file.exists()) {
            triggerNewConfiguration();
            return;
        }

        List<RegisteredComponent> placedComponents = new ArrayList<>();
        try {
            peachWindow.getComponentRegistry().getRegisteredComponents().clear();

            InputStream inputStream = new FileInputStream(file);

            JSONTokener tokener = new JSONTokener(inputStream);
            JSONObject object = new JSONObject(tokener);

            JSONArray registeredComponentsJSON = object.getJSONArray("registeredComponents");
            for (int i = 0; i < registeredComponentsJSON.length(); ++i) {
                JSONObject placedComponentJSON = registeredComponentsJSON.getJSONObject(i);

                placedComponents.add(new RegisteredComponent(
                        UUID.fromString(placedComponentJSON.getString("uuid")),
                        placedComponentJSON.getString("name"),
                        ComponentIcon.valueOf(placedComponentJSON.getString("icon")),
                        placedComponentJSON.getFloat("cost"),
                        placedComponentJSON.getFloat("availability")
                ));
            }

            inputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
            JOptionPane.showMessageDialog(peachWindow, "Er is een fout opgetreden terwijl de configuratie geladen werd: " + exception,
                    "NerdyGadgets Peach", JOptionPane.WARNING_MESSAGE);
        }

        peachWindow.getComponentRegistry().setRegisteredComponents(placedComponents);
    }

    public void save() {
        File file = getFile();
        if (!createSubdirectoriesForFile(file))
            return;

        try {
            if (!file.exists() &&!file.createNewFile()) {
                JOptionPane.showMessageDialog(peachWindow, "Kon de configuratie niet opslaan omdat we geen rechten hebben om een nieuw bestand aan te maken in \"" + file.getAbsolutePath() + "\"", "NerdyGadgets Peach", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            JOptionPane.showMessageDialog(peachWindow, "Kon de configuratie niet opslaan omdat we geen rechten hebben om een nieuw bestand aan te maken in \""
                    + file.getAbsolutePath() + "\"", "NerdyGadgets Peach", JOptionPane.ERROR_MESSAGE);
        }

        JSONObject object = new JSONObject();

        JSONArray registeredComponentsJSON = new JSONArray();
        for (@NotNull RegisteredComponent registeredComponent : peachWindow.getComponentRegistry().getRegisteredComponents()) {
            JSONObject registeredComponentJSON = new JSONObject();

            registeredComponentJSON.put("uuid", registeredComponent.getID().toString());
            registeredComponentJSON.put("name", registeredComponent.getName());
            registeredComponentJSON.put("icon", registeredComponent.getIcon().toString());
            registeredComponentJSON.put("cost", registeredComponent.getCost());
            registeredComponentJSON.put("availability", registeredComponent.getAvailability());

            registeredComponentsJSON.put(registeredComponentJSON);
        }

        object.put("registeredComponents", registeredComponentsJSON);

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(object.toString());
            fileWriter.close();
        } catch (Exception exception) {
            exception.printStackTrace();
            JOptionPane.showMessageDialog(peachWindow, "Er is een fout opgetreden terwijl de configuratie opgeslagen werd: " + exception,
                    "NerdyGadgets Peach", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void triggerNewConfiguration() {
        @NotNull final List<RegisteredComponent> components = peachWindow.getComponentRegistry().getRegisteredComponents();

        components.add(new RegisteredComponent(UUID.fromString("803b7358-6e5e-4074-8b0b-cb4bd669d57f"), "pfSense", ComponentIcon.FIREWALL, 4000, 0.99998f));

        components.add(new RegisteredComponent(UUID.fromString("b21f6b2b-8a40-4706-b35f-11a139da2669"), "HAL9001DB", ComponentIcon.SERVER_DATABASE, 5100, 0.90f));
        components.add(new RegisteredComponent(UUID.fromString("d5db3806-e0d1-4b2f-af99-3bc4f1fcbf04"), "HAL9002DB", ComponentIcon.SERVER_DATABASE, 7700, 0.95f));
        components.add(new RegisteredComponent(UUID.fromString("a06795be-388b-45ec-9bf6-c5074f7bc0a4"), "HAL9003DB", ComponentIcon.SERVER_DATABASE, 12200, 0.98f));

        components.add(new RegisteredComponent(UUID.fromString("2e92e587-e928-44d0-b3bb-01d4a7001164"), "HAL9001W", ComponentIcon.SERVER_WEB, 2200, 0.80f));
        components.add(new RegisteredComponent(UUID.fromString("4856e3d5-a279-41e2-89f4-1d0fb33bfe40"), "HAL9002W", ComponentIcon.SERVER_WEB, 3200, 0.90f));
        components.add(new RegisteredComponent(UUID.fromString("80f9995f-6f76-4f74-8a0f-c1c8c8edafe6"), "HAL9003W", ComponentIcon.SERVER_WEB, 5100, 0.95f));

        save();
    }

    @NotNull
    private File getFile() {
        @Nullable String operatingSystem = System.getProperty("os.name");

        if (operatingSystem == null) {
            JOptionPane.showMessageDialog(peachWindow, "Onbekend besturingssysteem, sorry!", "NerdyGadgets Peach", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return new File("/dev/null");
        }

        if (operatingSystem.toLowerCase().contains("win"))
            return new File(System.getenv("APPDATA") + "/Windesheim/Peach/Configuration.json");

        if (operatingSystem.toLowerCase().contains("linux"))
            return new File(System.getProperty("user.home") + "/.config/Windesheim/Peach/Configuration.json");

        if (operatingSystem.toLowerCase().contains("mac"))
            return new File(System.getProperty("user.home") + "/Library/Application Support/Windesheim/Peach/Configuration.json");

        JOptionPane.showMessageDialog(peachWindow, "Onbekend besturingssysteem, sorry: " + operatingSystem, "NerdyGadgets Peach", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
        return new File("/dev/null");
    }

}
