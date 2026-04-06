package su.nightexpress.nightcore.ui.inventory.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightPlugin;

public abstract class AbstractMenu extends AbstractMenuBase {

    @Deprecated
    public AbstractMenu(@NonNull MenuType defaultType, @NonNull String defaultTitle) {
        super(defaultType, defaultTitle);
    }

    public AbstractMenu(@NonNull NightPlugin plugin, @NonNull MenuType defaultType, @NonNull String defaultTitle) {
        super(plugin, defaultType, defaultTitle);
    }

    @Deprecated
    public boolean show(@NonNull NightPlugin plugin, @NonNull Player player) {
        return this.showMenu(plugin.getMenuRegistry(), player, null);
    }

    public boolean show(@NonNull Player player) {
        return this.showMenu(this.plugin.getMenuRegistry(), player, null);
    }
}
