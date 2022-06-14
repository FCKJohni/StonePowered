package dev.teamhelios.stonepowered.command;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class StoneCommand {

    private final String command;
    private final List<String> aliases;
    private final Class<?> executeClass;
    private final Method executeMethod;

    public StoneCommand(String command, List<String> aliases, Class<?> executeClass, Method executeMethod) {
        this.command = command;
        this.aliases = aliases;
        this.executeClass = executeClass;
        this.executeMethod = executeMethod;
    }

    public void execute(String message, Object[] args) {
        try {
            System.out.println(executeClass.getName());
            System.out.println(executeMethod.getName());
            System.out.println(Arrays.toString(args));
            if (args.length == 0) {
                executeMethod.invoke(executeClass, message);
            } else {
                executeMethod.invoke(executeClass, message, args);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isCommand(String message) {
        return message.equals(command) || aliases.contains(message);
    }

    public String getCommand() {
        return command;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public Method getExecuteMethod() {
        return executeMethod;
    }
}
