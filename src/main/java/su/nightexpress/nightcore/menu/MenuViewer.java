package su.nightexpress.nightcore.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.Version;

public class MenuViewer {

    private final Player player;

    private Inventory     inventory;
    private InventoryView view;
    private int           page;
    private int           pages;
    private long          lastClickTime;

    public MenuViewer(@NotNull Player player) {
        this.player = player;
        this.setPage(1);
        this.setPages(1);
    }

    public void openInventory(@NotNull Inventory inventory) {
        this.inventory = inventory;
        this.view = this.getPlayer().openInventory(inventory);
    }

    public void flushInventory(@NotNull MenuOptions options) {
        this.inventory.clear();

        if (Version.isAtLeast(Version.V1_19_R3)) {
            this.view.setTitle(options.getTitleFormatted());
        }
    }

    public boolean hasInventory() {
        return this.getInventory() != null && this.getView() != null;
    }

    public boolean canClickAgain(long cooldown) {
        return this.getLastClickTime() == 0 || (System.currentTimeMillis() - this.getLastClickTime()) > cooldown;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @Nullable
    public Inventory getInventory() {
        return inventory;
    }

    @Nullable
    public InventoryView getView() {
        return view;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = Math.max(1, page);
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = Math.max(1, pages);
    }

    public long getLastClickTime() {
        return lastClickTime;
    }

    public void setLastClickTime(long lastClickTime) {
        this.lastClickTime = lastClickTime;
    }
}
