package dev.teamhelios.stonepowered.commands;

import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.command.sender.StoneSender;
import dev.teamhelios.stonepowered.utils.HeliosLogger;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;

@Command("stop")
public class StopCommand extends BaseCommand {

    private boolean wasWarned = false;

    @Default
    public void handleCommand(StoneSender sender) {
        if (wasWarned) {
            HeliosLogger.success("&4Stopping all Pebbles and shutting down &6:)");
            System.exit(0);
        } else {
            HeliosLogger.info("&4Are you sure you want to shutdown StonePowered? &4&lexecute the command again to confirm.");
            wasWarned = true;
            startTimer();
        }
    }

    private void startTimer() {
        StonePowered.executorService.execute(() -> {
            try {
                Thread.sleep(10000);
                wasWarned = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
