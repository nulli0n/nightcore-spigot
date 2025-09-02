package su.nightexpress.nightcore.commands;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;

public interface NodeExecutor {

    boolean run(@NotNull CommandContext context, @NotNull ParsedArguments arguments);
}
