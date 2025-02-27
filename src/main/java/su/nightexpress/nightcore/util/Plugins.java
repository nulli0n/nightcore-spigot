package su.nightexpress.nightcore.util;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;

public class Plugins {

    public static final String VAULT           = "Vault";
    public static final String PLACEHOLDER_API = "PlaceholderAPI";
    public static final String FLOODGATE       = "floodgate";
    public static final String ECONOMY_BRIDGE = "EconomyBridge";

    private static boolean hasPlaceholderAPI;
    private static boolean hasVault;
    private static boolean hasFloodgate;
    private static boolean hasEconomyBridge;

    private static NightCore core;

    public static void load(@NotNull NightCore nightCore) {
        core = nightCore;
    }

    public static void detectPlugins() {
        hasPlaceholderAPI = isInstalled(PLACEHOLDER_API);
        hasVault = isInstalled(VAULT);
        hasFloodgate = isInstalled(FLOODGATE);
        hasEconomyBridge = isInstalled(ECONOMY_BRIDGE);
    }

    @NotNull
    public static NightCore getCore() {
        return core;
    }

    public static boolean isInstalled(@NotNull String pluginName) {
        Plugin plugin = core.getPluginManager().getPlugin(pluginName);
        return plugin != null;
    }

    public static boolean isLoaded(@NotNull String pluginName) {
        Plugin plugin = core.getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }

    @Deprecated
    public static boolean isSpigot() {
        return Version.isSpigot();
    }

    public static boolean hasPlaceholderAPI() {
        return hasPlaceholderAPI;
    }

    public static boolean hasVault() {
        return hasVault;
    }

    public static boolean hasFloodgate() {
        return hasFloodgate;
    }

    public static boolean hasEconomyBridge() {
        return hasEconomyBridge;
    }
}
