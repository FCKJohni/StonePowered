package dev.teamhelios.stonepowered.command.commands;

import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.command.annotations.ICommand;
import dev.teamhelios.stonepowered.command.annotations.IRunner;

@ICommand(value = "stop")
public class StopCommand {

    private StonePowered stonePowered;

    public StopCommand(StonePowered stonePowered) {
        this.stonePowered = stonePowered;
    }

    @IRunner
    public void handle(String message) {
        System.exit(0);
    }

}
