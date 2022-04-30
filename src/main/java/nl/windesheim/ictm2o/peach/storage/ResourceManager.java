package nl.windesheim.ictm2o.peach.storage;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public class ResourceManager {
    
    /**
     * Example:
     *     For src/main/resources/Peach.png use ResourceManager.load("Peach.png");
     *
     */
    @NotNull
    public static InputStream load(@NotNull String path) {
        var stream = ResourceManager.class.getResourceAsStream("/" + path);
        if (stream != null)
            return stream;

        try {
            return new FileInputStream(new File("src/main/resources/" + path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);

            // [[noreturn]] doesn't exist in Java, so have a nop return:
            return new ByteArrayInputStream(new byte[0]);
        }
    }

}
