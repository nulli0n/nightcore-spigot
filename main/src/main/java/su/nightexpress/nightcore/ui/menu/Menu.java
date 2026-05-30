package su.nightexpress.nightcore.ui.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.menu.click.ClickResult;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Deprecated
public interface Menu {

    void tick();

    void clear();

    void flush();

    void flush(@NonNull MenuViewer viewer);

    void flush(@NonNull Player player);

    void flush(@NonNull Player player, @NonNull Consumer<MenuViewer> consumer);

    boolean isViewer(@NonNull Player player);

    void close();

    void close(@NonNull Player player);

    void runNextTick(@NonNull Runnable runnable);

    void onClick(@NonNull MenuViewer viewer, @NonNull ClickResult result, @NonNull InventoryClickEvent event);

    void onDrag(@NonNull MenuViewer viewer, @NonNull InventoryDragEvent event);

    void onClose(@NonNull MenuViewer viewer, @NonNull InventoryCloseEvent event);

    @Deprecated
    void handleInput(Dialog.@NonNull Builder builder);

    boolean canOpen(@NonNull Player player);

    boolean isPersistent();

    @NonNull
    Set<MenuViewer> getViewers();

    @Nullable
    MenuViewer getViewer(@NonNull Player player);

    //@NonNull MenuViewer getViewerOrCreate(@NonNull Player player);

    @NonNull
    List<MenuItem> getItems(@NonNull MenuViewer viewer);

    @Nullable
    MenuItem getItem(int slot);

    @Nullable
    MenuItem getItem(@NonNull MenuViewer viewer, int slot);


    void addItem(@NonNull MenuViewer viewer, MenuItem.@NonNull Builder builder);

    void addItem(@NonNull MenuViewer viewer, @NonNull MenuItem menuItem);

    void addItem(MenuItem.@NonNull Builder builder);

    void addItem(@NonNull MenuItem menuItem);


    @NonNull
    Set<MenuItem> getItems();

    @NonNull
    MenuType getMenuType();

    void setMenuType(@NonNull MenuType menuType);

    @NonNull
    String getTitle();

    void setTitle(@NonNull String title);

    int getAutoRefreshInterval();

    void setAutoRefreshInterval(int autoRefreshInterval);

    boolean isReadyToRefresh();

    long getAutoRefreshIn();

    void setAutoRefreshIn(long autoRefreshIn);

    boolean isApplyPlaceholderAPI();

    void setApplyPlaceholderAPI(boolean applyPlaceholderAPI);
}
