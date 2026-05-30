package su.nightexpress.nightcore.ui.inventory.action;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.ui.inventory.viewer.MenuViewer;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;

public class ActionContext extends ViewerContext {

    private final InventoryClickEvent event;

    public ActionContext(@NonNull MenuViewer viewer, @Nullable Object object, @NonNull InventoryClickEvent event) {
        super(viewer, object);
        this.event = event;
    }

    @NonNull
    public InventoryClickEvent getEvent() {
        return this.event;
    }
}
