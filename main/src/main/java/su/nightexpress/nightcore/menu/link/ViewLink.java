package su.nightexpress.nightcore.menu.link;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.menu.MenuViewer;

import java.util.Map;
import java.util.WeakHashMap;

@Deprecated
public class ViewLink<T> {

    private final Map<Player, T> map;

    public ViewLink() {
        this.map = new WeakHashMap<>();
    }

    public void set(@NonNull MenuViewer viewer, @NonNull T object) {
        this.set(viewer.getPlayer(), object);
    }

    public void set(@NonNull Player viewer, @NonNull T object) {
        this.map.put(viewer, object);
    }

    public T get(@NonNull MenuViewer viewer) {
        return this.get(viewer.getPlayer());
    }

    public T get(@NonNull Player viewer) {
        return this.map.get(viewer);
    }

    public void clear(@NonNull MenuViewer viewer) {
        this.clear(viewer.getPlayer());
    }

    public void clear(@NonNull Player viewer) {
        this.map.remove(viewer);
    }
}
