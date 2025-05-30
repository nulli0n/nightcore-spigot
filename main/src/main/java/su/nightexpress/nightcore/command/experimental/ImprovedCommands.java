package su.nightexpress.nightcore.command.experimental;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.experimental.node.ChainedNode;

public interface ImprovedCommands extends NightCorePlugin {

    @NotNull
    default ServerCommand getRootCommand() {
        return this.getCommandManager().getRootCommand();
    }

    @NotNull
    default ChainedNode getRootNode() {
        return this.getCommandManager().getRootCommand().getNode();
    }
}
