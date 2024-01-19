package su.nightexpress.nightcore.menu.link;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.menu.api.Menu;

public interface Linked<T> extends Menu {

    @NotNull ViewLink<T> getLink();

    default boolean open(@NotNull Player player, @NotNull T obj) {
        this.getLink().set(player, obj);

        if (!this.open(player)) {
            this.getLink().clear(player);
            return false;
        }

        return true;
    }
}
