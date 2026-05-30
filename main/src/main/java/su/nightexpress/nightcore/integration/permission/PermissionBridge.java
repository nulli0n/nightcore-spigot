package su.nightexpress.nightcore.integration.permission;

import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class PermissionBridge {

    private static PermissionProvider provider;

    public static void unregisterAll() {
        provider = null;
    }

    public static void register(@NonNull PermissionProvider permissionProvider) {
        provider = permissionProvider;
    }

    @NonNull
    public static PermissionProvider getProvider() {
        if (provider == null)
            throw new IllegalStateException("No permission provider available! You must check #hasProvider before calling this method.");
        return provider;
    }

    @NonNull
    public static Optional<PermissionProvider> provider() {
        return Optional.ofNullable(provider);
    }

    public static boolean hasProvider() {
        return provider != null;
    }
}
