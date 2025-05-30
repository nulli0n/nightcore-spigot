package su.nightexpress.nightcore.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.Engine;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.integration.permission.PermissionProvider;

public class Plugins {

    public static final String VAULT           = "Vault";
    public static final String LUCK_PERMS      = "LuckPerms";
    public static final String PLACEHOLDER_API = "PlaceholderAPI";
    public static final String FLOODGATE       = "floodgate";
    public static final String ECONOMY_BRIDGE  = "EconomyBridge";

    private static boolean hasPlaceholderAPI;
//    private static boolean hasVault;
    private static boolean hasFloodgate;
    private static boolean hasEconomyBridge;

    //private static PermissionProvider permissionProvider;

    public static void detectPlugins() {
        hasPlaceholderAPI = isInstalled(PLACEHOLDER_API);
//        hasVault = isInstalled(VAULT);
        hasFloodgate = isInstalled(FLOODGATE);
        hasEconomyBridge = isInstalled(ECONOMY_BRIDGE);
    }

    @NotNull
    @Deprecated
    public static NightCore getCore() {
        return Engine.core();
    }

    public static boolean isInstalled(@NotNull String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        return plugin != null;
    }

    public static boolean isLoaded(@NotNull String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }

    @Nullable
    @Deprecated
    public static PermissionProvider getPermissionProvider() {
        return Engine.getPermissions();
    }

    @Deprecated
    public static boolean hasPermissionsProvider() {
        return Engine.hasPermissions();
    }

    @Deprecated
    public static boolean isSpigot() {
        return Version.isSpigot();
    }

    public static boolean hasPlaceholderAPI() {
        return hasPlaceholderAPI;
    }

    public static boolean hasVault() {
        return isInstalled(VAULT);// hasVault;
    }

    public static boolean hasFloodgate() {
        return hasFloodgate;
    }

    public static boolean hasEconomyBridge() {
        return hasEconomyBridge;
    }
}
