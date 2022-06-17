package dev.teamhelios.stonepowered.commands;


import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.command.sender.StoneSender;
import dev.teamhelios.stonepowered.pebble.PebbleManager;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;

@Command("test")
public class TestCommand extends BaseCommand {

    private StonePowered stonePowered;

    public TestCommand(StonePowered stonePowered) {
        this.stonePowered = stonePowered;
    }

    @Default
    public void handleDefault(StoneSender sender){
        this.stonePowered.getSoil().getDirtLoader().getLoader(PebbleManager.class).test();
    }
}
