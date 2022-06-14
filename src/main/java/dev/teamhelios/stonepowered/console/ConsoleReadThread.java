package dev.teamhelios.stonepowered.console;

import dev.teamhelios.stonepowered.console.utils.Task;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;

/**
 * @author CloudNet
 */
public class ConsoleReadThread extends Thread {

    private final ConsoleHandler consoleHandler;
    private Task<String> currentTask;

    public ConsoleReadThread(ConsoleHandler consoleHandler) {
        this.consoleHandler = consoleHandler;
    }

    @Override
    public void run() {
        String line;
        while (!Thread.interrupted() && (line = this.readLine()) != null) {
            if (this.currentTask != null) {
                this.currentTask.complete(line);
                this.currentTask = null;
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

    protected Task<String> currentTask() {
        if (this.currentTask == null) {
            this.currentTask = new Task<>();
        }

        return this.currentTask;
    }
}
