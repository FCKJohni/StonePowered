package dev.teamhelios.stonepowered.command;

import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.loader.utils.ILoader;
import dev.teamhelios.stonepowered.loader.utils.LoaderStatus;
import dev.teamhelios.stonepowered.loader.utils.Runnable;
import dev.teamhelios.stonepowered.utils.HeliosLogger;

public class CommandLoader extends ILoader {

    private StonePowered stonePowered;
    private CommandProcessor commandProcessor;
    private CommandRegistry commandRegistry;
    private Runnable<LoaderStatus> callback;

    @Override
    public void setup(Runnable<LoaderStatus> callback, StonePowered stonePowered) {
        this.stonePowered = stonePowered;
        this.callback = callback;
    }

    @Override
    public void initLoader() {
        notifyUpdate(LoaderStatus.CONSTRUCTING, callback);
        this.commandRegistry = new CommandRegistry(stonePowered);
        this.commandProcessor = new CommandProcessor(stonePowered, commandRegistry);
        this.commandProcessor.initProcessor();
        HeliosLogger.success("Found " + commandRegistry.getSize() + " command" + (commandRegistry.getSize() == 1 ? "" : "s!") + "!");
    }

    @Override
    public void shutdown() {
        notifyUpdate(LoaderStatus.UNLOADED, callback);
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }
}
