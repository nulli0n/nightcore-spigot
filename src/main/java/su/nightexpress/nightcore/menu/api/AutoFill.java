package su.nightexpress.nightcore.menu.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.menu.click.ClickAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class AutoFill<T> {

    private int[] slots;
    private List<T> items;
    private Function<T, ItemStack> itemCreator;
    private Function<T, ClickAction> clickAction;

    public AutoFill() {
        this.setSlots();
        this.setItems(new ArrayList<>());
        this.setItemCreator(obj -> new ItemStack(Material.AIR));
        this.setClickAction(obj -> (viewer, event) -> {});
    }

    public int[] getSlots() {
        return slots;
    }

    public void setSlots(int... slots) {
        this.slots = slots;
    }

    @NotNull
    public List<T> getItems() {
        return items;
    }

    public void setItems(@NotNull Collection<T> items) {
        this.setItems(new ArrayList<>(items));
    }

    public void setItems(@NotNull List<T> items) {
        this.items = items;
    }

    @NotNull
    public Function<T, ItemStack> getItemCreator() {
        return itemCreator;
    }

    public void setItemCreator(@NotNull Function<T, ItemStack> itemCreator) {
        this.itemCreator = itemCreator;
    }

    @NotNull
    public Function<T, ClickAction> getClickAction() {
        return clickAction;
    }

    public void setClickAction(@NotNull Function<T, ClickAction> clickAction) {
        this.clickAction = clickAction;
    }
}
