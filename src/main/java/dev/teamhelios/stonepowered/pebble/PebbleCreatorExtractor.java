package dev.teamhelios.stonepowered.pebble;

import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.utils.HeliosLogger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PebbleCreatorExtractor {

    public void extract() {
        HeliosLogger.info("Extracting PebbleCreator...");
        Path rootPath = StonePowered.getRootDirectory();
        String jar = "PebbleConfigCreator.jar.temp";
        String sh = "Creator.sh";
        Path jarPath = rootPath.resolve(jar.replace(".temp", ""));
        Path shPath = rootPath.resolve(sh);
        HeliosLogger.info("Extracting PebbleCreator");
        extractFile(jarPath, jar);
        extractFile(shPath, sh);
    }

    private void extractFile(Path path, String name) {
        try (InputStream inputStream = PebbleCreatorExtractor.class.getResourceAsStream("/" + name)) {
            if (inputStream == null) {
                throw new RuntimeException("Could not find " + name);
            }
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            HeliosLogger.success("Extracted " + name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
