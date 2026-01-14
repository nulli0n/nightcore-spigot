package su.nightexpress.nightcore.ui.menu.type;

import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightPlugin;

@Deprecated
public abstract class NormalMenu<P extends NightPlugin> extends AbstractMenu<P> {

    public NormalMenu(@NotNull P plugin, @NotNull MenuType menuType, @NotNull String title) {
        super(plugin, menuType, title);
    }

    public boolean open(@NotNull Player player) {
        return this.open(player, viewer -> {});
    }
}
