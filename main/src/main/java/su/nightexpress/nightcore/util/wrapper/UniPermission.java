package su.nightexpress.nightcore.util.wrapper;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Deprecated
public class UniPermission extends Permission {

    public UniPermission(@NonNull String name) {
        this(name, null, null);
    }

    public UniPermission(@NonNull String name, @Nullable PermissionDefault defaultValue) {
        this(name, null, defaultValue);
    }

    public UniPermission(@NonNull String name, @Nullable String description) {
        this(name, description, PermissionDefault.OP);
    }

    public UniPermission(@NonNull String name, @Nullable String description, @Nullable PermissionDefault defaultValue) {
        super(name, description, defaultValue);
    }

    @NonNull
    public UniPermission description(String... desc) {
        this.setDescription(String.join(" ", desc));
        return this;
    }

    public void addChildren(@NonNull Permission... childrens) {
        for (Permission children : childrens) {
            children.addParent(this, true);
        }
    }
}
