package su.nightexpress.nightcore.ui.menu.item;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.menu.Menu;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@Deprecated
public class MenuItem {

    public static final int BACKGROUND_PRIORITY = -1;
    public static final int HIGH_PRIORITY = 100;

    protected final NightItem   item;
    protected final int         priority;
    protected final int[]       slots;
    protected final ItemHandler handler;

    public MenuItem(@NotNull NightItem item, int priority, int[] slots, @Nullable ItemHandler handler) {
        this.item = item;
        this.priority = priority;
        this.slots = slots;
        this.handler = handler;
    }

    @NotNull
    public static Builder builder(@NotNull NightItem item) {
        return new Builder().setItem(item);
    }

    @NotNull
    public static Builder buildNextPage(@NotNull Menu menu, int slot) {
        return builder(NightItem.fromType(Material.ARROW).localized(CoreLang.MENU_ICON_NEXT_PAGE))
            .setHandler(ItemHandler.forNextPage(menu))
            .setSlots(slot);
    }

    @NotNull
    public static Builder buildPreviousPage(@NotNull Menu menu, int slot) {
        return builder(NightItem.fromType(Material.ARROW).localized(CoreLang.MENU_ICON_PREVIOUS_PAGE))
            .setHandler(ItemHandler.forPreviousPage(menu))
            .setSlots(slot);
    }

    @NotNull
    public static Builder buildExit(@NotNull Menu menu, int slot) {
        return builder(NightItem.fromType(Material.IRON_DOOR).localized(CoreLang.MENU_ICON_EXIT))
            .setHandler(ItemHandler.forClose(menu))
            .setSlots(slot);
    }

    @NotNull
    public static Builder buildReturn(@NotNull Menu menu, int slot, @NotNull ItemClick click) {
        return buildReturn(menu, slot, click, null);
    }

    @NotNull
    public static Builder buildReturn(@NotNull Menu menu, int slot, @NotNull ItemClick click, @Nullable ItemOptions options) {
        return builder(NightItem.fromType(Material.IRON_DOOR).localized(CoreLang.MENU_ICON_BACK))
            .setHandler(ItemHandler.forReturn(menu, click, options))
            .setSlots(slot);
    }

    @NotNull
    public static MenuItem background(@NotNull Material type, int... slots) {
        return background(NightItem.fromType(type), slots);
    }

    @NotNull
    public static MenuItem background(@NotNull NightItem item, int... slots) {
        return item.setHideTooltip(true).toMenuItem().setSlots(slots).setPriority(BACKGROUND_PRIORITY).build();
    }

    @NotNull
    @Deprecated
    public MenuItem copy() {
        return new MenuItem(this.getItem().copy(), this.getPriority(), this.getSlots(), this.getHandler());
    }

    public boolean canSee(@NotNull MenuViewer viewer) {
        return this.handler == null || this.handler.getOptions() == null || this.handler.getOptions().canSee(viewer);
    }

    public void click(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event) {
        if (this.handler == null) return;

        this.handler.getClick().onClick(viewer, event);
    }

    @NotNull
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

        @NotNull
        public MenuItem build() {
            return new MenuItem(this.item, this.priority, this.slots, this.handler);
        }

        public Builder setItem(@NotNull NightItem item) {
            this.item = item;
            return this;
        }

        @NotNull
        public Builder setPriority(int priority) {
            this.priority = priority;
            return this;
        }

        @NotNull
        public Builder setSlots(int... slots) {
            this.slots = slots;
            return this;
        }

        @NotNull
        public Builder setHandler(@Nullable ItemHandler handler) {
            this.handler = handler;
            return this;
        }

        @NotNull
        public Builder setHandler(@NotNull ItemClick click) {
            return this.setHandler(ItemHandler.forClick(click));
        }
    }
}
