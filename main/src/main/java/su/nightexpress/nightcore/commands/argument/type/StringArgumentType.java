package su.nightexpress.nightcore.commands.argument.type;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.SuggestionsProvider;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.util.Lists;

import java.util.List;

public class StringArgumentType implements ArgumentType<String>, SuggestionsProvider {

    private final boolean greedy;

    public StringArgumentType(boolean greedy) {
        this.greedy = greedy;
    }

    @Override
    @NotNull
    public String parse(@NotNull CommandContextBuilder contextBuilder, @NotNull String string) throws CommandSyntaxException {
        return string;
    }

    @Override
    @NotNull
    public List<String> suggest(@NotNull ArgumentReader reader, @NotNull CommandContext context) {
        return Lists.newList("<value>");
    }

    public boolean isGreedy() {
        return this.greedy;
    }
}
