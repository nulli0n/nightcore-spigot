package su.nightexpress.nightcore.ui.menu.type;

import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightPlugin;

@Deprecated
public abstract class NormalMenu<P extends NightPlugin> extends AbstractMenu<P> {

    public NormalMenu(@NonNull P plugin, @NonNull MenuType menuType, @NonNull String title) {
        super(plugin, menuType, title);
    }

    public boolean open(@NonNull Player player) {
        return this.open(player, viewer -> {
        });
    }
}
