package su.nightexpress.nightcore.ui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.Engine;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.ui.menu.confirmation.ConfirmMenu;
import su.nightexpress.nightcore.ui.menu.confirmation.Confirmation;
import su.nightexpress.nightcore.util.Version;

@Deprecated
public class UIUtils {

    private static ConfirmMenu confirmMenu;

    public static void load(@NotNull NightCore plugin) {
        if (Version.isAtLeast(Version.MC_1_21)) {
            confirmMenu = new ConfirmMenu(plugin);
        }
    }

    public static void clear() {
        if (confirmMenu != null) {
            confirmMenu.clear();
            confirmMenu = null;
        }
    }

    public static void openConfirmation(@NotNull Player player, @NotNull Confirmation confirmation) {
        if (confirmMenu == null) {
            Engine.core().error("[UIUtils] Unsupported server version.");
            return;
        }
        confirmMenu.open(player, confirmation);
    }
}
