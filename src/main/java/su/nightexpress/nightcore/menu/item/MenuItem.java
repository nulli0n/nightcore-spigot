package su.nightexpress.nightcore.menu.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.click.ClickAction;

public class MenuItem {

    protected ItemStack itemStack;
    protected int       priority;
    protected int[]     slots;

    protected ItemOptions options;
    protected ItemHandler handler;

    public MenuItem(@NotNull ItemStack itemStack) {
        this(itemStack, new int[0]);
    }

    public MenuItem(@NotNull ItemStack itemStack, int... slots) {
        this(itemStack, 0, slots);
    }

    public MenuItem(@NotNull ItemStack itemStack, int priority, int[] slots) {
        this(itemStack, priority, slots, new ItemOptions(), new ItemHandler());
    }

    public MenuItem(@NotNull ItemStack itemStack, int priority, int[] slots, @NotNull ItemOptions options, @NotNull ItemHandler handler) {
        this.setItemStack(itemStack);
        this.setPriority(priority);
        this.setSlots(slots);
        this.setOptions(options);
        this.setHandler(handler);
    }

    @NotNull
    public MenuItem copy() {
        return new MenuItem(this.getItemStack(), this.getPriority(), this.getSlots(), this.getOptions(), this.getHandler());
    }

    @NotNull
    public MenuItem resetOptions() {
        this.setOptions(new ItemOptions());
        return this;
    }

    public boolean canSee(@NotNull MenuViewer viewer) {
        if (!this.getOptions().canSee(viewer)) return false;

        var policy = this.getHandler().getVisibilityPolicy();
        return policy == null || policy.test(viewer);
    }

    @NotNull
    public ItemStack getItemStack() {
        return new ItemStack(this.itemStack);
    }

    @NotNull
    public MenuItem setItemStack(@NotNull ItemStack itemStack) {
        this.itemStack = new ItemStack(itemStack);
        return this;
    }

    public int getPriority() {
        return priority;
    }

    @NotNull
    public MenuItem setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public int[] getSlots() {
        return slots;
    }

    @NotNull
    public MenuItem setSlots(int... slots) {
        this.slots = slots;
        return this;
    }

    @NotNull
    public ItemOptions getOptions() {
        return options;
    }

    @NotNull
    public MenuItem setOptions(@NotNull ItemOptions options) {
        this.options = options;
        return this;
    }

    @NotNull
    public ItemHandler getHandler() {
        return handler;
    }

    @NotNull
    public MenuItem setHandler(@NotNull ItemHandler handler) {
        this.handler = handler;
        return this;
    }

    @NotNull
    public MenuItem setHandler(@NotNull ClickAction click) {
        return this.setHandler(ItemHandler.forClick(click));
    }

    @NotNull
    public MenuItem addClick(@NotNull ClickAction click) {
        this.getHandler().getClickActions().add(click);
        return this;
    }
}
