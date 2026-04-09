package su.nightexpress.nightcore.commands;

import java.util.List;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.context.CommandContext;

public interface SuggestionsProvider {

    @NonNull
    List<String> suggest(@NonNull ArgumentReader reader, @NonNull CommandContext context);
}
