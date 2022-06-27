package dev.teamhelios.stonepowered.console;

import dev.teamhelios.stonepowered.command.StoneCommandResult;
import dev.teamhelios.stonepowered.utils.HeliosLogger;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;

/**
 * @author CloudNet
 */
public class ConsoleReadThread extends Thread {

    private final ConsoleHandler consoleHandler;

    public ConsoleReadThread(ConsoleHandler consoleHandler) {
        this.consoleHandler = consoleHandler;
    }

    @Override
    public void run() {
        String line;
        while (!Thread.interrupted() && (line = this.readLine()) != null) {
            StoneCommandResult commandResult = consoleHandler.getStonePowered().getSoil().getCommandManager().dispatch(line);
            if (!commandResult.success()) {
                HeliosLogger.error(commandResult.message());
            } else {
                if (commandResult.silent()) continue;
                HeliosLogger.success(commandResult.message());
            }
        }
    }

    private String readLine() {
        try {
            return this.consoleHandler.lineReader().readLine(this.consoleHandler.prompt());
        } catch (EndOfFileException ignored) {
        } catch (UserInterruptException exception) {
            System.exit(-1);
        }

        return null;
    }


    public void end() {
        this.interrupt();
    }
}
