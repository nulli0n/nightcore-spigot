package su.nightexpress.nightcore.ui.inventory.display;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.ui.inventory.item.DisplayModifier;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.List;

public record CompositeDisplayModifier(@NonNull List<DisplayModifier> modifiers) implements DisplayModifier {

    @Override
    public void modify(@NotNull ViewerContext context, @NotNull NightItem item) {
        this.modifiers.forEach(modifier -> modifier.modify(context, item));
    }
}
