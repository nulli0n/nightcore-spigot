package su.nightexpress.nightcore.ui.inventory.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.manager.AbstractListener;
import su.nightexpress.nightcore.ui.inventory.Menu;
import su.nightexpress.nightcore.ui.inventory.MenuRegistry;

public class MenuInventoryListener extends AbstractListener<NightCore> {

    private final MenuRegistry registry;

    public MenuInventoryListener(@NotNull NightCore plugin, @NotNull MenuRegistry registry) {
        super(plugin);
        this.registry = registry;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onMenuItemClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Menu menu = this.registry.getActiveMenu(player);
        if (menu == null) return;

        menu.handleClick(player, event, this.plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onMenuItemDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();

        Menu menu = this.registry.getActiveMenu(player);
        if (menu == null) return;

        menu.handleDrag(player, event);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMenuClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        Menu menu = this.registry.getActiveMenu(player);
        if (menu == null) return;

        menu.handleClose(player, event, this.registry);
    }
}
