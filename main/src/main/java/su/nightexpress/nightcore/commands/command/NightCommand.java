package su.nightexpress.nightcore.commands.command;

import org.bukkit.command.PluginIdentifiableCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.builder.ExecutableNodeBuilder;
import su.nightexpress.nightcore.commands.builder.HubNodeBuilder;
import su.nightexpress.nightcore.commands.builder.LiteralNodeBuilder;
import su.nightexpress.nightcore.commands.tree.CommandNode;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.commands.tree.HubNode;
import su.nightexpress.nightcore.commands.tree.LiteralNode;
import su.nightexpress.nightcore.util.Lists;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public interface NightCommand extends PluginIdentifiableCommand {

    @NotNull NightPlugin getPlugin();

    @NotNull CommandNode getRoot();

    boolean register();

    boolean unregister();

    boolean isRegistered();

    @NotNull String getName();

    @Nullable String getPermission();

    @NotNull String getLabel();

    @NotNull List<String> getAliases();

    @NotNull String getDescription();

    @NotNull String getUsage();

    @NotNull
    static HubCommand forPlugin(@NotNull NightPlugin plugin, @NotNull Consumer<HubNodeBuilder> consumer) {
        String[] aliases = plugin.getDetails().getCommandAliases();

        return hub(plugin, aliases, builder -> {
            builder.localized(plugin.getNameLocalized());
            consumer.accept(builder);
        });
    }

    @NotNull
    static LiteralCommand literal(@NotNull NightPlugin plugin, @NotNull LiteralNode node) {
        return new LiteralCommand(plugin, node, Collections.emptyList());
    }

    @NotNull
    static LiteralCommand literal(@NotNull NightPlugin plugin, @NotNull String name, @NotNull Consumer<LiteralNodeBuilder> consumer) {
        return literal(plugin, new String[]{name}, consumer);
    }

    @NotNull
    static LiteralCommand literal(@NotNull NightPlugin plugin, @NotNull String[] names, @NotNull Consumer<LiteralNodeBuilder> consumer) {
        return create(plugin, names, Commands::literal, (root, aliases) -> new LiteralCommand(plugin, root, aliases),consumer);
    }

    @NotNull
    static HubCommand hub(@NotNull NightPlugin plugin, @NotNull HubNode node) {
        return new HubCommand(plugin, node, Collections.emptyList());
    }

    @NotNull
    static HubCommand hub(@NotNull NightPlugin plugin, @NotNull String name, @NotNull Consumer<HubNodeBuilder> consumer) {
        return hub(plugin, new String[]{name}, consumer);
    }

    @NotNull
    static HubCommand hub(@NotNull NightPlugin plugin, @NotNull String[] names, @NotNull Consumer<HubNodeBuilder> consumer) {
        return create(plugin, names, Commands::hub, (root, aliases) -> new HubCommand(plugin, root, aliases),consumer);
    }

    @NotNull
    static <C extends NightCommand, N extends ExecutableNode, B extends ExecutableNodeBuilder<N, B>> C create(
        @NotNull NightPlugin plugin,
        @NotNull String[] names,
        @NotNull Function<String, B> builderFunction,
        @NotNull BiFunction<N, List<String>, C> commandFunction,
        @NotNull Consumer<B> consumer
    ) {
        if (names == null || names.length == 0) throw new IllegalStateException("Could not create root node for empty aliases!");

        List<String> aliases = Lists.newList(names);

        B builder = builderFunction.apply(aliases.removeFirst());
        consumer.accept(builder);

        return commandFunction.apply(builder.build(), aliases);
    }
}
