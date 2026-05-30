package su.nightexpress.nightcore.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.Engine;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.ui.menu.confirmation.ConfirmMenu;
import su.nightexpress.nightcore.ui.menu.confirmation.Confirmation;

@Deprecated
public class UIUtils {

    private static ConfirmMenu confirmMenu;

    public static void load(@NonNull NightCore plugin) {
        confirmMenu = new ConfirmMenu(plugin);
    }

    public static void clear() {
        if (confirmMenu != null) {
            confirmMenu.clear();
            confirmMenu = null;
        }
    }

    public static void openConfirmation(@NonNull Player player, @NonNull Confirmation confirmation) {
        if (confirmMenu == null) {
            Engine.core().error("[UIUtils] Unsupported server version.");
            return;
        }
        confirmMenu.open(player, confirmation);
    }
}
