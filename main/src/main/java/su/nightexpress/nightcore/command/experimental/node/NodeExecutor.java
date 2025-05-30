package su.nightexpress.nightcore.command.experimental.node;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.experimental.CommandContext;

public interface NodeExecutor {

    boolean run(@NotNull CommandContext context);
}
