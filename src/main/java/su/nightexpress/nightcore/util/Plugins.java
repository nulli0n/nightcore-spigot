package su.nightexpress.nightcore.util;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;

public class Plugins {

    public static final NightCore CORE = NightCore.getPlugin(NightCore.class);

    public static final String VAULT           = "Vault";
    public static final String PLACEHOLDER_API = "PlaceholderAPI";
    public static final String FLOODGATE       = "floodgate";

    private static final boolean HAS_PLACEHOLDER_API = isInstalled(PLACEHOLDER_API);
    private static final boolean HAS_VAULT           = isInstalled(VAULT);
    private static final boolean HAS_FLOODGATE       = isInstalled(FLOODGATE);

    public static boolean isInstalled(@NotNull String pluginName) {
        Plugin plugin = CORE.getPluginManager().getPlugin(pluginName);
        return plugin != null;
    }

    public static boolean isLoaded(@NotNull String pluginName) {
        Plugin plugin = CORE.getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }

    public static boolean isSpigot() {
        return CORE.getServer().getVersion().contains("Spigot");
    }

    public static boolean hasPlaceholderAPI() {
        return HAS_PLACEHOLDER_API;
    }

    public static boolean hasVault() {
        return HAS_VAULT;
    }

    public static boolean hasFloodgate() {
        return HAS_FLOODGATE;
    }
}
