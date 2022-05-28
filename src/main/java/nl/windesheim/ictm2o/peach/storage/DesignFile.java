package nl.windesheim.ictm2o.peach.storage;

import nl.windesheim.ictm2o.peach.components.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DesignFile {

    //
    // VERSION CHANGE POLICY
    //
    // The key words "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL NOT",
    // "SHOULD", "SHOULD NOT", "RECOMMENDED", "MAY", and "OPTIONAL" in this
    // document are to be interpreted as described in
    // <a href="http://www.ietf.org/rfc/rfc2119.txt" rel="help glossary">RFC 2119</a>.
    //
    // Whether or not any of the version components should change can be derived
    // from the following ruleset:
    // (1) The VERSION_MAJOR field should be bumped when one of the following
    //     happens:
    //     - A field in the file has changed type.
    //     - A required field has been removed.
    // (2) The VERSION_MINOR field should be bumped when one of the following
    //     happens:
    //     - A new REQUIRED field is added.
    // (3) The VERSION_PATCH field should be bumped when one of the following
    //     happens:
    //     - A new OPTIONAL field is added.
    //

    public static final int VERSION_MAJOR = 1;
    public static final int VERSION_MINOR = 0;
    public static final int VERSION_PATCH = 1;

    @NotNull
    public static String produceVersionString() {
        return VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_PATCH;
    }

    /**
     * Checks whether or not the given DesignFile version is compatible with
     * this file loader class.
     *
     * @return null if the version is compatibel, a message otherwise.
     */
    @Nullable
    public static String isVersionCompatible(@Nullable String version) {
        if (version == null)
            return "Dit bestand heeft geen versie. De kans is groot dat dit geen NerdyGadgets Infrastructuur Ontwerpbestand is.";

        if (version.equals(produceVersionString()))
            return null;

        final String[] parts = version.split("\\.");
        if (parts.length != 3)
            return "Het formaat van de versie is niet zoals gewoonlijk. De kans is groot dat dit geen NerdyGadgets Infrastructuur Ontwerpbestand is.";

        try {
            final int major = Integer.parseInt(parts[0]);
            final int minor = Integer.parseInt(parts[1]);
            final int patch = Integer.parseInt(parts[2]);

            if (major < 0 || minor < 0 || patch < 0)
                return "Een of meer van de versiecomponenten zijn een negatief nummer.";

            if (major != VERSION_MAJOR && minor <= VERSION_MINOR)
                return "De versie van dit bestand is te nieuw: " + version + " vergeleken met onze versie " + produceVersionString();
        } catch (NumberFormatException exception) {
            return "Een of meer van de versiecomponenten zijn geen geldig nummer.";
        }

        return null;
    }

    @NotNull
    public static FileFilter getFileFilter() {
        return new FileNameExtensionFilter("NerdyGadgets Infrastructuur Ontwerpen (*.ngio)", "ngio");
    }

    private final @NotNull File file;

    public DesignFile(@NotNull File file) {
        this.file = file;
    }

    /**
     * Loads the Design from the selected file.
     *
     * @param parent We need this parameter to show error messages. If it is
     *               null, error message windows will be supressed, and the user
     *               may be presented with unexpected results.
     */
    public @NotNull DesignLoadResult load(@Nullable JFrame parent, ComponentRegistry componentRegistry) {
        try (final var inputStream = new FileInputStream(file)) {
            JSONTokener tokener = new JSONTokener(inputStream);
            JSONObject object = new JSONObject(tokener);

            final String versionIncompatibilityMessage = isVersionCompatible(object.getString("version"));
            if (versionIncompatibilityMessage != null)
                return new DesignLoadResult("De versie van dit bestand is niet compatibel met deze versie van Peach. " + versionIncompatibilityMessage);

            float targetAvailability = object.getFloat("targetAvailability");
            JSONArray placedComponentsJSON = object.getJSONArray("placedComponents");
            List<PlacedComponent> placedComponents = new ArrayList<>(placedComponentsJSON.length());

            for (int i = 0; i < placedComponentsJSON.length(); ++i) {
                JSONObject placedComponentJSON = placedComponentsJSON.getJSONObject(i);
                RegisteredComponent registeredComponent =
                        componentRegistry.findByID(UUID.fromString(placedComponentJSON.getString("uuid")));

                if (registeredComponent == null) {
                    if (parent != null)
                        JOptionPane.showMessageDialog(parent, String.format("Item genaamd \"%s\" maakt gebruik van een onbekend component met ID \"%s\". Dit item zal niet worden weergeven op het scherm.",
                                placedComponentJSON.getString("name"), placedComponentJSON.getString("uuid")), "Windesheim Peach - Waarschuwing", JOptionPane.WARNING_MESSAGE);
                    continue;
                }

                placedComponents.add(new PlacedComponent(registeredComponent, placedComponentJSON.getString("name"),
                        new Position(placedComponentJSON.getLong("x"), placedComponentJSON.getLong("y"))));
            }

            return new DesignLoadResult(new Design(file.getAbsolutePath(), targetAvailability, placedComponents));
        } catch (Exception exception) {
            exception.printStackTrace();
            return new DesignLoadResult("Een overwachte fout is opgetreden terwijl we het bestand aan het laden waren: " + exception.getLocalizedMessage());
        }
    }

    @NotNull
    public FileSaveError save(@NotNull Design design) {
        JSONObject object = new JSONObject();
        object.put("version", produceVersionString());
        object.put("targetAvailability", design.getTargetAvailability());

        JSONArray placedComponentsJSON = new JSONArray();
        if (design.getPlacedComponents() == null)
            return FileSaveError.DESIGN_HAD_NULL_PLACED_COMPONENTS;

        for (@NotNull PlacedComponent placedComponent : design.getPlacedComponents()) {
            var placedComponentJSON = new JSONObject();

            placedComponentJSON.put("name", placedComponent.getName());
            placedComponentJSON.put("uuid", placedComponent.getRegisteredComponent().getID().toString());
            placedComponentJSON.put("x", placedComponent.getPosition().x());
            placedComponentJSON.put("y", placedComponent.getPosition().y());

            placedComponentsJSON.put(placedComponentJSON);
        }

        object.put("placedComponents", placedComponentsJSON);

        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write(object.toString());
        } catch (IOException exception) {
            exception.printStackTrace();
            return FileSaveError.GENERIC;
        }

        return FileSaveError.NONE;
    }

}
