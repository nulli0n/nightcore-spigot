package su.nightexpress.nightcore.commands.command;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.commands.CommandRequirement;
import su.nightexpress.nightcore.commands.NodeUtils;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.context.Suggestions;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.commands.tree.CommandNode;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.CommandUtil;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.util.Collections;
import java.util.List;

public abstract class AbstractCommand<N extends ExecutableNode> extends Command implements NightCommand {

    private final NightPlugin plugin;
    private final N root;

    public AbstractCommand(@NotNull NightPlugin plugin, @NotNull N root, @NotNull List<String> aliases) {
        super(clean(root.getName()), clean(root.getDescription()), clean(root.getUsage()), aliases);
        this.plugin = plugin;
        this.root = root;
        this.setPermission(root.getPermission());
    }

    @NotNull
    private static String clean(@NotNull String string) {
        return NightMessage.stripTags(string);
    }

    @Override
    @NotNull
    public NightPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    @NotNull
    public N getRoot() {
        return this.root;
    }

    @Override
    public boolean register() {
        return CommandUtil.register(this, this.plugin.getName());
    }

    @Override
    public boolean unregister() {
        return CommandUtil.unregister(this);
    }

    @Override
    public boolean isRegistered() {
        return super.isRegistered();
    }

    @Override
    @NotNull
    public String getName() {
        return super.getName();
    }

    @Override
    @Nullable
    public String getPermission() {
        return super.getPermission();
    }

    @Override
    @NotNull
    public String getLabel() {
        return super.getLabel();
    }

    @Override
    @NotNull
    public List<String> getAliases() {
        return super.getAliases();
    }

    @Override
    @NotNull
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    @NotNull
    public String getUsage() {
        return super.getUsage();
    }

    @Nullable
    private CommandContext parseNodes(@NotNull CommandNode node, @NotNull ArgumentReader reader, @NotNull CommandContextBuilder builder, boolean forExecution) {
        CommandSender sender = builder.getSender();

        try {
            node.parse(reader, builder);
        }
        catch (CommandSyntaxException exception) {
            if (forExecution) {
                exception.getMessageLocale().withPrefix(this.plugin.getPrefix()).send(sender, replacer -> replacer
                    .replace(Placeholders.GENERIC_NAME, node.getLocalizedName())
                    .replace(Placeholders.GENERIC_INPUT, reader.getCursorArgument())
                    .replace(Placeholders.GENERIC_VALUE, String.valueOf(exception.getValue()))
                );
                return null;
            }
        }

        reader.moveForward();

        if (reader.canMoveForward()) {
            for (CommandNode child : node.getRelevantNodes(reader)) {
                if (!child.hasPermission(sender)) {
                    if (forExecution) {
                        CoreLang.ERROR_NO_PERMISSION.withPrefix(this.plugin).send(sender);
                    }
                    return null;
                }

                List<CommandRequirement> requirements = child.getRequirements();
                for (CommandRequirement requirement : requirements) {
                    if (!requirement.test(sender)) {
                        if (forExecution) {
                            requirement.getMessage().withPrefix(this.plugin).send(sender);
                        }
                        return null;
                    }
                }

                return this.parseNodes(child, reader, builder, forExecution);
            }
        }
        else if (forExecution && node.hasRequiredArguments() && builder.getExecutor() != null) {
            ExecutableNode executable = builder.getExecutor();

            CoreLang.COMMAND_EXECUTION_MISSING_ARGUMENTS.withPrefix(this.plugin).send(sender, replacer -> replacer
                .replace(Placeholders.GENERIC_COMMAND, NodeUtils.formatLabel(executable, builder))
                .replace(Placeholders.GENERIC_DESCRIPTION, executable.getDescription()));
            return null;
        }

        return builder.build();
    }

    private void listSuggestions(@NotNull CommandNode node, @NotNull ArgumentReader reader, @NotNull CommandContext context, @NotNull Suggestions suggestions) {
        if (reader.isEnd()) return;

        reader.moveForward();
        node.suggests(reader, context, suggestions); // Actually modify the suggestions list.

        //System.out.println(node.getName() + ": Parse & Inject suggestions: " + suggestions.getSuggestions());

        // Continue until pass through all user's input.
        if (reader.canMoveForward()) {

            //System.out.println("cursor = '" + reader.getCursorArgument() + "' [" + reader.getCursor() + "]");

            for (CommandNode child : node.getRelevantNodes(reader)) {
                this.listSuggestions(child, reader, context, suggestions);
            }
        }
    }

    @Nullable
    private CommandContext parse(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args, boolean forExecution) {
        ArgumentReader reader = ArgumentReader.forArgumentsWithLabel(label, args);
        CommandContextBuilder builder = new CommandContextBuilder(this.plugin, sender, this.root, reader.getString());
        return this.parseNodes(this.root, reader, builder, forExecution);
    }
    
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        CommandContext context = this.parse(sender, label, args, true);
        if (context == null) return false;

        ExecutableNode executor = context.getExecutor();
        if (executor == null) return false;

        return executor.run(context);
    }

    @Override
    @NotNull
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) return Collections.emptyList();

        CommandContext context = this.parse(sender, label, args, false);
        if (context == null) return Collections.emptyList();

        ArgumentReader reader = ArgumentReader.forArgumentsWithLabel(label, args);
        Suggestions suggestions = new Suggestions();
        this.listSuggestions(this.root, reader, context, suggestions);

        return suggestions.getSuggestions();
    }
}
