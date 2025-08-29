package su.nightexpress.nightcore.ui.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.menu.click.ClickResult;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
public interface Menu {

    void tick();

    void clear();

    void flush();

    void flush(@NotNull MenuViewer viewer);

    void flush(@NotNull Player player);

    void flush(@NotNull Player player, @NotNull Consumer<MenuViewer> consumer);

    boolean isViewer(@NotNull Player player);

    void close();

    void close(@NotNull Player player);

    void runNextTick(@NotNull Runnable runnable);

    void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event);

    void onDrag(@NotNull MenuViewer viewer, @NotNull InventoryDragEvent event);

    void onClose(@NotNull MenuViewer viewer, @NotNull InventoryCloseEvent event);

    @Deprecated
    void handleInput(@NotNull Dialog.Builder builder);

    boolean canOpen(@NotNull Player player);

    boolean isPersistent();

    @NotNull Set<MenuViewer> getViewers();

    @Nullable MenuViewer getViewer(@NotNull Player player);

    //@NotNull MenuViewer getViewerOrCreate(@NotNull Player player);

    @NotNull List<MenuItem> getItems(@NotNull MenuViewer viewer);

    @Nullable MenuItem getItem(int slot);

    @Nullable MenuItem getItem(@NotNull MenuViewer viewer, int slot);


    void addItem(@NotNull MenuViewer viewer, @NotNull MenuItem.Builder builder);

    void addItem(@NotNull MenuViewer viewer, @NotNull MenuItem menuItem);

    void addItem(@NotNull MenuItem.Builder builder);

    void addItem(@NotNull MenuItem menuItem);


    @NotNull Set<MenuItem> getItems();

    @NotNull MenuType getMenuType();

    void setMenuType(@NotNull MenuType menuType);

    @NotNull String getTitle();

    void setTitle(@NotNull String title);

    int getAutoRefreshInterval();

    void setAutoRefreshInterval(int autoRefreshInterval);

    boolean isReadyToRefresh();

    long getAutoRefreshIn();

    void setAutoRefreshIn(long autoRefreshIn);

    boolean isApplyPlaceholderAPI();

    void setApplyPlaceholderAPI(boolean applyPlaceholderAPI);
}
