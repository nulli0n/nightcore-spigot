package su.nightexpress.nightcore.menu.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.item.ItemOptions;
import su.nightexpress.nightcore.menu.item.MenuItem;

import java.util.ArrayList;
import java.util.List;

public interface AutoFilled<I> extends Menu {

    default boolean open(@NotNull Player player, int page) {
        return this.open(this.getViewerOrCreate(player), page);
    }

    default boolean open(@NotNull MenuViewer viewer, int page) {
        viewer.setPage(page);
        return this.open(viewer);
    }

    void onAutoFill(@NotNull MenuViewer viewer, @NotNull AutoFill<I> autoFill);

    default void autoFill(@NotNull MenuViewer viewer) {
        AutoFill<I> autoFill = new AutoFill<>();
        this.onAutoFill(viewer, autoFill);
        this.getAutoFillItems(viewer, autoFill).forEach(this::addItem);
    }

    @NotNull
    default List<MenuItem> getAutoFillItems(@NotNull MenuViewer viewer, @NotNull AutoFill<I> autoFill) {
        Player player = viewer.getPlayer();
        List<MenuItem> items = new ArrayList<>();
        List<I> origin = autoFill.getItems();//this.getAutoFills(player);

        int[] slots = autoFill.getSlots();//this.getAutoFillSlots();
        int limit = slots.length;
        int pages = (int) Math.ceil((double) origin.size() / (double) limit);
        viewer.setPages(pages);
        viewer.setPage(Math.min(viewer.getPage(), viewer.getPages()));

        int skip = (viewer.getPage() - 1) * limit;

        List<I> list = origin.stream().skip(skip).limit(limit).toList();

        int count = 0;
        for (I object : list) {
            ItemStack item = autoFill.getItemCreator().apply(object);//this.getAutoFillStack(player, object);
            ItemOptions options = ItemOptions.personalWeak(player);
            MenuItem menuItem = new MenuItem(item).setPriority(100).setOptions(options).setSlots(slots[count++])
                .addClick(autoFill.getClickAction().apply(object)/*this.getAutoFillClick(object)*/);
            items.add(menuItem);
        }

        return items;
    }
}
