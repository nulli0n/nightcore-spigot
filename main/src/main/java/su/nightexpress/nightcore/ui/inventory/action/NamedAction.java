package su.nightexpress.nightcore.ui.inventory.action;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NonNull;

public record NamedAction(@NonNull String name, @NonNull MenuItemAction action) implements MenuItemAction {

    @Override
    public void execute(@NonNull ActionContext context) {
        this.action.execute(context);
    }
}
