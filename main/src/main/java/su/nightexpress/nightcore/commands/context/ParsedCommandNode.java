package su.nightexpress.nightcore.commands.context;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.commands.tree.CommandNode;

public class ParsedCommandNode {

    private final CommandNode node;
    private final int cursor;

    public ParsedCommandNode(@NotNull CommandNode node, int cursor) {
        this.node = node;
        this.cursor = cursor;
    }

    @NotNull
    public CommandNode getNode() {
        return this.node;
    }

    public int getCursor() {
        return this.cursor;
    }
}
