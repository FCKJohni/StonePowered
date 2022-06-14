package dev.teamhelios.stonepowered;


import dev.teamhelios.stonepowered.console.ConsoleHandler;
import dev.teamhelios.stonepowered.pebble.PebbleCreatorExtractor;
import dev.teamhelios.stonepowered.utils.HeliosLogger;
import io.javalin.core.util.JavalinLogger;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StonePowered {

    private final Soil soil;
    public static ExecutorService executorService = Executors.newCachedThreadPool();
    private ConsoleHandler consoleHandler;


    public static void main(String[] args) {
        new StonePowered();
    }

    public StonePowered() {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$-7s] %5$s %n");
        JavalinLogger.startupInfo = false;
        try {
            consoleHandler = new ConsoleHandler(this);
            consoleHandler.handleConsole();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        HeliosLogger.info("StonePowered is starting...");
        HeliosLogger.info("testing Filesystem...");
        getRootDirectory();
        new PebbleCreatorExtractor().extract();
        HeliosLogger.info("Creating Soil...");
        soil = new Soil(this);
        soil.initLoader();
        HeliosLogger.info("StonePowered is ready! Preparing Shutdown hook");
        this.consoleHandler.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            HeliosLogger.info("StonePowered is shutting down...");

            soil.getDirtLoader().shutdown();
            executorService.shutdown();
            consoleHandler.stop();
        }));
        HeliosLogger.success("StonePowered has been loaded!");
    }

    public static Path getRootDirectory() {
        File directory = null;

        try {
            directory = new File(StonePowered.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
        } catch (URISyntaxException e) {
            HeliosLogger.error("Could not get root directory");
            System.exit(0);
        }
        return directory.toPath();
    }

    public Soil getSoil() {
        return soil;
    }

}
