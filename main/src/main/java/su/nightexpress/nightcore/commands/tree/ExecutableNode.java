package su.nightexpress.nightcore.commands.tree;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.commands.CommandRequirement;
import su.nightexpress.nightcore.commands.NodeExecutor;
import su.nightexpress.nightcore.commands.context.CommandContext;

import java.util.List;

public abstract class ExecutableNode extends CommandNode {

    protected final String       description;
    protected final NodeExecutor executor;

    public ExecutableNode(@NonNull String name,
                          @NonNull String description,
                          @Nullable String permission,
                          @NonNull List<CommandRequirement> requirements,
                          @Nullable NodeExecutor executor) {
        super(name, permission, requirements);
        this.description = description;
        this.executor = executor;
    }

    public abstract boolean run(@NonNull CommandContext context);

    @NonNull
    public String getDescription() {
        return this.description;
    }

    @Nullable
    public NodeExecutor getExecutor() {
        return this.executor;
    }
}
