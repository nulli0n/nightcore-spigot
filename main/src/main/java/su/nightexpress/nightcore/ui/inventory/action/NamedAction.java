package su.nightexpress.nightcore.ui.inventory.action;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public record NamedAction(@NonNull String name, @NonNull MenuItemAction action) implements MenuItemAction {

    @Override
    public void execute(@NotNull ActionContext context) {
        this.action.execute(context);
    }
}
