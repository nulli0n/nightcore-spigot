package su.nightexpress.nightcore.menu.link;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.api.Menu;

@Deprecated
public interface Linked<T> extends Menu {

    @NonNull
    ViewLink<T> getLink();

    default T getLink(@NonNull MenuViewer viewer) {
        return this.getLink(viewer.getPlayer());
    }

    default T getLink(@NonNull Player player) {
        return this.getLink().get(player);
    }

    default boolean isLinkPersistent() {
        return false;
    }

    default boolean open(@NonNull Player player, @NonNull T obj) {
        this.getLink().set(player, obj);

        if (!this.open(player)) {
            this.getLink().clear(player);
            return false;
        }

        return true;
    }
}
