package su.nightexpress.nightcore.ui.inventory.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.ui.inventory.action.MenuItemAction;
import su.nightexpress.nightcore.ui.inventory.action.ObjectAction;
import su.nightexpress.nightcore.ui.inventory.action.ObjectActionContext;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;

import java.util.Optional;

public abstract class AbstractObjectMenu<T> extends AbstractMenuBase {

    protected final Class<T> type;

    public AbstractObjectMenu(@NotNull MenuType defaultType, @NotNull String  defaultTitle, @NotNull Class<T> type) {
        super(defaultType, defaultTitle);
        this.type = type;
    }

    public boolean show(@NotNull NightPlugin plugin, @NotNull Player player, @NotNull T object) {
        return this.showMenu(plugin.getMenuRegistry(), player, object);
    }

    protected void registerObjectAction(@NotNull String id, @NotNull ObjectAction<T> action) {
        this.actionRegistry.register(id, this.createObjectAction(action));
    }

    @NotNull
    protected MenuItemAction createObjectAction(@NotNull ObjectAction<T> action) {
        return context -> {
            T boundObject = this.getObject(context);
            ObjectActionContext<T> objectContext = new ObjectActionContext<>(context.getViewer(), boundObject, context.getEvent());
            action.execute(objectContext);
        };
    }

    @NotNull
    protected Optional<T> object(@NotNull ViewerContext context) {
        return context.object(this.type);
    }

    @NotNull
    protected T getObject(@NotNull ViewerContext context) {
        return context.object(this.type)
            .orElseThrow(() -> new IllegalArgumentException("Viewer's object is defined as " + context.object().map(o -> o.getClass().getSimpleName()).orElse("null") + ", but " + this.type.getSimpleName() + " was expected"));
    }
}
