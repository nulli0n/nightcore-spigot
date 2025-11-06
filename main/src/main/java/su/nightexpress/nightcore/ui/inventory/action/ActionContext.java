package su.nightexpress.nightcore.ui.inventory.action;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.ui.inventory.viewer.MenuViewer;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;

public class ActionContext extends ViewerContext {

    private final InventoryClickEvent event;

    public ActionContext(@NotNull MenuViewer viewer, @Nullable Object object, @NotNull InventoryClickEvent event) {
        super(viewer, object);
        this.event = event;
    }

    @NotNull
    public InventoryClickEvent getEvent() {
        return this.event;
    }
}
