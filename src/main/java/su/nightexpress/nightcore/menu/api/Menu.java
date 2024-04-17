package su.nightexpress.nightcore.menu.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.click.ClickResult;
import su.nightexpress.nightcore.menu.item.MenuItem;

import java.util.*;

public interface Menu {

    void clear();

    default void flush() {
        this.getViewers().forEach(this::flush);
    }

    default void flush(@NotNull MenuViewer viewer) {
        this.flush(viewer.getPlayer());
    }

    void flush(@NotNull Player player);

    default boolean isViewer(@NotNull Player player) {
        return this.getViewer(player) != null;
    }

    void close();

    void close(@NotNull Player player);

    boolean close(@NotNull NightCorePlugin plugin);

    void runNextTick(@NotNull Runnable runnable);

    void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event);

    void onDrag(@NotNull MenuViewer viewer, @NotNull InventoryDragEvent event);

    void onClose(@NotNull MenuViewer viewer, @NotNull InventoryCloseEvent event);

    boolean open(@NotNull MenuViewer viewer);

    boolean open(@NotNull Player player);

    boolean canOpen(@NotNull Player player);

    boolean isPersistent();

    @NotNull Collection<MenuViewer> getViewers();

    @Nullable MenuViewer getViewer(@NotNull Player player);

    @NotNull MenuViewer getViewerOrCreate(@NotNull Player player);



    @NotNull List<MenuItem> getItems(@NotNull MenuViewer viewer);

    @Nullable MenuItem getItem(int slot);

    @Nullable MenuItem getItem(@NotNull MenuViewer viewer, int slot);

    @NotNull MenuItem addItem(@NotNull ItemStack item, int... slots);

    @NotNull MenuItem addWeakItem(@NotNull Player player, @NotNull ItemStack item, int... slots);

    @NotNull MenuItem addItem(@NotNull MenuItem menuItem);

    @NotNull UUID getId();

    //@NotNull Map<UUID, MenuViewer> getViewersMap();

    @NotNull Set<MenuItem> getItems();

    @NotNull MenuOptions getOptions();
}
