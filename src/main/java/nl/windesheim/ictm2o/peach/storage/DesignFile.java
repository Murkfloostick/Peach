package nl.windesheim.ictm2o.peach.storage;

import nl.windesheim.ictm2o.peach.components.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.*;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DesignFile {

    private final @NotNull File file;

    public DesignFile(@NotNull File file) {
        this.file = file;
    }

    public @Nullable Design load(ComponentRegistry componentRegistry) {
        try {
            InputStream inputStream = new FileInputStream(file);

            JSONTokener tokener = new JSONTokener(inputStream);
            JSONObject object = new JSONObject(tokener);

            float targetAvailability = object.getFloat("targetAvailability");
            List<PlacedComponent> placedComponents = new ArrayList<>();

            JSONArray placedComponentsJSON = object.getJSONArray("placedComponents");
            for (int i = 0; i < placedComponentsJSON.length(); ++i) {
                JSONObject placedComponentJSON = placedComponentsJSON.getJSONObject(i);
                RegisteredComponent registeredComponent =
                        componentRegistry.findByID(UUID.fromString(placedComponentJSON.getString("uuid")));

                if (registeredComponent == null)
                    continue;

                placedComponents.add(new PlacedComponent(registeredComponent, placedComponentJSON.getString("name"),
                        new Position(placedComponentJSON.getLong("x"), placedComponentJSON.getLong("y"))));
            }

            inputStream.close();
            return new Design(file.getAbsolutePath(), targetAvailability, placedComponents);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void save(@NotNull Design design) {
        JSONObject object = new JSONObject();
        object.append("targetAvailability", design.getTargetAvailability());

        JSONArray placedComponentsJSON = new JSONArray();
        for (PlacedComponent placedComponent : design.getPlacedComponents()) {
            JSONObject placedComponentJSON = new JSONObject();

            placedComponentJSON.append("name", placedComponent.getName());
            placedComponentJSON.append("uuid", placedComponent.getRegisteredComponent().getID().toString());
            placedComponentJSON.append("x", placedComponent.getPosition().getX());
            placedComponentJSON.append("y", placedComponent.getPosition().getY());

            placedComponentsJSON.put(placedComponentJSON);
        }

        object.append("placedComponents", placedComponentsJSON);

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(object.toString());
            fileWriter.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
