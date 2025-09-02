package su.nightexpress.nightcore.commands.argument;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.SuggestionsProvider;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;

public interface ArgumentType<T> extends SuggestionsProvider {

    @NotNull T parse(@NotNull CommandContextBuilder contextBuilder, @NotNull String string) throws CommandSyntaxException;

}
