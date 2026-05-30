package su.nightexpress.nightcore.menu.click;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.menu.MenuViewer;

@Deprecated
public interface ClickAction {

    void onClick(@NonNull MenuViewer viewer, @NonNull InventoryClickEvent event);
}
