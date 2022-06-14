package dev.teamhelios.stonepowered.loader.utils;

import dev.teamhelios.stonepowered.StonePowered;

public abstract class ILoader {

    public LoaderStatus currentStatus = LoaderStatus.CONSTRUCTING;

    public abstract void setup(Runnable<LoaderStatus> callback, StonePowered stonePowered);

    public abstract void initLoader();

    public abstract void shutdown();

    public void notifyUpdate(LoaderStatus status, Runnable<LoaderStatus> callback) {
        currentStatus = status;
        callback.notifyStatus(status);
    }

}
