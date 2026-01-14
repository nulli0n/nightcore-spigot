package su.nightexpress.nightcore.ui.menu.item;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.ui.menu.MenuViewer;

@Deprecated
public interface ItemClick {

    void onClick(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event);
}
