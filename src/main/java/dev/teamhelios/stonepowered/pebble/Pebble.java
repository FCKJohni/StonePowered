package dev.teamhelios.stonepowered.pebble;

import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.utils.HeliosLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

import static dev.teamhelios.stonepowered.utils.HeliosLogger.f;

public class Pebble {


    private final String name;
    private final List<String> cmd;
    private final UUID uuid;

    private File workingDir;

    private Process process;

    public Pebble(String name, List<String> cmd, UUID uuid) {
        this.name = name;
        this.cmd = cmd;
        this.uuid = uuid;
    }


    public void startPebble() {
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
                String line;
                while ((line = reader.readLine()) != null) {
                    HeliosLogger.info(f("Pebble &b%s&r: %s", name, line));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void handleErrorLogging(Process process) {
        StonePowered.executorService.execute(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    HeliosLogger.error(f("Pebble &b%s&r: %s", name, line));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void stopPebble() {
        if (process.isAlive()) {
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
}
