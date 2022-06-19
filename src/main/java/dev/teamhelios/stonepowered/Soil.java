package dev.teamhelios.stonepowered;

import dev.teamhelios.stonepowered.clientHandling.ClientHandler;
import dev.teamhelios.stonepowered.command.StoneCommandManager;
import dev.teamhelios.stonepowered.command.sender.StoneSender;
import dev.teamhelios.stonepowered.commands.PebbleCommand;
import dev.teamhelios.stonepowered.commands.StopCommand;
import dev.teamhelios.stonepowered.loader.DirtLoader;
import dev.teamhelios.stonepowered.pebble.PebbleManager;

public class Soil {

    private final StonePowered stonePowered;
    private DirtLoader dirtLoader;
    private ClientHandler clientHandler;
    private final StoneCommandManager<StoneSender> commandManager = StoneCommandManager.create();

    public Soil(StonePowered stonePowered) {
        this.stonePowered = stonePowered;
    }

    public void initLoader() {
        commandManager.registerCommand(new StopCommand());
        commandManager.registerCommand(new PebbleCommand(stonePowered));
        dirtLoader = new DirtLoader(stonePowered);
        dirtLoader.initLoader();
        this.clientHandler = new ClientHandler(stonePowered);

        this.dirtLoader.getLoader(PebbleManager.class).start();
    }

    public StonePowered getStonePowered() {
        return stonePowered;
    }

    public DirtLoader getDirtLoader() {
        return dirtLoader;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public StoneCommandManager<StoneSender> getCommandManager() {
        return commandManager;
    }
}
