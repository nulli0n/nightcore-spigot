package su.nightexpress.nightcore.commands.command;

import org.bukkit.command.PluginIdentifiableCommand;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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

    @NonNull
    NightPlugin getPlugin();

    @NonNull
    CommandNode getRoot();

    boolean register();

    boolean unregister();

    boolean isRegistered();

    @NonNull
    String getName();

    @Nullable
    String getPermission();

    @NonNull
    String getLabel();

    @NonNull
    List<String> getAliases();

    @NonNull
    String getDescription();

    @NonNull
    String getUsage();

    @NonNull
    static HubCommand forPlugin(@NonNull NightPlugin plugin, @NonNull Consumer<HubNodeBuilder> consumer) {
        String[] aliases = plugin.getDetails().getCommandAliases();

        return hub(plugin, aliases, builder -> {
            builder.localized(plugin.getNameLocalized());
            consumer.accept(builder);
        });
    }

    @NonNull
    static LiteralCommand literal(@NonNull NightPlugin plugin, @NonNull LiteralNode node) {
        return new LiteralCommand(plugin, node, Collections.emptyList());
    }

    @NonNull
    static LiteralCommand literal(@NonNull NightPlugin plugin, @NonNull String name,
                                  @NonNull Consumer<LiteralNodeBuilder> consumer) {
        return literal(plugin, new String[]{name}, consumer);
    }

    @NonNull
    static LiteralCommand literal(@NonNull NightPlugin plugin, @NonNull String[] names,
                                  @NonNull Consumer<LiteralNodeBuilder> consumer) {
        return create(plugin, names, Commands::literal, (root, aliases) -> new LiteralCommand(plugin, root, aliases),
            consumer);
    }

    @NonNull
    static HubCommand hub(@NonNull NightPlugin plugin, @NonNull HubNode node) {
        return new HubCommand(plugin, node, Collections.emptyList());
    }

    @NonNull
    static HubCommand hub(@NonNull NightPlugin plugin, @NonNull String name,
                          @NonNull Consumer<HubNodeBuilder> consumer) {
        return hub(plugin, new String[]{name}, consumer);
    }

    @NonNull
    static HubCommand hub(@NonNull NightPlugin plugin, @NonNull String[] names,
                          @NonNull Consumer<HubNodeBuilder> consumer) {
        return create(plugin, names, Commands::hub, (root, aliases) -> new HubCommand(plugin, root, aliases), consumer);
    }

    @NonNull
    static <C extends NightCommand, N extends ExecutableNode, B extends ExecutableNodeBuilder<N, B>> C create(
                                                                                                              @NonNull NightPlugin plugin,
                                                                                                              @NonNull String[] names,
                                                                                                              @NonNull Function<String, B> builderFunction,
                                                                                                              @NonNull BiFunction<N, List<String>, C> commandFunction,
                                                                                                              @NonNull Consumer<B> consumer
    ) {
        if (names == null || names.length == 0)
            throw new IllegalStateException("Could not create root node for empty aliases!");

        List<String> aliases = Lists.newList(names);

        B builder = builderFunction.apply(aliases.removeFirst());
        consumer.accept(builder);

        return commandFunction.apply(builder.build(), aliases);
    }
}
