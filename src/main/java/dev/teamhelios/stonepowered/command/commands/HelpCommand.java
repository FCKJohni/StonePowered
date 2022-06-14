package dev.teamhelios.stonepowered.command.commands;

import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.command.annotations.ICommand;
import dev.teamhelios.stonepowered.command.annotations.IRunner;

import java.util.List;

@ICommand(value = "help")
public class HelpCommand {

    private StonePowered stonePowered;

    public HelpCommand(StonePowered stonePowered) {
        this.stonePowered = stonePowered;
    }

    @IRunner
    public void handle(String message, int test) {

    }

}
