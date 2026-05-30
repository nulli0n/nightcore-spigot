package su.nightexpress.nightcore.ui.inventory.action;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.ui.inventory.viewer.MenuViewer;

public class ObjectActionContext<T> extends ActionContext {

    private final T object;

    public ObjectActionContext(@NonNull MenuViewer viewer, @NonNull T object, @NonNull InventoryClickEvent event) {
        super(viewer, object, event);
        this.object = object;
    }

    @NonNull
    @Override
    public T getObject() {
        return this.object;
    }
}
