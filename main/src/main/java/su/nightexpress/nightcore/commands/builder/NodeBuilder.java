package su.nightexpress.nightcore.commands.builder;

import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.commands.CommandRequirement;
import su.nightexpress.nightcore.commands.tree.CommandNode;

import java.util.ArrayList;
import java.util.List;

public abstract class NodeBuilder<T extends NodeBuilder<T>> {

    protected String permission;
    protected List<CommandRequirement> requirements;

    public NodeBuilder() {
        this.requirements = new ArrayList<>();
    }

    @NotNull
    protected abstract T getThis();

    @NotNull
    public abstract CommandNode build();

    @NotNull
    public T permission(@Nullable Permission permission) {
        return this.permission(permission == null ? null : permission.getName());
    }

    @NotNull
    public T permission(@Nullable String permission) {
        this.permission = permission;
        return this.getThis();
    }

    @NotNull
    public T playerOnly() {
        return this.requires(CommandRequirement.playerOnly());
    }

    @NotNull
    public T requires(@Nullable CommandRequirement requirement) {
        this.requirements.add(requirement);
        return this.getThis();
    }
}
