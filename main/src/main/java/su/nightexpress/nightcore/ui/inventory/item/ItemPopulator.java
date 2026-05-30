package su.nightexpress.nightcore.ui.inventory.item;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.ui.inventory.action.MenuItemAction;
import su.nightexpress.nightcore.ui.inventory.item.populator.SlotProvider;
import su.nightexpress.nightcore.ui.inventory.viewer.MenuViewer;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

public record ItemPopulator<T>(@NonNull SlotProvider slotProvider,
                               @NonNull BiFunction<ViewerContext, T, NightItem> itemProvider,
                               @NonNull Function<T, MenuItemAction> actionProvider) {

    public static <T> @NonNull Builder<T> builder() {
        return new Builder<>();
    }

    public static <T> @NonNull Builder<T> builder(@NonNull Class<T> type) {
        return new Builder<>();
    }

    public void populateTo(@NonNull ViewerContext context, @NonNull Collection<T> items,
                           @NonNull List<MenuItem> targetItems) {
        MenuViewer viewer = context.getViewer();

        int itemsSize = items.size();
        int[] slots = this.slotProvider.getSlots(itemsSize);
        int limit = slots.length;
        int pages = (int) Math.ceil((double) itemsSize / (double) limit);

        viewer.setTotalPages(pages);
        viewer.setCurrentPage(Math.min(viewer.getCurrentPage(), viewer.getTotalPages()));

        int skip = (viewer.getCurrentPage() - 1) * limit;

        List<T> list = items.stream().skip(skip).limit(limit).toList();

        int count = 0;
        for (T object : list) {
            NightItem item = this.itemProvider.apply(context, object);
            MenuItem menuItem = MenuItem.custom()
                .defaultState(item, this.actionProvider.apply(object))
                .slots(slots[count++])
                .build();

            targetItems.add(menuItem);
        }
    }

    public static class Builder<T> {

        private SlotProvider                            slotProvider;
        private BiFunction<ViewerContext, T, NightItem> itemProvider;
        private Function<T, MenuItemAction>             actionProvider;

        Builder() {

        }

        public @NonNull ItemPopulator<T> build() {
            Objects.requireNonNull(this.slotProvider, "No slots defined");
            Objects.requireNonNull(this.itemProvider, "No item provider defined");
            Objects.requireNonNull(this.actionProvider, "No action provider defined");

            return new ItemPopulator<>(this.slotProvider, this.itemProvider, this.actionProvider);
        }

        public @NonNull Builder<T> slots(int... slots) {
            return this.slots(size -> slots);
        }

        public @NonNull Builder<T> slots(@NonNull SlotProvider slotProvider) {
            this.slotProvider = slotProvider;
            return this;
        }

        public @NonNull Builder<T> itemProvider(@NonNull BiFunction<ViewerContext, T, NightItem> itemProvider) {
            this.itemProvider = itemProvider;
            return this;
        }

        public @NonNull Builder<T> actionProvider(@NonNull Function<T, MenuItemAction> actionProvider) {
            this.actionProvider = actionProvider;
            return this;
        }
    }
}
