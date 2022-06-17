package dev.teamhelios.stonepowered.commands;

import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.command.StoneCommandResult;
import dev.teamhelios.stonepowered.command.sender.StoneSender;
import dev.teamhelios.stonepowered.pebble.PebbleManager;
import dev.teamhelios.stonepowered.utils.HeliosLogger;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.UUID;

@Command("pebble")
public class PebbleCommand extends BaseCommand {

    private StonePowered stonePowered;

    public PebbleCommand(StonePowered stonePowered) {
        this.stonePowered = stonePowered;
    }

    @Default
    public void handleDefault(StoneSender sender) {

    }

    @SubCommand("--list")
    public void handleList(StoneSender sender) {
        List<Pair<String, UUID>> pebbles = stonePowered.getSoil().getDirtLoader().getLoader(PebbleManager.class).getPebbles();
        for (Pair<String, UUID> pebble : pebbles) {
            HeliosLogger.info("&b" + pebble.getLeft() + " &7- &6" + pebble.getRight());
        }
    }

    @SubCommand("start")
    public void handleStart(StoneSender sender, String pebbleIdentifier) {
        StoneCommandResult result = stonePowered.getSoil().getDirtLoader().getLoader(PebbleManager.class).startPebble(pebbleIdentifier);
        if (result.success()) {
            HeliosLogger.info("&aSuccessfully started Pebble &6" + pebbleIdentifier);
        } else {
            HeliosLogger.info("&cFailed to start Pebble &6" + pebbleIdentifier + " &c- &7" + result.message());
        }
    }

    @SubCommand("stop")
    public void handleStop(StoneSender sender, String pebbleIdentifier) {
        StoneCommandResult result = stonePowered.getSoil().getDirtLoader().getLoader(PebbleManager.class).stopPebble(pebbleIdentifier);
        if (result.success()) {
            HeliosLogger.info("&cSuccessfully stopped Pebble &6" + pebbleIdentifier);
        } else {
            HeliosLogger.info("&cFailed to stop Pebble &6" + pebbleIdentifier + " &c- &7" + result.message());
        }
    }
}
