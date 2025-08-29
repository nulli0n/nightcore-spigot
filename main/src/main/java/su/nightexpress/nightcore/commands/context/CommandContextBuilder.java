package su.nightexpress.nightcore.commands.context;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.commands.NodeUtils;
import su.nightexpress.nightcore.commands.tree.CommandNode;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.util.Lists;

import java.util.ArrayList;
import java.util.List;

public class CommandContextBuilder {

    private final NightPlugin plugin;
    private final CommandSender sender;
    private final CommandNode   root;
    private final String        input;

    private final ParsedArguments         arguments;
    private final List<String>            flags;
    private final List<ParsedCommandNode> nodes;

    private ExecutableNode executor;

    public CommandContextBuilder(@NotNull NightPlugin plugin, @NotNull CommandSender sender, @NotNull CommandNode root, @NotNull String input) {
        this.plugin = plugin;
        this.sender = sender;
        this.root = root;
        this.input = input;
        this.arguments = new ParsedArguments();
        this.flags = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }

    @NotNull
    public CommandContext build() {
        return new CommandContext(this.plugin, this.sender, this.root, this.input, this.arguments, this.flags, this.executor, this.nodes);
    }

    @NotNull
    public CommandContextBuilder withArgument(@NotNull String name, @NotNull ParsedArgument<?> argument) {
        this.arguments.add(name, argument);
        return this;
    }

    @NotNull
    public CommandContextBuilder withFlag(@NotNull String name) {
        this.flags.add(name);
        return this;
    }

    @NotNull
    public CommandContextBuilder withExecutor(@Nullable ExecutableNode executable) {
        this.executor = executable;
        return this;
    }

    @NotNull
    public CommandContextBuilder withNode(@NotNull CommandNode node, int cursor) {
        this.nodes.add(new ParsedCommandNode(node, cursor));
        return this;
    }

    @NotNull
    public CommandSender getSender() {
        return this.sender;
    }

    @NotNull
    public CommandNode getRoot() {
        return this.root;
    }

    @NotNull
    public ParsedArguments getArguments() {
        return this.arguments;
    }

    @Nullable
    public ExecutableNode getExecutor() {
        return this.executor;
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
