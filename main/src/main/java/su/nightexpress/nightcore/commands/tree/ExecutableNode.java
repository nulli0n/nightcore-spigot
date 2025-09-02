package su.nightexpress.nightcore.commands.tree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.commands.CommandRequirement;
import su.nightexpress.nightcore.commands.NodeExecutor;
import su.nightexpress.nightcore.commands.context.CommandContext;

import java.util.List;

public abstract class ExecutableNode extends CommandNode {

    protected final String       description;
    protected final NodeExecutor executor;

    public ExecutableNode(@NotNull String name,
                          @NotNull String description,
                          @Nullable String permission,
                          @NotNull List<CommandRequirement> requirements,
                          @Nullable NodeExecutor executor) {
        super(name, permission, requirements);
        this.description = description;
        this.executor = executor;
    }

    public abstract boolean run(@NotNull CommandContext context);

    @NotNull
    public String getDescription() {
        return this.description;
    }

    @Nullable
    public NodeExecutor getExecutor() {
        return this.executor;
    }
}
