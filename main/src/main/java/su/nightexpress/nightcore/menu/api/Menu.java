package su.nightexpress.nightcore.menu.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.click.ClickResult;
import su.nightexpress.nightcore.menu.item.MenuItem;

import java.util.*;

@Deprecated
public interface Menu {

    void clear();

    default void flush() {
        this.getViewers().forEach(this::flush);
    }

    default void flush(@NonNull MenuViewer viewer) {
        this.flush(viewer.getPlayer());
    }

    void flush(@NonNull Player player);

    default boolean isViewer(@NonNull Player player) {
        return this.getViewer(player) != null;
    }

    void close();

    void close(@NonNull Player player);

    boolean close(@NonNull NightCorePlugin plugin);

    void runNextTick(@NonNull Runnable runnable);

    void onClick(@NonNull MenuViewer viewer, @NonNull ClickResult result, @NonNull InventoryClickEvent event);

    void onDrag(@NonNull MenuViewer viewer, @NonNull InventoryDragEvent event);

    void onClose(@NonNull MenuViewer viewer, @NonNull InventoryCloseEvent event);

    boolean open(@NonNull MenuViewer viewer);

    boolean open(@NonNull Player player);

    boolean canOpen(@NonNull Player player);

    boolean isPersistent();

    @NonNull
    Collection<MenuViewer> getViewers();

    @Nullable
    MenuViewer getViewer(@NonNull Player player);

    @NonNull
    MenuViewer getViewerOrCreate(@NonNull Player player);


    @NonNull
    List<MenuItem> getItems(@NonNull MenuViewer viewer);

    @Nullable
    MenuItem getItem(int slot);

    @Nullable
    MenuItem getItem(@NonNull MenuViewer viewer, int slot);

    @NonNull
    MenuItem addItem(@NonNull ItemStack item, int... slots);

    @NonNull
    MenuItem addWeakItem(@NonNull Player player, @NonNull ItemStack item, int... slots);

    @NonNull
    MenuItem addItem(@NonNull MenuItem menuItem);

    @NonNull
    UUID getId();

    //@NonNull Map<UUID, MenuViewer> getViewersMap();

    @NonNull
    Set<MenuItem> getItems();

    @NonNull
    MenuOptions getOptions();
}
