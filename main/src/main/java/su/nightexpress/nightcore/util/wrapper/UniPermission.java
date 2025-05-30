package su.nightexpress.nightcore.util.wrapper;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UniPermission extends Permission {

    public UniPermission(@NotNull String name) {
        this(name, null, null);
    }

    public UniPermission(@NotNull String name, @Nullable PermissionDefault defaultValue) {
        this(name, null, defaultValue);
    }

    public UniPermission(@NotNull String name, @Nullable String description) {
        this(name, description, PermissionDefault.OP);
    }

    public UniPermission(@NotNull String name, @Nullable String description, @Nullable PermissionDefault defaultValue) {
        super(name, description, defaultValue);
    }

    @NotNull
    public UniPermission description(String... desc) {
        this.setDescription(String.join(" ", desc));
        return this;
    }

    public void addChildren(@NotNull Permission... childrens) {
        for (Permission children : childrens) {
            children.addParent(this, true);
        }
    }
}
