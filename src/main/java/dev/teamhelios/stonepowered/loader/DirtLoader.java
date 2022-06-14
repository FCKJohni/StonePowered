package dev.teamhelios.stonepowered.loader;

import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.command.CommandLoader;
import dev.teamhelios.stonepowered.loader.utils.ILoader;
import dev.teamhelios.stonepowered.loader.utils.LoaderStatus;
import dev.teamhelios.stonepowered.pebble.PebbleManager;
import dev.teamhelios.stonepowered.utils.HeliosLogger;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.logging.Level;

public class DirtLoader {

    private final StonePowered stonePowered;

    private static final LinkedList<Class<? extends ILoader>> loaderClasses = new LinkedList<>();
    private final LinkedList<ILoader> registeredLoaders = new LinkedList<>();


    static {
        loaderClasses.add(PebbleManager.class);
        loaderClasses.add(WebsocketLoader.class);
        loaderClasses.add(CommandLoader.class);
    }

    public DirtLoader(StonePowered stonePowered) {
        this.stonePowered = stonePowered;
    }

    public void initLoader() {
        for (Class<? extends ILoader> loader : loaderClasses) {
            try {
                ILoader loaderInstance = loader.getDeclaredConstructor().newInstance();
                loaderInstance.setup(r -> handleUpdate(loaderInstance, r), stonePowered);
                loaderInstance.initLoader();
                registeredLoaders.add(loaderInstance);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleUpdate(ILoader loader, LoaderStatus status) {
        if (status.getLevel().equals(Level.WARNING))
            HeliosLogger.warn(String.format("%s changed its Status to %s!", loader.getClass().getSimpleName(), status));
        if (status.getLevel().equals(Level.SEVERE))
            HeliosLogger.error(String.format("%s changed its Status to %s!", loader.getClass().getSimpleName(), status));
    }

    public void shutdown() {
        for (ILoader loader : registeredLoaders) {
            loader.shutdown();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getLoader(Class<T> loaderClass) {
        for (ILoader loader : registeredLoaders) {
            if (loader.getClass().equals(loaderClass)) {
                return (T) loader;
            }
        }
        return null;
    }
}
