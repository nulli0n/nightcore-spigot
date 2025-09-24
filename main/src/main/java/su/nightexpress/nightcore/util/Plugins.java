package su.nightexpress.nightcore.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.Engine;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.integration.permission.PermissionProvider;

public class Plugins {

    @Deprecated public static final String VAULT           = "Vault";
    @Deprecated public static final String LUCK_PERMS      = "LuckPerms";
    @Deprecated public static final String ECONOMY_BRIDGE  = "EconomyBridge";

    public static final String PLACEHOLDER_API = "PlaceholderAPI";
    public static final String FLOODGATE       = "floodgate";

    private static boolean hasPlaceholderAPI;
    private static boolean hasFloodgate;

    public static void detectPlugins() {
        hasPlaceholderAPI = isInstalled(PLACEHOLDER_API);
        hasFloodgate = isInstalled(FLOODGATE);
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

    @NotNull
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

    @Deprecated
    public static boolean hasVault() {
        return isInstalled(VAULT);
    }

    public static boolean hasFloodgate() {
        return hasFloodgate;
    }

    @Deprecated
    public static boolean hasEconomyBridge() {
        return isInstalled(ECONOMY_BRIDGE);
    }
}
