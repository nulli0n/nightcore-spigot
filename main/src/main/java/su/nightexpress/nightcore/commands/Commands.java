package su.nightexpress.nightcore.commands;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.builder.ArgumentNodeBuilder;
import su.nightexpress.nightcore.commands.builder.HubNodeBuilder;
import su.nightexpress.nightcore.commands.builder.LiteralNodeBuilder;
import su.nightexpress.nightcore.commands.tree.ArgumentNode;
import su.nightexpress.nightcore.commands.tree.HubNode;
import su.nightexpress.nightcore.commands.tree.LiteralNode;

import java.util.function.Consumer;

public class Commands {

    @NonNull
    public static HubNodeBuilder hub(@NonNull String name) {
        return new HubNodeBuilder(name);
    }

    @NonNull
    public static HubNode hub(@NonNull String name, @NonNull Consumer<HubNodeBuilder> consumer) {
        HubNodeBuilder builder = hub(name);
        consumer.accept(builder);
        return builder.build();
    }

    @NonNull
    public static LiteralNodeBuilder literal(@NonNull String name) {
        return new LiteralNodeBuilder(name);
    }

    @NonNull
    public static LiteralNode literal(@NonNull String name, @NonNull Consumer<LiteralNodeBuilder> consumer) {
        LiteralNodeBuilder builder = literal(name);
        consumer.accept(builder);
        return builder.build();
    }

    @NonNull
    public static <T> ArgumentNodeBuilder<T> argument(@NonNull String name, @NonNull ArgumentType<T> type) {
        return new ArgumentNodeBuilder<>(name, type);
    }

    @NonNull
    public static <T> ArgumentNode<T> argument(@NonNull String name, @NonNull ArgumentType<T> type,
                                               @NonNull Consumer<ArgumentNodeBuilder<T>> consumer) {
        ArgumentNodeBuilder<T> builder = argument(name, type);
        consumer.accept(builder);
        return builder.build();
    }

    @NonNull
    public static <T> ArgumentNodeBuilder<T> optionalArgument(@NonNull String name, @NonNull ArgumentType<T> type) {
        return argument(name, type).optional();
    }

    @NonNull
    public static <T> ArgumentNode<T> optionalArgument(@NonNull String name, @NonNull ArgumentType<T> type,
                                                       @NonNull Consumer<ArgumentNodeBuilder<T>> consumer) {
        ArgumentNodeBuilder<T> builder = argument(name, type).optional();
        consumer.accept(builder);
        return builder.build();
    }
}
