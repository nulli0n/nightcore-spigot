package su.nightexpress.nightcore.ui.inventory.display;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.ui.inventory.item.DisplayModifier;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

public record NamedDisplayModifier(@NonNull String name, @NonNull DisplayModifier modifier) implements DisplayModifier {

    @Override
    public void modify(@NotNull ViewerContext context, @NotNull NightItem item) {
        this.modifier.modify(context, item);
    }
}
