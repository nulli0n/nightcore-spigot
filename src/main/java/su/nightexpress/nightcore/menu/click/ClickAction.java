package su.nightexpress.nightcore.menu.click;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.menu.MenuViewer;

public interface ClickAction {

    void onClick(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event);
}
