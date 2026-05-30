package su.nightexpress.nightcore.commands;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;

public interface NodeExecutor {

    boolean run(@NonNull CommandContext context, @NonNull ParsedArguments arguments);
}
