package dev.teamhelios.stonepowered.pebble;

import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.utils.HeliosLogger;
import org.apache.commons.lang3.SystemUtils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class PebbleCreatorExtractor {

    public void extract() {
        HeliosLogger.info("Extracting PebbleCreator...");
        Path rootPath = StonePowered.getRootDirectory();
        String os = getFileToExtract();
        HeliosLogger.info("Extracting PebbleCreator for " + getOS());
        Path path = rootPath.resolve(os);
        if (!Files.exists(path)) {
            try (InputStream inputStream = PebbleCreatorExtractor.class.getResourceAsStream("/" + os)) {
                if (inputStream == null) {
                    throw new RuntimeException("Could not find " + os);
                }
                Files.copy(inputStream, path);
                HeliosLogger.success("Extracted PebbleCreator for " + getOS());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getOS() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return "Windows";
        } else if (SystemUtils.IS_OS_LINUX) {
            return "Linux";
        } else {
            throw new RuntimeException("Unsupported OS");
        }
    }

    public String getFileToExtract() {
        if (Objects.equals(getOS(), "Windows")) {
            return "createPebble.bat";
        } else if (Objects.equals(getOS(), "Linux")) {
            return "createPebble.sh";
        } else {
            throw new RuntimeException("Unsupported OS");
        }
    }
}
