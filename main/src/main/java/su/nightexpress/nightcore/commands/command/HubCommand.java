package su.nightexpress.nightcore.commands.command;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.commands.tree.HubNode;

import java.util.List;

public class HubCommand extends AbstractCommand<HubNode> {

    public HubCommand(@NotNull NightPlugin plugin, @NotNull HubNode root, @NotNull List<String> aliases) {
        super(plugin, root, aliases);
    }
}
