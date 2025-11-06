package su.nightexpress.nightcore.ui.inventory.action;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.ui.inventory.viewer.MenuViewer;

public class ObjectActionContext<T> extends ActionContext {

    private final T object;

    public ObjectActionContext(@NotNull MenuViewer viewer, @NotNull T object, @NotNull InventoryClickEvent event) {
        super(viewer, object, event);
        this.object = object;
    }

    @NotNull
    @Override
    public T getObject() {
        return this.object;
    }
}
