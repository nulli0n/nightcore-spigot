package su.nightexpress.nightcore.commands;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.context.CommandContext;

@NullMarked
public interface SuggestionsProvider {

    List<String> suggest(ArgumentReader reader, CommandContext context);
}
