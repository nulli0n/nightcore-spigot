package su.nightexpress.nightcore.commands;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.builder.ArgumentNodeBuilder;
import su.nightexpress.nightcore.commands.builder.HubNodeBuilder;
import su.nightexpress.nightcore.commands.builder.LiteralNodeBuilder;
import su.nightexpress.nightcore.commands.tree.ArgumentNode;
import su.nightexpress.nightcore.commands.tree.HubNode;
import su.nightexpress.nightcore.commands.tree.LiteralNode;

import java.util.function.Consumer;

public class Commands {

    @NotNull
    public static HubNodeBuilder hub(@NotNull String name) {
        return new HubNodeBuilder(name);
    }

    @NotNull
    public static HubNode hub(@NotNull String name, @NotNull Consumer<HubNodeBuilder> consumer) {
        HubNodeBuilder builder = hub(name);
        consumer.accept(builder);
        return builder.build();
    }

    @NotNull
    public static LiteralNodeBuilder literal(@NotNull String name) {
        return new LiteralNodeBuilder(name);
    }

    @NotNull
    public static LiteralNode literal(@NotNull String name, @NotNull Consumer<LiteralNodeBuilder> consumer) {
        LiteralNodeBuilder builder = literal(name);
        consumer.accept(builder);
        return builder.build();
    }

    @NotNull
    public static <T> ArgumentNodeBuilder<T> argument(@NotNull String name, @NotNull ArgumentType<T> type) {
        return new ArgumentNodeBuilder<>(name, type);
    }

    @NotNull
    public static <T> ArgumentNode<T> argument(@NotNull String name, @NotNull ArgumentType<T> type, @NotNull Consumer<ArgumentNodeBuilder<T>> consumer) {
        ArgumentNodeBuilder<T> builder = argument(name, type);
        consumer.accept(builder);
        return builder.build();
    }

    @NotNull
    public static <T> ArgumentNodeBuilder<T> optionalArgument(@NotNull String name, @NotNull ArgumentType<T> type) {
        return argument(name, type).optional();
    }

    @NotNull
    public static <T> ArgumentNode<T> optionalArgument(@NotNull String name, @NotNull ArgumentType<T> type, @NotNull Consumer<ArgumentNodeBuilder<T>> consumer) {
        ArgumentNodeBuilder<T> builder = argument(name, type).optional();
        consumer.accept(builder);
        return builder.build();
    }
}
