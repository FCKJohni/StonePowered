package dev.teamhelios.stonepowered.loader.utils;

import org.apache.logging.log4j.Level;

public enum LoaderStatus {

    CONSTRUCTING(Level.WARN),
    LOADED(Level.getLevel("SUCCESS")),
    UNLOADED(Level.WARN),
    ERROR(Level.ERROR);

    final Level level;

    LoaderStatus(Level level){
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }
}
