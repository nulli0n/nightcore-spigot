package su.nightexpress.nightcore.ui.inventory.action;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MenuItemAction {

    /**
     * Executes the action.
     * @param context The context of the action, containing information about the viewer and any bound object.
     */
    void execute(@NotNull ActionContext context);
}
