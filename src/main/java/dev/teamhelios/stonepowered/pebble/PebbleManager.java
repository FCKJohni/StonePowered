package dev.teamhelios.stonepowered.pebble;

import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.command.StoneCommandResult;
import dev.teamhelios.stonepowered.loader.utils.ILoader;
import dev.teamhelios.stonepowered.loader.utils.LoaderStatus;
import dev.teamhelios.stonepowered.loader.utils.Runnable;
import dev.teamhelios.stonepowered.utils.HeliosLogger;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PebbleManager extends ILoader {

    private List<Pebble> pebbles = new ArrayList<>();
    private StonePowered stonePowered;
    private Runnable<LoaderStatus> callback;

    private List<UUID> currentPebbleScreens = new ArrayList<>();

    @Override
    public void setup(Runnable<LoaderStatus> callback, StonePowered stonePowered) {
        this.callback = callback;
        this.stonePowered = stonePowered;
    }

    @Override
    public void initLoader() {
        callback.notifyStatus(LoaderStatus.CONSTRUCTING);
        PebbleConfigFinder pebbleConfigFinder = new PebbleConfigFinder();
        this.pebbles = pebbleConfigFinder.findPebbleConfigs();
    }

    @Override
    public void shutdown() {
        HeliosLogger.info("Stopping all Pebbles...");
        for (Pebble pebble : pebbles) {
            pebble.stopPebble();
        }
        callback.notifyStatus(LoaderStatus.UNLOADED);
    }

    public void start() {
        callback.notifyStatus(LoaderStatus.LOADED);
        for (Pebble pebble : pebbles) {
            if (pebble.shouldAutoRun()) {
                pebble.startPebble();
            }
        }
    }

    public void startScreen(UUID screen) {
        if (!currentPebbleScreens.contains(screen)) {
            currentPebbleScreens.add(screen);
        }
    }

    public List<UUID> getCurrentPebbleScreens() {
        return currentPebbleScreens;
    }

    public List<Pair<String, UUID>> getPebbles() {
        List<Pair<String, UUID>> pebbles = new ArrayList<>();
        for (Pebble pebble : this.pebbles) {
            pebbles.add(Pair.of(pebble.getName(), pebble.getUuid()));
        }
        return pebbles;
    }

    public StoneCommandResult startPebble(String pebbleIdentifier) {
        for (Pebble pebble : pebbles) {
            if (pebble.getName().equals(pebbleIdentifier) || pebble.getUuid().toString().equals(pebbleIdentifier)) {
                pebble.startPebble();
                return new StoneCommandResult(true, "Pebble started successfully!", false);
            }
        }
        return new StoneCommandResult(false, "Pebble not found!", false);
    }

    public StoneCommandResult stopPebble(String pebbleIdentifier) {
        for (Pebble pebble : pebbles) {
            if (pebble.getName().equals(pebbleIdentifier) || pebble.getUuid().toString().equals(pebbleIdentifier)) {
                pebble.stopPebble();
                return new StoneCommandResult(true, "Pebble stopped successfully!", false);
            }
        }
        return new StoneCommandResult(false, "Pebble not found!", false);
    }

    @Nullable
    public Pebble retrievePebbleByName(String target) {
        for (Pebble pebble : pebbles) {
            if (pebble.getName().equals(target)) {
                return pebble;
            }
        }
        return null;
    }
}
