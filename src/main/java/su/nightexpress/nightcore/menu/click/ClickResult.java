package su.nightexpress.nightcore.menu.click;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ClickResult {

    private final int slot;
    private final ItemStack itemStack;
    private final boolean isMenu;

    public ClickResult(int slot, @Nullable ItemStack itemStack, boolean isMenu) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.isMenu = isMenu;
    }

    public int getSlot() {
        return slot;
    }

    @Nullable
    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean isEmptySlot() {
        return this.itemStack == null || this.itemStack.getType().isAir();
    }

    public boolean isMenu() {
        return isMenu;
    }

    public boolean isInventory() {
        return !this.isMenu();
    }
}
