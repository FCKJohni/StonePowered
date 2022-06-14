package dev.teamhelios.stonepowered.command;

import dev.teamhelios.stonepowered.StonePowered;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandRegistry {

    private StonePowered stonePowered;
    private final List<StoneCommand> commands = new ArrayList<>();

    public CommandRegistry(StonePowered stonePowered) {
        this.stonePowered = stonePowered;
    }

    public void register(StoneCommand command) {
        commands.add(command);
    }

    private boolean isExecutable(String command, Object[] args) {
        for (StoneCommand stoneCommand : commands) {
            if (stoneCommand.isCommand(command)) {
                if (args.length != 0 && stoneCommand.getExecuteMethod().getParameterCount() != 1) {
                    for (int i = 0; i < args.length; i++) {
                        if (!stoneCommand.getExecuteMethod().getParameterTypes()[i + 1].isAssignableFrom(args[i].getClass())) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void dispatch(String command, Object[] args) {
        for (StoneCommand stoneCommand : commands) {
            if (stoneCommand.isCommand(command)) {
                stoneCommand.execute(command, args);
            }
        }
    }

    public CommandResult execute(String command, Object[] args) {
        if (isExecutable(command, args)) {
            dispatch(command, args);
            return new CommandResult(true, "Command executed successfully!");
        } else {
            return new CommandResult(false, "Command has invalid Args!");
        }
    }

    public int getSize() {
        return commands.size();
    }
}
