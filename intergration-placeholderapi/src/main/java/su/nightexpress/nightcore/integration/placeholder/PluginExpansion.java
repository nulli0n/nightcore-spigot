package su.nightexpress.nightcore.integration.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.placeholder.PlaceholderRegistry;

public class PluginExpansion extends PlaceholderExpansion implements Expansion {

    private final JavaPlugin          plugin;
    private final PlaceholderRegistry registry;
    private final String              identifier;

    public PluginExpansion(@NonNull JavaPlugin plugin, @NonNull PlaceholderRegistry registry, @NonNull String identifier) {
        this.plugin = plugin;
        this.registry = registry;
        this.identifier = identifier;
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return this.plugin.getDescription().getAuthors().getFirst();
    }

    @Override
    @NotNull
    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    @Nullable
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return null;

        if (!this.registry.isEmpty()) {
            return this.registry.onPlaceholderRequest(player, params);
        }

        return super.onPlaceholderRequest(player, params);
    }
}
