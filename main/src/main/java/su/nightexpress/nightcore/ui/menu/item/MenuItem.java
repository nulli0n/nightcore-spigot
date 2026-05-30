package su.nightexpress.nightcore.ui.menu.item;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@Deprecated
public class MenuItem {

    public static final int BACKGROUND_PRIORITY = -1;
    public static final int HIGH_PRIORITY       = 100;

    protected final NightItem   item;
    protected final int         priority;
    protected final int[]       slots;
    protected final ItemHandler handler;

    public MenuItem(@NonNull NightItem item, int priority, int[] slots, @Nullable ItemHandler handler) {
        this.item = item;
        this.priority = priority;
        this.slots = slots;
        this.handler = handler;
    }

    @NonNull
    public static Builder builder(@NonNull NightItem item) {
        return new Builder().setItem(item);
    }

    @NonNull
    public static Builder buildNextPage(@NonNull Menu menu, int slot) {
        return builder(NightItem.fromType(Material.ARROW).localized(CoreLang.MENU_ICON_NEXT_PAGE))
            .setHandler(ItemHandler.forNextPage(menu))
            .setSlots(slot);
    }

    @NonNull
    public static Builder buildPreviousPage(@NonNull Menu menu, int slot) {
        return builder(NightItem.fromType(Material.ARROW).localized(CoreLang.MENU_ICON_PREVIOUS_PAGE))
            .setHandler(ItemHandler.forPreviousPage(menu))
            .setSlots(slot);
    }

    @NonNull
    public static Builder buildExit(@NonNull Menu menu, int slot) {
        return builder(NightItem.fromType(Material.IRON_DOOR).localized(CoreLang.MENU_ICON_EXIT))
            .setHandler(ItemHandler.forClose(menu))
            .setSlots(slot);
    }

    @NonNull
    public static Builder buildReturn(@NonNull Menu menu, int slot, @NonNull ItemClick click) {
        return buildReturn(menu, slot, click, null);
    }

    @NonNull
    public static Builder buildReturn(@NonNull Menu menu, int slot, @NonNull ItemClick click,
                                      @Nullable ItemOptions options) {
        return builder(NightItem.fromType(Material.IRON_DOOR).localized(CoreLang.MENU_ICON_BACK))
            .setHandler(ItemHandler.forReturn(menu, click, options))
            .setSlots(slot);
    }

    @NonNull
    public static MenuItem background(@NonNull Material type, int... slots) {
        return background(NightItem.fromType(type), slots);
    }

    @NonNull
    public static MenuItem background(@NonNull NightItem item, int... slots) {
        return item.setHideTooltip(true).toMenuItem().setSlots(slots).setPriority(BACKGROUND_PRIORITY).build();
    }

    @NonNull
    @Deprecated
    public MenuItem copy() {
        return new MenuItem(this.getItem().copy(), this.getPriority(), this.getSlots(), this.getHandler());
    }

    public boolean canSee(@NonNull MenuViewer viewer) {
        return this.handler == null || this.handler.getOptions() == null || this.handler.getOptions().canSee(viewer);
    }

    public void click(@NonNull MenuViewer viewer, @NonNull InventoryClickEvent event) {
        if (this.handler == null) return;

        this.handler.getClick().onClick(viewer, event);
    }

    @NonNull
    public NightItem getItem() {
        return this.item;
    }

    public int getPriority() {
        return this.priority;
    }

    public int[] getSlots() {
        return this.slots;
    }

    @Nullable
    public ItemHandler getHandler() {
        return this.handler;
    }

    public static class Builder {

        private NightItem   item;
        private int         priority;
        private int[]       slots;
        private ItemHandler handler;

        @NonNull
        public MenuItem build() {
            return new MenuItem(this.item, this.priority, this.slots, this.handler);
        }

        public Builder setItem(@NonNull NightItem item) {
            this.item = item;
            return this;
        }

        @NonNull
        public Builder setPriority(int priority) {
            this.priority = priority;
            return this;
        }

        @NonNull
        public Builder setSlots(int... slots) {
            this.slots = slots;
            return this;
        }

        @NonNull
        public Builder setHandler(@Nullable ItemHandler handler) {
            this.handler = handler;
            return this;
        }

        @NonNull
        public Builder setHandler(@NonNull ItemClick click) {
            return this.setHandler(ItemHandler.forClick(click));
        }
    }
}
