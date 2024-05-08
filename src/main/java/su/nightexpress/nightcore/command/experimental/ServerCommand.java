package su.nightexpress.nightcore.command.experimental;

import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.experimental.node.CommandNode;
import su.nightexpress.nightcore.command.impl.WrappedCommand;

public interface ServerCommand extends TabExecutor {

    boolean register();

    boolean unregister();

    @NotNull CommandNode getNode();

    @NotNull WrappedCommand getBackend();
}
