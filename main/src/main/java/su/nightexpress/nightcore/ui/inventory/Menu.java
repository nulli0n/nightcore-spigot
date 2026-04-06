package su.nightexpress.nightcore.ui.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.viewer.MenuViewer;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Menu {

    void onPrepare(@NonNull ViewerContext context, @NonNull InventoryView view, @NonNull Inventory inventory, @NonNull List<MenuItem> items);

    void onReady(@NonNull ViewerContext context, @NonNull InventoryView view, @NonNull Inventory inventory);

    void onRender(@NonNull ViewerContext context, @NonNull InventoryView view, @NonNull Inventory inventory);

    void load();

    void load(@NonNull Path config);

    void load(@NonNull FileConfig config);

    void tick();

    void refresh();

    void refresh(@NonNull Player player);

    void close();

    void close(@NonNull Player player);

    void close(@NonNull UUID playerId);

    void handleClick(@NonNull Player player, @NonNull InventoryClickEvent event);

    void handleDrag(@NonNull Player player, @NonNull InventoryDragEvent event);

    void handleClose(@NonNull Player player, @NonNull InventoryCloseEvent event, @NonNull MenuRegistry registry);

    boolean hasViewers();

    boolean isViewer(@NonNull Player player);

    boolean isViewer(@NonNull UUID playerId);

    @NonNull NightComponent getTitle(@NonNull ViewerContext context);

    @NonNull MenuType getType(@NonNull MenuViewer viewer);



    @Nullable MenuViewer getViewer(@NonNull Player player);

    @Nullable MenuViewer getViewer(@NonNull UUID playerId);

    @NonNull Collection<MenuViewer> getViewers();



    @NonNull MenuDataRegistry getDataRegistry();

    @NonNull Map<String, MenuItem> getItemsToDisplay();

    @NonNull Map<String, MenuItem> getDefaultButtons();

    @NonNull Map<String, MenuItem> getConfigButtons();

    @Deprecated
    @NonNull Map<String, MenuItem> getDefaultItems();

    @Deprecated
    @NonNull Map<String, MenuItem> getConfigItems();

    int getAutoRefreshInterval();

    long getAutoRefreshIn();

    void setAutoRefreshIn(long autoRefreshIn);

    boolean isAutoRefreshTime();

    boolean isPlaceholderIntegrationEnabled();
}
