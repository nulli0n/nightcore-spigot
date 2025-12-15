package su.nightexpress.nightcore.ui.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.ui.inventory.action.ActionRegistry;
import su.nightexpress.nightcore.ui.inventory.condition.ConditionRegistry;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.viewer.MenuViewer;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface Menu {

    void onPrepare(@NotNull ViewerContext context, @NotNull InventoryView view, @NotNull Inventory inventory, @NotNull List<MenuItem> items);

    void onReady(@NotNull ViewerContext context, @NotNull InventoryView view, @NotNull Inventory inventory);

    void onRender(@NotNull ViewerContext context, @NotNull InventoryView view, @NotNull Inventory inventory);

    void tick();

    void refresh();

    void refresh(@NotNull Player player);

    void close();

    void close(@NotNull Player player);

    void close(@NotNull UUID playerId);

    void handleClick(@NotNull Player player, @NotNull InventoryClickEvent event, @NotNull NightPlugin plugin);

    void handleDrag(@NotNull Player player, @NotNull InventoryDragEvent event);

    void handleClose(@NotNull Player player, @NotNull InventoryCloseEvent event, @NotNull MenuRegistry registry);

    boolean hasViewers();

    boolean isViewer(@NotNull Player player);

    boolean isViewer(@NotNull UUID playerId);

    //@NotNull NightComponent getTitle(@NotNull MenuViewer viewer);

    @NotNull NightComponent getTitle(@NotNull ViewerContext context);

    @NotNull MenuType getType(@NotNull MenuViewer viewer);



    @Nullable MenuViewer getViewer(@NotNull Player player);

    @Nullable MenuViewer getViewer(@NotNull UUID playerId);

    @NotNull Set<MenuViewer> getViewers();



    @NotNull ActionRegistry getActionRegistry();

    @NotNull ConditionRegistry getConditionRegistry();

    @NotNull Map<String, MenuItem> getItemsToDisplay();

    @NotNull Map<String, MenuItem> getDefaultButtons();

    @NotNull Map<String, MenuItem> getConfigButtons();

    @NotNull Map<String, MenuItem> getDefaultItems();

    @NotNull Map<String, MenuItem> getConfigItems();

    int getAutoRefreshInterval();

    long getAutoRefreshIn();

    void setAutoRefreshIn(long autoRefreshIn);

    boolean isAutoRefreshTime();

    boolean isPlaceholderIntegrationEnabled();
}
