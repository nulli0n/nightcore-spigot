package su.nightexpress.nightcore.commands.context;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.commands.NodeUtils;
import su.nightexpress.nightcore.commands.tree.CommandNode;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.util.Lists;

import java.util.ArrayList;
import java.util.List;

public class CommandContextBuilder {

    private final NightPlugin   plugin;
    private final CommandSender sender;
    private final CommandNode   root;
    private final String        input;

    private final ParsedArguments         arguments;
    private final List<String>            flags;
    private final List<ParsedCommandNode> nodes;

    private ExecutableNode executor;

    public CommandContextBuilder(@NonNull NightPlugin plugin, @NonNull CommandSender sender, @NonNull CommandNode root,
                                 @NonNull String input) {
        this.plugin = plugin;
        this.sender = sender;
        this.root = root;
        this.input = input;
        this.arguments = new ParsedArguments();
        this.flags = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }

    @NonNull
    public CommandContext build() {
        return new CommandContext(this.plugin, this.sender, this.root, this.input, this.arguments, this.flags, this.executor, this.nodes);
    }

    @NonNull
    public CommandContextBuilder withArgument(@NonNull String name, @NonNull ParsedArgument<?> argument) {
        this.arguments.add(name, argument);
        return this;
    }

    @NonNull
    public CommandContextBuilder withFlag(@NonNull String name) {
        this.flags.add(name);
        return this;
    }

    @NonNull
    public CommandContextBuilder withExecutor(@Nullable ExecutableNode executable) {
        this.executor = executable;
        return this;
    }

    @NonNull
    public CommandContextBuilder withNode(@NonNull CommandNode node, int cursor) {
        this.nodes.add(new ParsedCommandNode(node, cursor));
        return this;
    }

    @NonNull
    public CommandSender getSender() {
        return this.sender;
    }

    @NonNull
    public CommandNode getRoot() {
        return this.root;
    }

    @NonNull
    public ParsedArguments getArguments() {
        return this.arguments;
    }

    @Nullable
    public ExecutableNode getExecutor() {
        return this.executor;
    }

    @NonNull
    public List<ParsedCommandNode> getNodes() {
        return this.nodes;
    }

    @NonNull
    public List<CommandNode> getNodesPriorTo(@NonNull CommandNode target) {
        return NodeUtils.getNodesPriorTo(Lists.modify(this.nodes, ParsedCommandNode::getNode), target);
    }
}
