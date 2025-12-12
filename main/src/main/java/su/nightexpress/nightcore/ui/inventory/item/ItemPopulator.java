package su.nightexpress.nightcore.ui.inventory.item;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.ui.inventory.action.MenuItemAction;
import su.nightexpress.nightcore.ui.inventory.viewer.MenuViewer;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public record ItemPopulator<T>(int[] slots,
                               @NotNull BiFunction<ViewerContext, T, NightItem> itemProvider,
                               @NotNull Function<T, MenuItemAction> actionProvider) {

    @NotNull
    public static <T> Builder<T> builder(@NotNull Class<T> type) {
        return new Builder<>();
    }

    public void populateTo(@NotNull ViewerContext context, @NotNull Collection<T> items, @NotNull List<MenuItem> targetItems) {
        MenuViewer viewer = context.getViewer();

        int limit = this.slots.length;
        int pages = (int) Math.ceil((double) items.size() / (double) limit);

        viewer.setTotalPages(pages);
        viewer.setCurrentPage(Math.min(viewer.getCurrentPage(), viewer.getTotalPages()));

        int skip = (viewer.getCurrentPage() - 1) * limit;

        List<T> list = items.stream().skip(skip).limit(limit).toList();

        int count = 0;
        for (T object : list) {
            NightItem item = this.itemProvider.apply(context, object);
            MenuItem menuItem = MenuItem.builder()
                .defaultState(item, this.actionProvider.apply(object))
                .slots(this.slots[count++])
                .build();

            targetItems.add(menuItem);
        }
    }

    public static class Builder<T> {

        private int[]                                   slots;
        private BiFunction<ViewerContext, T, NightItem> itemProvider;
        private Function<T, MenuItemAction>             actionProvider;

        Builder() {

        }

        @NotNull
        public ItemPopulator<T> build() {
            Objects.requireNonNull(this.slots, "No slots defined");
            Objects.requireNonNull(this.itemProvider, "No item provider defined");
            Objects.requireNonNull(this.actionProvider, "No action provider defined");

            return new ItemPopulator<>(this.slots, this.itemProvider, this.actionProvider);
        }

        @NotNull
        public Builder<T> slots(int... slots) {
            this.slots = slots;
            return this;
        }

        @NotNull
        public Builder<T> itemProvider(@NotNull BiFunction<ViewerContext, T, NightItem> itemProvider) {
            this.itemProvider = itemProvider;
            return this;
        }

        @NotNull
        public Builder<T> actionProvider(@NotNull Function<T, MenuItemAction> actionProvider) {
            this.actionProvider = actionProvider;
            return this;
        }
    }
}
