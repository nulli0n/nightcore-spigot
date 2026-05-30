package su.nightexpress.nightcore.commands.command;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.commands.tree.HubNode;

import java.util.List;

public class HubCommand extends AbstractCommand<HubNode> {

    public HubCommand(@NonNull NightPlugin plugin, @NonNull HubNode root, @NonNull List<String> aliases) {
        super(plugin, root, aliases);
    }
}
