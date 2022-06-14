package dev.teamhelios.stonepowered.command;

import dev.teamhelios.stonepowered.StonePowered;
import dev.teamhelios.stonepowered.command.annotations.ICommand;
import dev.teamhelios.stonepowered.command.annotations.IRunner;
import dev.teamhelios.stonepowered.utils.HeliosLogger;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class CommandProcessor {

    private StonePowered stonePowered;
    private final CommandRegistry commandRegistry;

    public CommandProcessor(StonePowered stonePowered, CommandRegistry commandRegistry) {
        this.stonePowered = stonePowered;
        this.commandRegistry = commandRegistry;
    }

    public void initProcessor() {
        Reflections reflections = new Reflections("dev.teamhelios.stonepowered.command.commands");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(ICommand.class);

        for (Class<?> clazz : annotated) {
            try {
                ICommand command = clazz.getAnnotation(ICommand.class);
                Method executeMethod = findAnnotatedMethod(clazz);
                Object commandInstance = clazz.getDeclaredConstructor(StonePowered.class).newInstance(stonePowered);
                if (executeMethod == null) {
                    HeliosLogger.error("Command &b" + command.value() + "&r has no execute method!");
                    continue;
                }
                StoneCommand stoneCommand = new StoneCommand(command.value(), Arrays.asList(command.alias()), commandInstance.getClass(), executeMethod);
                commandRegistry.register(stoneCommand);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                HeliosLogger.error("Failed to register command &b" + clazz.getSimpleName() + "&r!");
            }
        }
    }

    private Method findAnnotatedMethod(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(IRunner.class)) {
                return method;
            }
        }
        return null;
    }

}
