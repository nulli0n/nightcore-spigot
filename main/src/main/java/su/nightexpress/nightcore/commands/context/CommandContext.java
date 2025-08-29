package su.nightexpress.nightcore.commands.context;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.commands.NodeUtils;
import su.nightexpress.nightcore.commands.tree.CommandNode;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.message.LangMessage;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.placeholder.Replacer;

import java.util.List;
import java.util.function.Consumer;

public class CommandContext {

    private final NightPlugin             plugin;
    private final CommandSender           sender;
    private final CommandNode             root;
    private final String                  input;
    private final ExecutableNode          executor;
    private final ParsedArguments         arguments;
    private final List<String> flags;
    private final List<ParsedCommandNode> nodes;

    private final Player player;

    public CommandContext(@NotNull NightPlugin plugin,
                          @NotNull CommandSender sender,
                          @NotNull CommandNode root,
                          @NotNull String input,
                          @NotNull ParsedArguments arguments,
                          @NotNull List<String> flags,
                          @Nullable ExecutableNode executor,
                          @NotNull List<ParsedCommandNode> nodes) {
        this.plugin = plugin;
        this.sender = sender;
        this.root = root;
        this.input = input;
        this.arguments = arguments;
        this.flags = flags;
        this.executor = executor;
        this.nodes = nodes;

        this.player = sender instanceof Player user ? user : null;
    }

    public void send(@NotNull MessageLocale locale) {
        this.send(locale, null);
    }

    public void send(@NotNull MessageLocale locale, @Nullable Consumer<Replacer> consumer) {
        this.send(locale.message(), consumer);
    }

    public void send(@NotNull LangMessage message) {
        this.send(message, null);
    }

    public void send(@NotNull LangMessage message, @Nullable Consumer<Replacer> consumer) {
        message.send(this.sender, consumer);
    }

    public void errorPermission() {
        this.send(CoreLang.ERROR_NO_PERMISSION.withPrefix(this.plugin));
    }

    public void errorBadPlayer() {
        this.send(CoreLang.ERROR_INVALID_PLAYER.withPrefix(this.plugin));
    }

    public void errorPlayerOnly() {
        this.send(CoreLang.COMMAND_EXECUTION_PLAYER_ONLY.withPrefix(this.plugin));
    }

    public void printUsage() {
        if (this.executor == null) return;

        CoreLang.COMMAND_EXECUTION_MISSING_ARGUMENTS.withPrefix(this.plugin).send(this.sender, replacer -> replacer
            .replace(Placeholders.GENERIC_COMMAND, NodeUtils.formatLabel(this.executor, this))
            .replace(Placeholders.GENERIC_DESCRIPTION, this.executor.getDescription()));
    }

    public boolean hasPermission(@NotNull Permission permission) {
        return this.sender.hasPermission(permission);
    }

    public boolean hasPermission(@NotNull String permission) {
        return this.sender.hasPermission(permission);
    }

    public boolean hasFlag(@NotNull String name) {
        return this.flags.contains(name);
    }

    @Nullable
    public Player getPlayer() {
        return this.player;
    }

    @NotNull
    public Player getPlayerOrThrow() {
        if (this.isPlayer()) return this.player;

        throw new IllegalStateException("CommandContext is not bound to a player!");
    }

    public boolean isPlayer() {
        return this.player != null;
    }

    public boolean hasNodes() {
        return !this.nodes.isEmpty();
    }

    @Nullable
    public ExecutableNode getExecutor() {
        return this.executor;
    }

    @NotNull
    public CommandSender getSender() {
        return this.sender;
    }

    @NotNull
    public String getInput() {
        return this.input;
    }

    @NotNull
    public CommandNode getRoot() {
        return this.root;
    }

    @NotNull
    public ParsedArguments getArguments() {
        return this.arguments;
    }

    @NotNull
    public List<String> getFlags() {
        return this.flags;
    }

    @NotNull
    public List<ParsedCommandNode> getNodes() {
        return this.nodes;
    }

    @NotNull
    public List<CommandNode> getNodesPriorTo(@NotNull CommandNode target) {
        return NodeUtils.getNodesPriorTo(Lists.modify(this.nodes, ParsedCommandNode::getNode), target);
    }
}
