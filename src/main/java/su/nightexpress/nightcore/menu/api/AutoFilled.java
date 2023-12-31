package su.nightexpress.nightcore.menu.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.click.ClickAction;
import su.nightexpress.nightcore.menu.item.ItemOptions;
import su.nightexpress.nightcore.menu.item.MenuItem;

import java.util.ArrayList;
import java.util.List;

public interface AutoFilled<I> extends Menu {

    int[] getAutoFillSlots();

    @NotNull List<I> getAutoFills(@NotNull Player player);

    @NotNull ItemStack getAutoFillStack(@NotNull Player player, @NotNull I object);

    @NotNull ClickAction getAutoFillClick(@NotNull I object);

    default void autoFill(@NotNull MenuViewer viewer) {
        this.getAutoFillItems(viewer).forEach(this::addItem);
    }

    @NotNull
    default List<MenuItem> getAutoFillItems(@NotNull MenuViewer viewer) {
        Player player = viewer.getPlayer();
        List<MenuItem> items = new ArrayList<>();
        List<I> origin = this.getAutoFills(player);

        int[] slots = this.getAutoFillSlots();
        int limit = slots.length;
        int pages = (int) Math.ceil((double) origin.size() / (double) limit);
        viewer.setPages(pages);
        viewer.setPage(Math.min(viewer.getPage(), viewer.getPages()));

        int skip = (viewer.getPage() - 1) * limit;

        List<I> list = origin.stream().skip(skip).limit(limit).toList();

        int count = 0;
        for (I object : list) {
            ItemStack item = this.getAutoFillStack(player, object);
            ItemOptions options = ItemOptions.personalWeak(player);
            MenuItem menuItem = new MenuItem(item).setPriority(100).setOptions(options).setSlots(slots[count++])
                .addClick(this.getAutoFillClick(object));
            //MenuItem menuItem = new MenuItem(item, 100, options, slots[count++]);
            //menuItem.addClick(this.getAutoFillClick(object));
            items.add(menuItem);
        }

        return items;
    }
}
