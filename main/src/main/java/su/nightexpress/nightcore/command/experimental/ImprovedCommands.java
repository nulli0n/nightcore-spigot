package su.nightexpress.nightcore.command.experimental;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.experimental.node.ChainedNode;

@Deprecated
public interface ImprovedCommands extends NightCorePlugin {

    @NotNull
    @Deprecated
    default ServerCommand getRootCommand() {
        return this.getCommandManager().getRootCommand();
    }

    @NotNull
    @Deprecated
    default ChainedNode getRootNode() {
        return this.getCommandManager().getRootCommand().getNode();
    }
}
