package su.nightexpress.nightcore.menu.link;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.menu.MenuViewer;

import java.util.Map;
import java.util.WeakHashMap;

public class ViewLink<T> {

    private final Map<Player, T> map;

    public ViewLink() {
        this.map = new WeakHashMap<>();
    }

    public void set(@NotNull MenuViewer viewer, @NotNull T object) {
        this.set(viewer.getPlayer(), object);
    }

    public void set(@NotNull Player viewer, @NotNull T object) {
        this.map.put(viewer, object);
    }

    public T get(@NotNull MenuViewer viewer) {
        return this.get(viewer.getPlayer());
    }

    public T get(@NotNull Player viewer) {
        return this.map.get(viewer);
    }

    public void clear(@NotNull MenuViewer viewer) {
        this.clear(viewer.getPlayer());
    }

    public void clear(@NotNull Player viewer) {
        this.map.remove(viewer);
    }
}
