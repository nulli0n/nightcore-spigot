package su.nightexpress.nightcore.command.experimental;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.experimental.builder.ChainedNodeBuilder;
import su.nightexpress.nightcore.command.experimental.builder.DirectNodeBuilder;
import su.nightexpress.nightcore.command.experimental.builder.NodeBuilder;
import su.nightexpress.nightcore.command.experimental.node.ChainedNode;
import su.nightexpress.nightcore.command.experimental.node.CommandNode;
import su.nightexpress.nightcore.command.experimental.node.DirectNode;
import su.nightexpress.nightcore.command.impl.WrappedCommand;
import su.nightexpress.nightcore.util.CommandUtil;
import su.nightexpress.nightcore.util.Lists;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class RootCommand<P extends NightCorePlugin, S extends CommandNode> implements ServerCommand {

    private final P plugin;
    private final S node;

    private WrappedCommand backend;

    public RootCommand(@NotNull P plugin, @NotNull S node) {
        this.plugin = plugin;
        this.node = node;
    }

    @NotNull
    public static <T extends NightCorePlugin> RootCommand<T, DirectNode> direct(@NotNull T plugin, @NotNull String name, @NotNull Consumer<DirectNodeBuilder> consumer) {
        return direct(plugin, new String[] {name}, consumer);
    }

    @NotNull
    public static <T extends NightCorePlugin> RootCommand<T, ChainedNode> chained(@NotNull T plugin, @NotNull String name, @NotNull Consumer<ChainedNodeBuilder> consumer) {
        return chained(plugin, new String[] {name}, consumer);
    }

    @NotNull
    public static <T extends NightCorePlugin> RootCommand<T, DirectNode> direct(@NotNull T plugin, @NotNull String[] aliases, @NotNull Consumer<DirectNodeBuilder> consumer) {
        DirectNodeBuilder builder = DirectNode.builder(plugin, aliases);
        consumer.accept(builder);
        return build(plugin, builder);
    }

    @NotNull
    public static <T extends NightCorePlugin> RootCommand<T, ChainedNode> chained(@NotNull T plugin, @NotNull String[] aliases, @NotNull Consumer<ChainedNodeBuilder> consumer) {
        ChainedNodeBuilder builder = ChainedNode.builder(plugin, aliases);
        consumer.accept(builder);
        return build(plugin, builder);
    }

    @NotNull
    public static <T extends NightCorePlugin, S extends CommandNode, B extends NodeBuilder<S, B>> RootCommand<T, S> build(@NotNull T plugin, @NotNull B builder) {
        return new RootCommand<>(plugin, builder.build());
    }

    @Override
    public boolean register() {
        this.backend = new WrappedCommand(this.plugin, this, this,
            this.node.getName(),
            this.node.getAliases(),
            this.node.getDescription(),
            this.node.getUsage(),
            this.node.getPermission()
        );
        return CommandUtil.register(this.plugin, this.backend);
    }

    @Override
    public boolean unregister() {
        if (CommandUtil.unregister(this.getNode().getName())) {
            this.backend = null;
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) return Collections.emptyList();

        //int index = 0;//args.length - 1;
        //String input = args[index];
        TabContext context = new TabContext(sender, label, args, 0);
        List<String> samples = this.node.getTab(context);

        return Lists.getSequentialMatches(samples, context.getInput());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        CommandContext commandContext = new CommandContext(this.plugin, sender, label, args);

        return this.node.run(commandContext);
    }

    @Override
    @NotNull
    public S getNode() {
        return node;
    }

    @Override
    @NotNull
    public WrappedCommand getBackend() {
        return backend;
    }
}
