package su.nightexpress.nightcore.ui.inventory.item;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@FunctionalInterface
public interface DisplayModifier {

    void modify(@NotNull ViewerContext context, @NotNull NightItem item);
}
