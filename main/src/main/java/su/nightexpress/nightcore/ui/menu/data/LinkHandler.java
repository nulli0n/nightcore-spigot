package su.nightexpress.nightcore.ui.menu.data;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.ui.menu.MenuViewer;

public interface LinkHandler<T> {

    void handle(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event, @NotNull T obj);
}
