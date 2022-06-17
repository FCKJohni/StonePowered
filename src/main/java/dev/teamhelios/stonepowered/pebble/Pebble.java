package dev.teamhelios.stonepowered.pebble;

import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.utils.HeliosLogger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static dev.teamhelios.stonepowered.utils.HeliosLogger.f;

public class Pebble {


    private final String name;
    private final List<String> cmd;
    private final UUID uuid;
    private final boolean autoRun;
    private File workingDir;
    private Process process;
    private File currentLog;

    public Pebble(String name, List<String> cmd, UUID uuid, boolean autoRun) {
        this.name = name;
        this.cmd = cmd;
        this.uuid = uuid;
        this.autoRun = autoRun;
    }


    public void startPebble() {
        checkLogFile();
        HeliosLogger.info(f("Starting Pebble: &b%s&r", name));
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(cmd);
        processBuilder.directory(workingDir);
        try {
            process = processBuilder.start();
            process.onExit().thenAcceptAsync(p -> HeliosLogger.error(f("Pebble %s exited with code %d", name, p.exitValue())));
            handleInputLogging(process);
            handleErrorLogging(process);
        } catch (IOException e) {
            HeliosLogger.error(f("An error occurred while starting the Pebble [%s]!", uuid.toString()));
            e.printStackTrace();
        }
    }

    public void handleInputLogging(Process process) {
        StonePowered.executorService.execute(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentLog, true))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        HeliosLogger.info(f("Pebble &b%s&r: %s", name, line));
                        writer.write(f("[%s] %s\n", name, line));
                        writer.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void handleErrorLogging(Process process) {
        StonePowered.executorService.execute(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentLog, true))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        HeliosLogger.error(f("Pebble &b%s&r: %s", name, line));
                        writer.write(f("[%s] %s\n", name, line));
                        writer.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void checkLogFile() {
        currentLog = workingDir.toPath().resolve(".pebble/pebble.log").toFile();
        if (!workingDir.toPath().resolve(".pebble/").toFile().exists()) {
            workingDir.toPath().resolve(".pebble/").toFile().mkdirs();
        }
        if (!currentLog.exists()) {
            createLog();
        } else {
            compressLog();
        }
    }

    public void compressLog() {
        try {
            currentLog = workingDir.toPath().resolve(".pebble/pebble.log").toFile();
            final Path logFile = currentLog.toPath();
            int index = 1;
            while (Files.exists(workingDir.toPath().resolve(".pebble/pebble-" + index + ".log.zip"))) {
                index++;
            }
            final Path zipFile = workingDir.toPath().resolve(".pebble/pebble-" + index + ".log.zip");
            try (
                    final var in = Files.newInputStream(logFile);
                    final var out = Files.newOutputStream(zipFile);
                    final var zipOut = new ZipOutputStream(out)
            ) {
                zipOut.putNextEntry(new ZipEntry(logFile.getFileName().toString()));
                in.transferTo(zipOut);
                zipOut.closeEntry();
            }
            Files.delete(logFile);
            createLog();
        } catch (Exception e) {
            HeliosLogger.error("Could not compress pebble.log for Pebble: &b" + name + "&r");
            e.printStackTrace();
        }
    }

    public void createLog() {
        currentLog = workingDir.toPath().resolve(".pebble/pebble.log").toFile();
        try {
            currentLog.createNewFile();
        } catch (IOException e) {
            HeliosLogger.error("Could not create pebble.log for Pebble: &b" + name + "&r");
        }
    }

    public void stopPebble() {
        if (process != null && process.isAlive()) {
            process.destroy();
            HeliosLogger.error(f("Pebble [%s] has been stopped!", uuid.toString()));
        }
    }

    public String getName() {
        return name;
    }

    public List<String> getCmd() {
        return cmd;
    }


    public UUID getUuid() {
        return uuid;
    }

    public void setWorkingDir(File workingDir) {
        this.workingDir = workingDir;
    }

    public boolean shouldAutoRun() {
        return autoRun;
    }
}
