package su.nightexpress.nightcore.ui.menu.data;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.ui.menu.MenuViewer;

import java.util.*;

@Deprecated
public class LinkCache<T> {

    private final Map<UUID, T> map;
    private final Set<UUID>    anchors;

    public LinkCache() {
        this.map = new HashMap<>();
        this.anchors = new HashSet<>();
    }

    public boolean hasAnchor(@NotNull Player player) {
        return this.anchors.contains(player.getUniqueId());
    }

    public void addAnchor(@NotNull Player player) {
        this.anchors.add(player.getUniqueId());
    }

    public void removeAnchor(@NotNull Player player) {
        this.anchors.remove(player.getUniqueId());
    }

    public void set(@NotNull MenuViewer viewer, @NotNull T object) {
        this.set(viewer.getPlayer(), object);
    }

    public void set(@NotNull Player player, @NotNull T object) {
        this.map.put(player.getUniqueId(), object);
    }

    public boolean contains(@NotNull Player player) {
        return this.map.containsKey(player.getUniqueId());
    }

    public T get(@NotNull MenuViewer viewer) {
        return this.get(viewer.getPlayer());
    }

    public T get(@NotNull Player player) {
        return this.map.get(player.getUniqueId());
    }

    public void clear(@NotNull MenuViewer viewer) {
        this.clear(viewer.getPlayer());
    }

    public void clear(@NotNull Player player) {
        this.map.remove(player.getUniqueId());
        this.removeAnchor(player);
    }

    public void clear() {
        this.map.clear();
        this.anchors.clear();
    }
}
