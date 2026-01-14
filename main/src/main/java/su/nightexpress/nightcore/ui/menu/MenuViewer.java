package su.nightexpress.nightcore.ui.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;

import java.util.HashSet;
import java.util.Set;

@Deprecated
public class MenuViewer {

    private final Menu          menu;
    private final Player        player;
    private final Set<MenuItem> items;

    private InventoryView view;
    private int           page;
    private int           pages;
    private long          lastClickTime;

    private boolean rebuildMenu;

    public MenuViewer(@NotNull Menu menu, @NotNull Player player) {
        this.menu = menu;
        this.player = player;
        this.items = new HashSet<>();
        this.setPage(1);
        this.setPages(1);
    }

    public void assignInventory(@NotNull InventoryView view) {
        this.view = view;
    }

    public boolean canClickAgain(long cooldown) {
        return this.getLastClickTime() == 0 || (System.currentTimeMillis() - this.getLastClickTime()) > cooldown;
    }

    public boolean isMenu(@NotNull Menu menu) {
        return this.menu == menu;
    }

    public void addItem(@NotNull MenuItem menuItem) {
        this.items.add(menuItem);
    }

    public void removeItem(@NotNull MenuItem menuItem) {
        this.items.remove(menuItem);
    }

    public boolean hasItem(@NotNull MenuItem menuItem) {
        return this.items.contains(menuItem);
    }

    public void removeItems() {
        this.items.clear();
    }

    @NotNull
    public Menu getMenu() {
        return this.menu;
    }

    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    @NotNull
    public Set<MenuItem> getItems() {
        return this.items;
    }

    @Nullable
    public Inventory getInventory() {
        return this.view == null ? null : this.view.getTopInventory();
    }

    @Nullable
    public InventoryView getView() {
        return this.view;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = Math.max(1, page);
    }

    public int getPages() {
        return this.pages;
    }

    public void setPages(int pages) {
        this.pages = Math.max(1, pages);
    }

    public long getLastClickTime() {
        return this.lastClickTime;
    }

    public void setLastClickTime(long lastClickTime) {
        this.lastClickTime = lastClickTime;
    }

    public boolean isRebuildMenu() {
        return this.rebuildMenu;
    }

    public void setRebuildMenu(boolean rebuildMenu) {
        this.rebuildMenu = rebuildMenu;
    }
}
