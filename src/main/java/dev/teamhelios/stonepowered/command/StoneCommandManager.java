/**
 * MIT License
 * <p>
 * Copyright (c) 2019-2021 Matt
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.teamhelios.stonepowered.command;

import dev.teamhelios.stonepowered.command.sender.StoneSender;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.CommandManager;
import dev.triumphteam.cmd.core.execution.AsyncExecutionProvider;
import dev.triumphteam.cmd.core.execution.ExecutionProvider;
import dev.triumphteam.cmd.core.execution.SyncExecutionProvider;
import dev.triumphteam.cmd.core.registry.RegistryContainer;
import dev.triumphteam.cmd.core.sender.SenderMapper;
import dev.triumphteam.cmd.core.sender.SenderValidator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StoneCommandManager<S> extends CommandManager<StoneSender, S> {

    private final Map<String, StoneCommand<S>> commands = new HashMap<>();

    private final RegistryContainer<S> registryContainer = new RegistryContainer<>();

    private final ExecutionProvider syncExecutionProvider = new SyncExecutionProvider();
    private final ExecutionProvider asyncExecutionProvider = new AsyncExecutionProvider();

    private StoneCommandManager(@NotNull final SenderMapper<StoneSender, S> senderMapper, @NotNull final SenderValidator<S> senderValidator) {
        super(senderMapper, senderValidator);
    }

    @NotNull
    @Contract(" -> new")
    public static StoneCommandManager<StoneSender> create() {
        final StoneCommandManager<StoneSender> commandManager = new StoneCommandManager<>(SenderMapper.defaultMapper(), new StoneSenderValidator());
        setUpDefaults(commandManager);
        return commandManager;
    }

    @NotNull
    @Contract("_, _ -> new")
    public static <S> StoneCommandManager<S> create(@NotNull final SenderMapper<StoneSender, S> senderMapper, @NotNull final SenderValidator<S> senderValidator) {
        return new StoneCommandManager<>(senderMapper, senderValidator);
    }

    @Override
    public void registerCommand(@NotNull final BaseCommand baseCommand) {
        final StoneCommandProcessor<S> processor = new StoneCommandProcessor<>(baseCommand, getRegistryContainer(), getSenderMapper(), getSenderValidator(), syncExecutionProvider, asyncExecutionProvider);

        final String name = processor.getName();

        final StoneCommand<S> command = commands.computeIfAbsent(name, ignored -> new StoneCommand<>(processor, syncExecutionProvider, asyncExecutionProvider));

        command.addSubCommands(baseCommand);
    }

    @Override
    @NotNull
    protected RegistryContainer<S> getRegistryContainer() {
        return registryContainer;
    }


    @Override
    public void unregisterCommand(@NotNull final BaseCommand command) {
        // TODO: add a remove functionality
    }

    public StoneCommandResult dispatch(String line) {
        if (line.isEmpty()) return new StoneCommandResult(false, "Line is empty", false);
        final String[] args = line.split(" ");
        if (args.length == 0) return new StoneCommandResult(false, "Line is empty", false);
        final String commandName = args[0];

        final StoneCommand<S> command = commands.get(commandName);
        if (command == null) {
            return new StoneCommandResult(false, "Command not found", false);
        }

        command.execute(new StoneCommandSender(), Arrays.copyOfRange(args, 1, args.length));
        return new StoneCommandResult(true, "Command executed", true);
    }

    private static void setUpDefaults(@NotNull final StoneCommandManager<StoneSender> manager) {
        //TODO: https://github.com/TriumphTeam/triumph-cmds/blob/a020e9d1774bb9a98dc1f3990af52fe86fa2fa8a/cli/src/main/java/dev/triumphteam/cmds/cli/CliCommandManager.java#L121
    }

}
