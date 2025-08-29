package su.nightexpress.nightcore.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.manager.AbstractListener;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.dialog.DialogInput;
import su.nightexpress.nightcore.ui.dialog.DialogManager;
import su.nightexpress.nightcore.ui.menu.MenuRegistry;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.click.ClickResult;
import su.nightexpress.nightcore.util.NumberUtil;

public class UIListener extends AbstractListener<NightCore> {

    public UIListener(@NotNull NightCore plugin) {
        super(plugin);
    }

    private void handleDialogInput(@NotNull Player player, @NotNull Dialog dialog, @NotNull DialogInput input) {
        // Jump back to the main thread from async chat thread.
        this.plugin.runTask(task -> {
            if (input.getTextRaw().equalsIgnoreCase(DialogManager.EXIT) || dialog.getHandler().handle(input)) {
                DialogManager.stopDialog(player);
            }
        });
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        DialogManager.stopDialog(player);

        MenuRegistry.closeMenu(player);
        MenuRegistry.terminate(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDialogChatText(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Dialog dialog = DialogManager.getDialog(player);
        if (dialog == null) return;

        event.getRecipients().clear();
        event.setCancelled(true);

        DialogInput input = new DialogInput(event.getMessage());
        this.handleDialogInput(player, dialog, input);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDialogChatCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Dialog dialog = DialogManager.getDialog(player);
        if (dialog == null) return;

        event.setCancelled(true);

        String raw = event.getMessage();
        String message = raw.substring(1);
        if (message.startsWith(DialogManager.VALUES)) {
            String[] split = message.split(" ");
            int page = split.length >= 2 ? NumberUtil.getIntegerAbs(split[1]) : 1;
            DialogManager.displaySuggestions(dialog, page);
            return;
        }

        DialogInput input = new DialogInput(message);
        this.handleDialogInput(player, dialog, input);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onMenuItemClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        MenuViewer viewer = MenuRegistry.getViewer(player);
        if (viewer == null) return;

        if (!viewer.canClickAgain(CoreConfig.MENU_CLICK_COOLDOWN.get())) {
            event.setCancelled(true);
            return;
        }

        Inventory inventory = event.getInventory();
        ItemStack item = event.getCurrentItem();

        int slot = event.getRawSlot();
        boolean isMenu = slot < inventory.getSize();
        ClickResult result = new ClickResult(slot, item, isMenu);

        viewer.getMenu().onClick(viewer, result, event);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onMenuItemDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();

        MenuViewer viewer = MenuRegistry.getViewer(player);
        if (viewer == null) return;

        viewer.getMenu().onDrag(viewer, event);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMenuClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        MenuViewer viewer = MenuRegistry.getViewer(player);
        if (viewer == null) return;

        viewer.getMenu().onClose(viewer, event);

        Bukkit.getScheduler().runTaskLater(plugin, player::updateInventory, 1);
    }
}
