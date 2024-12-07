package su.nightexpress.nightcore.ui.menu.click;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public enum ClickKey {

    LEFT, RIGHT, SHIFT_LEFT, SHIFT_RIGHT,
    DROP_KEY, SWAP_KEY,
    ;

    @NotNull
    public static ClickKey from(@NotNull InventoryClickEvent event) {
        if (event.getClick() == ClickType.DROP) return DROP_KEY;
        if (event.getClick() == ClickType.SWAP_OFFHAND) return SWAP_KEY;

        if (event.isShiftClick()) {
            return event.isRightClick() ? SHIFT_RIGHT : SHIFT_LEFT;
        }
        return event.isRightClick() ? RIGHT : LEFT;
    }
}