package su.nightexpress.nightcore.commands.argument;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;

public interface ArgumentType<T> {

    @NonNull
    T parse(@NonNull CommandContextBuilder contextBuilder, @NonNull String string) throws CommandSyntaxException;
}
