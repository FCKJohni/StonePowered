package dev.teamhelios.stonepowered.utils;

import dev.teamhelios.stonepowered.console.ConsoleHandler;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class HeliosLogger {


    private HeliosLogger() {

    }

    public static void info(String var1) {
        ConsoleHandler.instance.forceWriteLine(format("INFO", "&8" + var1 + "&r"));
    }

    public static void warn(String var1) {
        ConsoleHandler.instance.forceWriteLine(format("WARNING", "&6" + var1 + "&r"));
    }

    public static void error(String var1) {
        ConsoleHandler.instance.forceWriteLine(format("SEVERE", "&4" + var1 + "&r"));
    }

    public static void debug(String var1) {
        ConsoleHandler.instance.forceWriteLine(format("DEBUG", var1));
    }

    public static void success(String var1) {
        ConsoleHandler.instance.forceWriteLine(format("FINEST", "&a" + var1 + "&r"));
    }

    private static String format(String level, String message) {
        SimpleFormatter simpleFormatter = new SimpleFormatter();
        return simpleFormatter.format(new LogRecord(Level.parse(level.toUpperCase()), message));
    }

    public static String f(String var1, Object... var2) {
        return String.format(var1, var2);
    }

}
