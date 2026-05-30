package su.nightexpress.nightcore.command.experimental.node;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.command.experimental.CommandContext;

@Deprecated
public interface NodeExecutor {

    boolean run(@NonNull CommandContext context);
}
