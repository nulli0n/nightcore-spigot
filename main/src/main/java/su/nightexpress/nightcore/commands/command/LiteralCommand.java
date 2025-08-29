package su.nightexpress.nightcore.commands.command;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.commands.tree.LiteralNode;

import java.util.List;

public class LiteralCommand extends AbstractCommand<LiteralNode> {

    public LiteralCommand(@NotNull NightPlugin plugin, @NotNull LiteralNode root, @NotNull List<String> aliases) {
        super(plugin, root, aliases);
    }
}
