package su.nightexpress.nightcore.integration.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.placeholder.PlaceholderRegistry;
import su.nightexpress.nightcore.util.Reflex;

import java.util.HashMap;
import java.util.Map;

public class PAPI {

    public static final  String  NAME    = "PlaceholderAPI";
    private static final boolean PRESENT = Reflex.classExists("me.clip.placeholderapi.PlaceholderAPI");

    private static final Map<Class<? extends JavaPlugin>, Expansion> EXPANSION_MAP = new HashMap<>();

    public static boolean isPresent() {
        return PRESENT;
    }

    public static boolean addExpansion(@NonNull JavaPlugin plugin, @NonNull PlaceholderRegistry registry, @NonNull String identifier) {
        Expansion expansion = new PluginExpansion(plugin, registry, identifier);
        if (!expansion.register()) return false;

        EXPANSION_MAP.put(plugin.getClass(), expansion);
        return true;
    }

    public static boolean removeExpansions(@NonNull JavaPlugin plugin) {
        Expansion expansion = EXPANSION_MAP.remove(plugin.getClass());
        if (expansion == null) return false;

        return expansion.unregister();
    }

    @NonNull
    public static String setPlaceholders(@Nullable Player player, @NonNull String string) {
        return isPresent() ? PlaceholderAPI.setPlaceholders(null, string) : string;
    }

    @NonNull
    public static String setBracketPlaceholders(@Nullable Player player, @NonNull String string) {
        return isPresent() ? PlaceholderAPI.setBracketPlaceholders(null, string) : string;
    }
}
