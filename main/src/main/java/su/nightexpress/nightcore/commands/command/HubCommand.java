package su.nightexpress.nightcore.commands.command;

import java.util.List;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.commands.tree.HubNode;

public class HubCommand extends AbstractCommand<HubNode> {

    @Deprecated
    public HubCommand(@NonNull NightPlugin plugin, @NonNull HubNode root, @NonNull List<String> aliases) {
        super(plugin, root, aliases);
    }

    public HubCommand(@NonNull NightCorePlugin plugin, @NonNull HubNode root, @NonNull List<String> aliases) {
        super(plugin, root, aliases);
    }
}
