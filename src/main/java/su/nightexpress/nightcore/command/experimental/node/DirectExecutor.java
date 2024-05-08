package su.nightexpress.nightcore.command.experimental.node;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArguments;

public interface DirectExecutor {

    boolean execute(@NotNull CommandContext context, @NotNull ParsedArguments arguments);
}
