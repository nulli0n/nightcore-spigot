package su.nightexpress.nightcore.command.experimental;

import org.bukkit.command.TabExecutor;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.command.experimental.node.CommandNode;
import su.nightexpress.nightcore.command.impl.WrappedCommand;

@Deprecated
public interface ServerCommand extends TabExecutor {

    boolean register();

    boolean unregister();

    @NonNull
    CommandNode getNode();

    @NonNull
    WrappedCommand getBackend();
}
