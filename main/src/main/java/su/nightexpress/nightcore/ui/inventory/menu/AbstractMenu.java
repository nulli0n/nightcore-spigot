package su.nightexpress.nightcore.ui.inventory.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightPlugin;

public abstract class AbstractMenu extends AbstractMenuBase {

    public AbstractMenu(@NotNull MenuType defaultType, @NotNull String defaultTitle) {
        super(defaultType, defaultTitle);
    }

    public boolean show(@NotNull NightPlugin plugin, @NotNull Player player) {
        return this.showMenu(plugin.getMenuRegistry(), player, null);
    }
}
