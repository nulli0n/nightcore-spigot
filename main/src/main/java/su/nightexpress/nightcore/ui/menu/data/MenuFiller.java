package su.nightexpress.nightcore.ui.menu.data;

import org.bukkit.Material;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.item.ItemClick;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Deprecated
public class MenuFiller<I> {

    private final int[]                  slots;
    private final Collection<I>          items;
    private final Function<I, NightItem> itemCreator;
    private final Function<I, ItemClick> itemClick;

    public MenuFiller(int[] slots,
                      @NonNull Collection<I> items,
                      @NonNull Function<I, NightItem> itemCreator,
                      @NonNull Function<I, ItemClick> itemClick) {
        this.slots = slots;
        this.items = items;
        this.itemCreator = itemCreator;
        this.itemClick = itemClick;
    }

    @NonNull
    public static <I> Builder<I> builder(@NonNull Filled<I> menu) {
        return new Builder<>();
    }

    public void addItems(@NonNull MenuViewer viewer) {
        int limit = this.slots.length;
        int pages = (int) Math.ceil((double) items.size() / (double) limit);
        viewer.setPages(pages);
        viewer.setPage(Math.min(viewer.getPage(), viewer.getPages()));

        int skip = (viewer.getPage() - 1) * limit;

        List<I> list = items.stream().skip(skip).limit(limit).toList();

        int count = 0;
        for (I object : list) {
            NightItem item = this.itemCreator.apply(object);
            MenuItem menuItem = MenuItem.builder(item).setPriority(MenuItem.HIGH_PRIORITY).setSlots(this.slots[count++])
                .setHandler(this.itemClick.apply(object)).build();

            viewer.addItem(menuItem);
        }
    }

    //    public int[] getSlots() {
    //        return slots;
    //    }
    //
    //    @NonNull
    //    public Collection<I> getItems() {
    //        return items;
    //    }
    //
    //    @NonNull
    //    public Function<I, NightItem> getItemCreator() {
    //        return itemCreator;
    //    }
    //
    //    @NonNull
    //    public Function<I, ItemClick> getItemClick() {
    //        return itemClick;
    //    }

    public static class Builder<I> {

        private int[]                  slots;
        private Collection<I>          items;
        private Function<I, NightItem> itemCreator;
        private Function<I, ItemClick> itemClick;

        public Builder() {
            this.slots = new int[0];
            this.items = Collections.emptyList();
            this.itemCreator = obj -> new NightItem(Material.AIR);
            this.itemClick = obj -> (viewer, event) -> {
            };
        }

        public MenuFiller<I> build() {
            return new MenuFiller<>(this.slots, this.items, this.itemCreator, this.itemClick);
        }

        public Builder<I> setSlots(int... slots) {
            this.slots = slots;
            return this;
        }

        //        public Builder<I> setItems(@NonNull Collection<I> items) {
        //            return this.setItems(new ArrayList<>(items));
        //        }

        public Builder<I> setItems(@NonNull Collection<I> items) {
            this.items = items;
            return this;
        }

        public Builder<I> setItemCreator(@NonNull Function<I, NightItem> itemCreator) {
            this.itemCreator = itemCreator;
            return this;
        }

        public Builder<I> setItemClick(@NonNull Function<I, ItemClick> itemClick) {
            this.itemClick = itemClick;
            return this;
        }
    }
}
