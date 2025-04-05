package su.nightexpress.nightcore.ui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.ui.menu.confirmation.ConfirmMenu;
import su.nightexpress.nightcore.ui.menu.confirmation.Confirmation;

public class UIUtils {

    private static ConfirmMenu confirmMenu;

    public static void load(@NotNull NightCore plugin) {
        confirmMenu = new ConfirmMenu(plugin);
    }

    public static void clear() {
        confirmMenu.clear();
        confirmMenu = null;
    }

    public static void openConfirmation(@NotNull Player player, @NotNull Confirmation confirmation) {
        confirmMenu.open(player, confirmation);
    }
}
