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
import dev.triumphteam.cmd.core.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.execution.ExecutionProvider;
import dev.triumphteam.cmd.core.message.MessageKey;
import dev.triumphteam.cmd.core.message.MessageRegistry;
import dev.triumphteam.cmd.core.message.context.DefaultMessageContext;
import dev.triumphteam.cmd.core.registry.RegistryContainer;
import dev.triumphteam.cmd.core.sender.SenderMapper;
import dev.triumphteam.cmd.core.sender.SenderValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StoneCommand<S> implements Command<S, StoneSubCommand<S>> {

    private final String name;

    private final RegistryContainer<S> registries;
    private final @NotNull MessageRegistry<S> messageRegistry;

    private final @NotNull SenderMapper<StoneSender, S> senderMapper;
    private final @NotNull SenderValidator<S> senderValidator;

    private final ExecutionProvider syncExecutionProvider;
    private final ExecutionProvider asyncExecutionProvider;

    private final Map<String, StoneSubCommand<S>> subCommands = new HashMap<>();
    private final Map<String, StoneSubCommand<S>> subCommandAliases = new HashMap<>();

    public StoneCommand(@NotNull final StoneCommandProcessor<S> processor, @NotNull final ExecutionProvider syncExecutionProvider, @NotNull final ExecutionProvider asyncExecutionProvider) {
        this.name = processor.getName();

        this.senderMapper = processor.getSenderMapper();
        this.senderValidator = processor.getSenderValidator();
        this.registries = processor.getRegistryContainer();
        this.messageRegistry = registries.getMessageRegistry();
        this.syncExecutionProvider = syncExecutionProvider;
        this.asyncExecutionProvider = asyncExecutionProvider;
    }

    public void addSubCommands(@NotNull final BaseCommand baseCommand) {
        for (final Method method : baseCommand.getClass().getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) continue;

            final StoneSubCommandProcessor<S> processor = new StoneSubCommandProcessor<>(baseCommand, name, method, registries, senderValidator);

            final String subCommandName = processor.getName();
            if (subCommandName == null) continue;

            final ExecutionProvider executionProvider = processor.isAsync() ? asyncExecutionProvider : syncExecutionProvider;
            final StoneSubCommand<S> subCommand = subCommands.computeIfAbsent(subCommandName, it -> new StoneSubCommand<>(processor, name, executionProvider));
            processor.getAlias().forEach(alias -> subCommandAliases.putIfAbsent(alias, subCommand));
        }
    }

    // TODO: Comments
    public void execute(@NotNull final StoneSender sender, @NotNull final String[] args) {
        StoneSubCommand<S> subCommand = getDefaultSubCommand();

        String subCommandName = "";
        if (args.length > 0) subCommandName = args[0].toLowerCase();
        if (subCommand == null || subCommandExists(subCommandName)) {
            subCommand = getSubCommand(subCommandName);
        }

        final S mappedSender = senderMapper.map(sender);

        if (subCommand == null) {
            messageRegistry.sendMessage(MessageKey.UNKNOWN_COMMAND, mappedSender, new DefaultMessageContext(name, subCommandName));
            return;
        }

        final List<String> commandArgs = Arrays.asList(!subCommand.isDefault() ? Arrays.copyOfRange(args, 1, args.length) : args);
        subCommand.execute(mappedSender, commandArgs);
    }

    /**
     * Gets a default command if present.
     *
     * @return A default SubCommand.
     */
    @Nullable
    private StoneSubCommand<S> getDefaultSubCommand() {
        return subCommands.get(Default.DEFAULT_CMD_NAME);
    }

    @Nullable
    private StoneSubCommand<S> getSubCommand(@NotNull final String key) {
        final StoneSubCommand<S> subCommand = subCommands.get(key);
        if (subCommand != null) return subCommand;
        return subCommandAliases.get(key);
    }

    private boolean subCommandExists(@NotNull final String key) {
        return subCommands.containsKey(key) || subCommandAliases.containsKey(key);
    }

    @Override
    public void addSubCommands(@NotNull Map<String, StoneSubCommand<S>> subCommands, @NotNull Map<String, StoneSubCommand<S>> subCommandAliases) {
        this.subCommands.putAll(subCommands);
        this.subCommandAliases.putAll(subCommandAliases);
    }
}

