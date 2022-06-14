package dev.teamhelios.stonepowered.pebble;

import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.loader.utils.ILoader;
import dev.teamhelios.stonepowered.loader.utils.LoaderStatus;
import dev.teamhelios.stonepowered.loader.utils.Runnable;
import dev.teamhelios.stonepowered.utils.HeliosLogger;

import java.util.ArrayList;
import java.util.List;

public class PebbleManager extends ILoader {

    private List<Pebble> pebbles = new ArrayList<>();
    private StonePowered stonePowered;
    private Runnable<LoaderStatus> callback;

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
            pebble.startPebble();
        }
    }
}
