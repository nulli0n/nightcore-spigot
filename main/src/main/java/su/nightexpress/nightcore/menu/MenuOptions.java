package su.nightexpress.nightcore.menu;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.util.function.UnaryOperator;

@Deprecated
public class MenuOptions {

    private String        title;
    private int           size;
    private InventoryType type;
    private int           autoRefresh;

    private long lastAutoRefresh;

    @Deprecated
    public MenuOptions(@NonNull String title, int size, @NonNull InventoryType type) {
        this(title, size, type, 0);
    }

    public MenuOptions(@NonNull String title, @NonNull MenuSize size) {
        this(title, size.getSize(), InventoryType.CHEST, 0);
    }

    public MenuOptions(@NonNull String title, @NonNull InventoryType type) {
        this(title, 27, type, 0);
    }

    public MenuOptions(@NonNull String title, int size, @NonNull InventoryType type, int autoRefresh) {
        this.setTitle(title);
        this.setSize(size);
        this.setType(type);
        this.setAutoRefresh(autoRefresh);
    }

    public MenuOptions(@NonNull MenuOptions options) {
        this(options.getTitle(), options.getSize(), options.getType(), options.getAutoRefresh());
        this.size = options.getSize();
        this.autoRefresh = options.getAutoRefresh();
        this.lastAutoRefresh = 0L;
    }

    @NonNull
    public Inventory createInventory() {
        if (this.getType() == InventoryType.CHEST) {
            return Bukkit.getServer().createInventory(null, this.getSize(), this.getTitleFormatted());
        }
        else {
            return Bukkit.getServer().createInventory(null, this.getType(), this.getTitleFormatted());
        }
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getTitleFormatted() {
        return NightMessage.asLegacy(this.getTitle());
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void editTitle(@NonNull UnaryOperator<String> function) {
        this.setTitle(function.apply(this.getTitle()));
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (size <= 0 || size % 9 != 0 || size > 54) size = 27;
        this.size = size;
    }

    @NonNull
    public InventoryType getType() {
        return type;
    }

    public void setType(@NonNull InventoryType type) {
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
        return this.getAutoRefresh() > 0 && System.currentTimeMillis() - this.getLastAutoRefresh() >= this
            .getAutoRefresh();
    }
}
