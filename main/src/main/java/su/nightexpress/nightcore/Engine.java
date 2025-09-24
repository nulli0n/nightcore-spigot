package su.nightexpress.nightcore;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.integration.permission.PermissionBridge;
import su.nightexpress.nightcore.integration.permission.PermissionProvider;
import su.nightexpress.nightcore.util.bridge.Software;

import java.util.Set;

@Deprecated
public class Engine {

    @NotNull
    @Deprecated
    public static Set<NightPlugin> getChildrens() {
        return NightCore.CHILDRENS;
    }

    @NotNull
    @Deprecated
    public static NightCore core() {
        return NightCore.get();
    }

    @NotNull
    @Deprecated
    public static Software software() {
        return Software.instance();
    }

    @NotNull
    @Deprecated
    public static PermissionProvider getPermissions() {
        return PermissionBridge.getProvider();
    }

    @Deprecated
    public static boolean hasPermissions() {
        return PermissionBridge.hasProvider();
    }
}
