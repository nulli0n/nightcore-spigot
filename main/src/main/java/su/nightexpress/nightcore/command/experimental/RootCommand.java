package su.nightexpress.nightcore.command.experimental;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;
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

@Deprecated
public class RootCommand<P extends NightCorePlugin, S extends CommandNode> implements ServerCommand {

    private final P plugin;
    private final S node;

    private WrappedCommand backend;

    public RootCommand(@NonNull P plugin, @NonNull S node) {
        this.plugin = plugin;
        this.node = node;
    }

    @NonNull
    public static <T extends NightCorePlugin> RootCommand<T, DirectNode> direct(@NonNull T plugin, @NonNull String name,
                                                                                @NonNull Consumer<DirectNodeBuilder> consumer) {
        return direct(plugin, new String[]{name}, consumer);
    }

    @NonNull
    public static <T extends NightCorePlugin> RootCommand<T, ChainedNode> chained(@NonNull T plugin,
                                                                                  @NonNull String name,
                                                                                  @NonNull Consumer<ChainedNodeBuilder> consumer) {
        return chained(plugin, new String[]{name}, consumer);
    }

    @NonNull
    public static <T extends NightCorePlugin> RootCommand<T, DirectNode> direct(@NonNull T plugin,
                                                                                @NonNull String[] aliases,
                                                                                @NonNull Consumer<DirectNodeBuilder> consumer) {
        DirectNodeBuilder builder = DirectNode.builder(plugin, aliases);
        consumer.accept(builder);
        return build(plugin, builder);
    }

    @NonNull
    public static <T extends NightCorePlugin> RootCommand<T, ChainedNode> chained(@NonNull T plugin,
                                                                                  @NonNull String[] aliases,
                                                                                  @NonNull Consumer<ChainedNodeBuilder> consumer) {
        ChainedNodeBuilder builder = ChainedNode.builder(plugin, aliases);
        consumer.accept(builder);
        return build(plugin, builder);
    }

    @NonNull
    public static <T extends NightCorePlugin, S extends CommandNode, B extends NodeBuilder<S, B>> RootCommand<T, S> build(@NonNull T plugin,
                                                                                                                          @NonNull B builder) {
        return new RootCommand<>(plugin, builder.build());
    }

    @Override
    public boolean register() {
        this.backend = new WrappedCommand(this.plugin, this, this, this.node.getName(), this.node
            .getAliases(), this.node.getDescription(), this.node.getUsage(), this.node.getPermission()
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
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label,
                                      String[] args) {
        if (args.length == 0) return Collections.emptyList();

        TabContext context = new TabContext(sender, label, args, 0);
        List<String> samples = this.node.getTab(context);

        return Lists.getSequentialMatches(samples, context.getInput());
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label,
                             @NonNull String[] args) {
        CommandContext commandContext = new CommandContext(this.plugin, sender, label, args);

        return this.node.run(commandContext);
    }

    @Override
    @NonNull
    public S getNode() {
        return node;
    }

    @Override
    @NonNull
    public WrappedCommand getBackend() {
        return backend;
    }
}
