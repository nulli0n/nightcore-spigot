package su.nightexpress.nightcore.menu.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.api.event.PlayerOpenMenuEvent;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.api.Menu;
import su.nightexpress.nightcore.menu.link.Linked;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.click.ClickResult;
import su.nightexpress.nightcore.menu.item.ItemOptions;
import su.nightexpress.nightcore.menu.item.MenuItem;
import su.nightexpress.nightcore.util.Lists;

import java.util.*;

public abstract class AbstractMenu<P extends NightCorePlugin> implements Menu {

    public static final Map<UUID, Menu> PLAYER_MENUS = new HashMap<>();

    public static void closeAll() {
        getActiveMenus().forEach(Menu::close);
    }

    public static void closeAll(@NotNull NightCorePlugin plugin) {
        getActiveMenus().forEach(menu -> menu.close(plugin));
    }

    public static void clearAll(@NotNull NightCorePlugin plugin) {
        getActiveMenus().stream().distinct().forEach(Menu::clear);
    }

    public static Collection<Menu> getActiveMenus() {
        return new HashSet<>(PLAYER_MENUS.values());
    }

    public static void purge(@NotNull Player player) {
        Menu menu = getMenu(player);
        if (menu == null) return;

        menu.close(player);
    }

    protected final P                     plugin;
    protected final UUID                  id;
    protected final MenuOptions           options;
    protected final Map<UUID, MenuViewer> viewers;
    protected final Set<MenuItem>         items;

    public AbstractMenu(@NotNull P plugin) {
        this(plugin, "NC Inventory", InventoryType.CHEST);
    }

    public AbstractMenu(@NotNull P plugin, @NotNull String title, @NotNull InventoryType type) {
        this(plugin, new MenuOptions(title, type));
    }

    @Deprecated
    public AbstractMenu(@NotNull P plugin, @NotNull String title, int size) {
        this(plugin, new MenuOptions(title, size, InventoryType.CHEST));
    }

    public AbstractMenu(@NotNull P plugin, @NotNull String title, @NotNull MenuSize size) {
        this(plugin, new MenuOptions(title, size));
    }

    public AbstractMenu(@NotNull P plugin, @NotNull MenuOptions options) {
        this.plugin = plugin;
        this.id = UUID.randomUUID();
        this.options = new MenuOptions(options);
        this.viewers = new HashMap<>();
        this.items = new HashSet<>();
    }

    @Nullable
    public static Menu getMenu(@NotNull Player player) {
        return PLAYER_MENUS.get(player.getUniqueId());
    }

    @Override
    public void clear() {
        this.close();
        this.getItems().clear();
        this.getViewers().clear();
    }

    @Override
    public void close() {
        new HashSet<>(this.getViewers()).forEach(viewer -> this.close(viewer.getPlayer()));
        this.viewers.clear();
    }

    @Override
    public void close(@NotNull Player player) {
        Menu current = getMenu(player);

        if (current == this && player.getOpenInventory().getType() != InventoryType.CRAFTING && player.getOpenInventory().getType() != InventoryType.CREATIVE) {
            player.closeInventory();
        }
        else {
            this.onClose(player);
        }
    }

    @Override
    public boolean close(@NotNull NightCorePlugin plugin) {
        if (this.plugin == plugin) {
            this.close();
            return true;
        }
        return false;
    }

    /*@NotNull
    public P plugin() {
        return this.plugin;
    }*/

    @Override
    public void runNextTick(@NotNull Runnable runnable) {
        this.plugin.runTask(task -> runnable.run());
    }

    @Override
    public void flush(@NotNull Player player) {
        this.open(player);
    }

    @Override
    public boolean canOpen(@NotNull Player player) {
        return !player.isSleeping();
    }

    @Override
    public boolean open(@NotNull MenuViewer viewer) {
        return this.open(viewer.getPlayer());
    }

    @Override
    public boolean open(@NotNull Player player) {
        if (!this.canOpen(player)) {
            this.purgeViewer(player);
            return false;
        }

        PlayerOpenMenuEvent event = new PlayerOpenMenuEvent(player, this);
        this.plugin.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            this.purgeViewer(player);
            //this.close(player);
            return false;
        }

        MenuOptions options = new MenuOptions(this.getOptions());
        MenuViewer viewer = this.getViewerOrCreate(player);

        this.getItems().removeIf(menuItem -> menuItem.getOptions().canBeDestroyed(viewer));
        this.onPrepare(viewer, options);

        if (!viewer.hasInventory()) {
            viewer.openInventory(options.createInventory());
        }
        else {
            viewer.flushInventory(options);
        }

