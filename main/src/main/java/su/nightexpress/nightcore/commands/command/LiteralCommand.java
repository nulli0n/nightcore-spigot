package su.nightexpress.nightcore.commands.command;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.commands.tree.LiteralNode;

import java.util.List;

public class LiteralCommand extends AbstractCommand<LiteralNode> {

    public LiteralCommand(@NonNull NightPlugin plugin, @NonNull LiteralNode root, @NonNull List<String> aliases) {
        super(plugin, root, aliases);
    }
}
