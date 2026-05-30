package su.nightexpress.nightcore.menu.item;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.click.ClickAction;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@Deprecated
public class MenuItem {

    protected NightItem item;
    protected ItemStack itemStack;

    protected int   priority;
    protected int[] slots;

    protected ItemOptions options;
    protected ItemHandler handler;

    @Deprecated
    public MenuItem(@NonNull ItemStack itemStack) {
        this(itemStack, new int[0]);
    }

    @Deprecated
    public MenuItem(@NonNull ItemStack itemStack, int... slots) {
        this(itemStack, 0, slots);
    }

    @Deprecated
    public MenuItem(@NonNull ItemStack itemStack, int priority, int[] slots) {
        this(itemStack, priority, slots, new ItemOptions(), new ItemHandler());
    }

    @Deprecated
    public MenuItem(@NonNull ItemStack itemStack, int priority, int[] slots, @NonNull ItemOptions options,
                    @NonNull ItemHandler handler) {
        this.setItemStack(itemStack);
        this.setPriority(priority);
        this.setSlots(slots);
        this.setOptions(options);
        this.setHandler(handler);
    }

    public MenuItem(@NonNull NightItem item) {
        this(item, new int[0]);
    }

    public MenuItem(@NonNull NightItem item, int... slots) {
        this(item, 0, slots);
    }

    public MenuItem(@NonNull NightItem item, int priority, int[] slots) {
        this(item, priority, slots, new ItemOptions(), new ItemHandler());
    }

    public MenuItem(@NonNull NightItem item, int priority, int[] slots, @NonNull ItemOptions options,
                    @NonNull ItemHandler handler) {
        this.setItem(item);
        this.setPriority(priority);
        this.setSlots(slots);
        this.setOptions(options);
        this.setHandler(handler);
    }

    @NonNull
    public MenuItem copy() {
        return new MenuItem(this.getItem(), this.getPriority(), this.getSlots(), this.getOptions(), this.getHandler());
    }

    @NonNull
    public MenuItem resetOptions() {
        this.setOptions(new ItemOptions());
        return this;
    }

    public boolean canSee(@NonNull MenuViewer viewer) {
        if (!this.getOptions().canSee(viewer)) return false;

        var policy = this.getHandler().getVisibilityPolicy();
        return policy == null || policy.test(viewer);
    }

    @NonNull
    public NightItem getItem() {
        return this.item;
    }

    public MenuItem setItem(@NonNull NightItem item) {
        this.item = item;
        return this;
    }

    @NonNull
    @Deprecated
    public ItemStack getItemStack() {
        return this.itemStack == null ? this.item.getItemStack() : new ItemStack(this.itemStack);
        //return new ItemStack(this.itemStack);
    }

    @NonNull
    @Deprecated
    public MenuItem setItemStack(@NonNull ItemStack itemStack) {
        //return this.setItem(NightItem.fromItemStack(itemStack));
        this.itemStack = new ItemStack(itemStack);
        return this;
    }

    public int getPriority() {
        return priority;
    }

    @NonNull
    public MenuItem setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public int[] getSlots() {
        return slots;
    }

    @NonNull
    public MenuItem setSlots(int... slots) {
        this.slots = slots;
        return this;
    }

    @NonNull
    public ItemOptions getOptions() {
        return options;
    }

    @NonNull
    public MenuItem setOptions(@NonNull ItemOptions options) {
        this.options = options;
        return this;
    }

    @NonNull
    public ItemHandler getHandler() {
        return handler;
    }

    @NonNull
    public MenuItem setHandler(@NonNull ItemHandler handler) {
        this.handler = handler;
        return this;
    }

    @NonNull
    public MenuItem setHandler(@NonNull ClickAction click) {
        return this.setHandler(ItemHandler.forClick(click));
    }

    @NonNull
    public MenuItem addClick(@NonNull ClickAction click) {
        this.getHandler().getClickActions().add(click);
        return this;
    }
}
