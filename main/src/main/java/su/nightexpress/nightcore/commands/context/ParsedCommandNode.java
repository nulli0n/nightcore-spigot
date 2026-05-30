package su.nightexpress.nightcore.commands.context;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.commands.tree.CommandNode;

public class ParsedCommandNode {

    private final CommandNode node;
    private final int         cursor;

    public ParsedCommandNode(@NonNull CommandNode node, int cursor) {
        this.node = node;
        this.cursor = cursor;
    }

    @NonNull
    public CommandNode getNode() {
        return this.node;
    }

    public int getCursor() {
        return this.cursor;
    }
}
