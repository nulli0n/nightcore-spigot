package su.nightexpress.nightcore;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.integration.permission.PermissionBridge;
import su.nightexpress.nightcore.integration.permission.PermissionProvider;
import su.nightexpress.nightcore.util.bridge.Software;

import java.util.Set;

@Deprecated
public class Engine {

    @NonNull
    @Deprecated
    public static Set<NightPlugin> getChildrens() {
        return NightCore.CHILDRENS;
    }

    @NonNull
    @Deprecated
    public static NightCore core() {
        return NightCore.get();
    }

    @NonNull
    @Deprecated
    public static Software software() {
        return Software.instance();
    }

    @NonNull
    @Deprecated
    public static PermissionProvider getPermissions() {
        return PermissionBridge.getProvider();
    }

    @Deprecated
    public static boolean hasPermissions() {
        return PermissionBridge.hasProvider();
    }
}
