package su.nightexpress.nightcore.ui.menu.data;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
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

    public boolean hasAnchor(@NonNull Player player) {
        return this.anchors.contains(player.getUniqueId());
    }

    public void addAnchor(@NonNull Player player) {
        this.anchors.add(player.getUniqueId());
    }

    public void removeAnchor(@NonNull Player player) {
        this.anchors.remove(player.getUniqueId());
    }

    public void set(@NonNull MenuViewer viewer, @NonNull T object) {
        this.set(viewer.getPlayer(), object);
    }

    public void set(@NonNull Player player, @NonNull T object) {
        this.map.put(player.getUniqueId(), object);
    }

    public boolean contains(@NonNull Player player) {
        return this.map.containsKey(player.getUniqueId());
    }

    public T get(@NonNull MenuViewer viewer) {
        return this.get(viewer.getPlayer());
    }

    public T get(@NonNull Player player) {
        return this.map.get(player.getUniqueId());
    }

    public void clear(@NonNull MenuViewer viewer) {
        this.clear(viewer.getPlayer());
    }

    public void clear(@NonNull Player player) {
        this.map.remove(player.getUniqueId());
        this.removeAnchor(player);
    }

    public void clear() {
        this.map.clear();
        this.anchors.clear();
    }
}
