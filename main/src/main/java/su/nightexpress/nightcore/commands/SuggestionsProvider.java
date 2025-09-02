package su.nightexpress.nightcore.commands;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.context.CommandContext;

import java.util.List;

public interface SuggestionsProvider {

    @NotNull List<String> suggest(@NotNull ArgumentReader reader, @NotNull CommandContext context);
}
