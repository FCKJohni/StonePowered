package dev.teamhelios.stonepowered.loader.utils;


import java.util.logging.Level;

public enum LoaderStatus {

    PREFLIGHT(Level.INFO),
    CONSTRUCTING(Level.WARNING),
    LOADED(Level.INFO),
    UNLOADED(Level.WARNING),
    ERROR(Level.SEVERE);

    final Level level;

    LoaderStatus(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }
}