        Inventory inventory = viewer.getInventory();
        if (inventory == null) {
            this.plugin.debug("Could not create " + this.getClass().getSimpleName() + " menu for '" + player.getName() + "'.");
            this.purgeViewer(player);
            return false;
        }
        if (inventory.getType() == InventoryType.CRAFTING) {
            this.plugin.warn("Got CRAFTING inventory when trying to open " + this.getClass().getSimpleName() + " menu for '" + player.getName() + "'.");
            this.purgeViewer(player);
            return false;
        }

        this.getItems(viewer).forEach(menuItem -> {
            ItemStack item = menuItem.getItemStack();
            menuItem.getOptions().modifyDisplay(viewer, item);

            for (int slot : menuItem.getSlots()) {
                if (slot < 0 || slot >= inventory.getSize()) continue;
                inventory.setItem(slot, item);
            }
        });

        this.onReady(viewer, inventory);

        PLAYER_MENUS.put(player.getUniqueId(), this);
        return true;
    }

    protected abstract void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options);

    protected abstract void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory);

    @Override
    public void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event) {
        event.setCancelled(true);

        if (result.isInventory()) return;

        MenuItem menuItem = this.getItem(viewer, result.getSlot());
        if (menuItem == null) return;

        menuItem.getHandler().getClickActions().forEach(action -> action.onClick(viewer, event));

        viewer.setLastClickTime(System.currentTimeMillis());
    }

    @Override
    public void onDrag(@NotNull MenuViewer viewer, @NotNull InventoryDragEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onClose(@NotNull MenuViewer viewer, @NotNull InventoryCloseEvent event) {
        this.onClose(viewer.getPlayer());
    }

    public void onClose(@NotNull Player player) {
        MenuViewer viewer = this.purgeViewer(player);

        // Do not clear link if entered Editor, so it can reopen menu without data loss when done.
        if (viewer != null && this instanceof Linked<?> linked && (!linked.isLinkPersistent() && !Dialog.contains(player))) {
            linked.getLink().clear(viewer);
        }

        PLAYER_MENUS.remove(player.getUniqueId());

        if (this.getViewers().isEmpty() && !this.isPersistent()) {
            this.clear();
        }
    }

    @Nullable
    private MenuViewer purgeViewer(@NotNull Player player) {
        MenuViewer viewer = this.viewers.remove(player.getUniqueId());
        if (viewer != null) {
            this.getItems().removeIf(menuItem -> menuItem.getOptions().canBeDestroyed(viewer));
        }
        return viewer;
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    @NotNull
    public Collection<MenuViewer> getViewers() {
        return this.getViewersMap().values();
    }

    @Override
    @Nullable
    public MenuViewer getViewer(@NotNull Player player) {
        return this.getViewersMap().get(player.getUniqueId());
    }

    @Override
    @NotNull
    public MenuViewer getViewerOrCreate(@NotNull Player player) {
        return this.getViewersMap().computeIfAbsent(player.getUniqueId(), k -> new MenuViewer(player));
    }



    @Override
    @NotNull
    public List<MenuItem> getItems(@NotNull MenuViewer viewer) {
        return this.getItems().stream()
            .filter(menuItem -> menuItem.canSee(viewer))
            .sorted(Comparator.comparingInt(MenuItem::getPriority)).toList();
    }

    @Override
    @Nullable
    public MenuItem getItem(int slot) {
        return this.getItems().stream()
            .filter(item -> Lists.contains(item.getSlots(), slot))
            .max(Comparator.comparingInt(MenuItem::getPriority)).orElse(null);
    }

    @Override
    @Nullable
    public MenuItem getItem(@NotNull MenuViewer viewer, int slot) {
        return this.getItems(viewer).stream()
            .filter(menuItem -> Lists.contains(menuItem.getSlots(), slot))
            .max(Comparator.comparingInt(MenuItem::getPriority)).orElse(null);
    }

    @Override
    @NotNull
    public MenuItem addItem(@NotNull ItemStack item, int... slots) {
        return this.addItem(new MenuItem(item, slots));
    }

    @Override
    @NotNull
    public MenuItem addWeakItem(@NotNull Player player, @NotNull ItemStack item, int... slots) {
        MenuItem menuItem = new MenuItem(item, slots);
        menuItem.setOptions(ItemOptions.personalWeak(player));
        return this.addItem(menuItem);
    }

    @Override
    @NotNull
    public MenuItem addItem(@NotNull MenuItem menuItem) {
        this.getItems().add(menuItem);
        return menuItem;
    }

    @Override
    @NotNull
    public UUID getId() {
        return id;
    }

    @NotNull
    private Map<UUID, MenuViewer> getViewersMap() {
        return viewers;
    }

    @Override
    @NotNull
    public Set<MenuItem> getItems() {
        return items;
    }

    @Override
    @NotNull
    public MenuOptions getOptions() {
        return options;
    }
}
