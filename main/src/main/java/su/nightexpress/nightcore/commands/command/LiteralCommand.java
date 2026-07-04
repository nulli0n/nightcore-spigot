package su.nightexpress.nightcore.commands.command;

import java.util.List;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.commands.tree.LiteralNode;

public class LiteralCommand extends AbstractCommand<LiteralNode> {

    @Deprecated
    public LiteralCommand(@NonNull NightPlugin plugin, @NonNull LiteralNode root, @NonNull List<String> aliases) {
        super(plugin, root, aliases);
    }

    public LiteralCommand(@NonNull NightCorePlugin plugin, @NonNull LiteralNode root, @NonNull List<String> aliases) {
        super(plugin, root, aliases);
    }
}
