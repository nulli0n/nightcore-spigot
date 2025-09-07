package su.nightexpress.nightcore.commands.argument.type;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.SuggestionsProvider;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;

import java.util.Arrays;
import java.util.List;

public class BoolArgumentType implements ArgumentType<Boolean>, SuggestionsProvider {

    private static final List<String> EXAMPLES = Arrays.asList("true", "false");

    @Override
    @NotNull
    public Boolean parse(@NotNull CommandContextBuilder contextBuilder, @NotNull String string) throws CommandSyntaxException {
        return Boolean.parseBoolean(string);
    }

    @Override
    @NotNull
    public List<String> suggest(@NotNull ArgumentReader reader, @NotNull CommandContext context) {
        return EXAMPLES;
    }
}
