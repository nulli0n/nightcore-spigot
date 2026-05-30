package su.nightexpress.nightcore.bridge.permission;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PermissionNamespace {

    private final String           prefix;
    private final Permission       wildcard;
    private final List<Permission> registeredPermissions;

    private PermissionNamespace(String prefix) {
        this.prefix = prefix;

        // Generate the wildcard (e.g., 'plugin.*' or 'plugin.module.*')
        this.wildcard = new Permission(prefix + ".*", PermissionDefault.OP);
        this.registeredPermissions = new ArrayList<>();

        // Add wildcard to the registry list so it is handled automatically
        this.registeredPermissions.add(this.wildcard);
    }

    public static PermissionNamespace root(String rootPrefix) {
        return new PermissionNamespace(rootPrefix);
    }

    public PermissionNamespace namespace(String subPrefix) {
        PermissionNamespace child = new PermissionNamespace(this.prefix + "." + subPrefix);

        // Link the child module's wildcard to the parent wildcard.
        // e.g., 'plugin.module.*' grants access via 'plugin.*'
        child.wildcard.addParent(this.wildcard, true);

        return child;
    }

    public Permission create(String node) {
        return this.create(node, PermissionDefault.OP);
    }

    public Permission create(String node, PermissionDefault def) {
        Permission permission = new Permission(this.prefix + "." + node, def);

        // Link this specific permission to its local namespace wildcard
        permission.addParent(this.wildcard, true);
        this.registeredPermissions.add(permission);

        return permission;
    }

    public void register(PluginManager manager) {
        for (Permission permission : this.registeredPermissions) {
            if (manager.getPermission(permission.getName()) == null) {
                manager.addPermission(permission);
            }
        }
    }

    public void unregister(PluginManager manager) {
        for (Permission permission : this.registeredPermissions) {
            manager.removePermission(permission.getName());
        }
    }

    public boolean isEmpty() {
        return this.registeredPermissions.size() <= 1; // Default wildcard permission
    }

    public String getPrefix() {
        return this.prefix;
    }
}
