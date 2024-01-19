package su.nightexpress.nightcore.menu;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Colorizer;

import java.util.function.UnaryOperator;

public class MenuOptions {

    private String title;
    private int size;
    private InventoryType type;
    private int autoRefresh;

    private long lastAutoRefresh;

    public MenuOptions(@NotNull String title, int size, @NotNull InventoryType type) {
        this(title, size, type, 0);
    }

    public MenuOptions(@NotNull String title, int size, @NotNull InventoryType type, int autoRefresh) {
        this.setTitle(title);
        this.setSize(size);
        this.setType(type);
        this.setAutoRefresh(autoRefresh);
    }

    public MenuOptions(@NotNull MenuOptions options) {
        this(options.getTitle(), options.getSize(), options.getType(), options.getAutoRefresh());
        this.lastAutoRefresh = 0L;
    }

    @NotNull
    public Inventory createInventory() {
        String title = this.getTitle();
        if (this.getType() == InventoryType.CHEST) {
            return Bukkit.getServer().createInventory(null, this.getSize(), title);
        }
        else {
            return Bukkit.getServer().createInventory(null, this.getType(), title);
        }
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = Colorizer.apply(title);
    }

    public void editTitle(@NotNull UnaryOperator<String> function) {
        this.setTitle(function.apply(this.getTitle()));
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (size <= 0 || size % 9 != 0 || size > 54) size = 27;
        this.size = size;
    }

    @NotNull
    public InventoryType getType() {
        return type;
    }

    public void setType(@NotNull InventoryType type) {
        this.type = type;
    }

    public int getAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(int autoRefresh) {
        this.autoRefresh = Math.max(0, autoRefresh);
    }

    public long getLastAutoRefresh() {
        return lastAutoRefresh;
    }

    public void setLastAutoRefresh(long lastAutoRefresh) {
        this.lastAutoRefresh = lastAutoRefresh;
    }

    public boolean isReadyToRefresh() {
        return this.getAutoRefresh() > 0 && System.currentTimeMillis() - this.getLastAutoRefresh() >= this.getAutoRefresh();
    }
}
