package su.nightexpress.nightcore.ui.inventory.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightPlugin;
import su.nightexpress.nightcore.ui.inventory.action.MenuItemAction;
import su.nightexpress.nightcore.ui.inventory.action.ObjectAction;
import su.nightexpress.nightcore.ui.inventory.action.ObjectActionContext;
import su.nightexpress.nightcore.ui.inventory.viewer.MenuViewer;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractObjectMenu<T> extends AbstractMenuBase {

    protected final Class<T> type;

    @Deprecated
    public AbstractObjectMenu(@NonNull MenuType defaultType, @NonNull String  defaultTitle, @NonNull Class<T> type) {
        super(defaultType, defaultTitle);
        this.type = type;
    }

    public AbstractObjectMenu(@NonNull NightPlugin plugin, @NonNull MenuType defaultType, @NonNull String  defaultTitle, @NonNull Class<T> type) {
        super(plugin, defaultType, defaultTitle);
        this.type = type;
    }

    @Deprecated
    public boolean show(@NonNull NightPlugin plugin, @NonNull Player player, @NonNull T object) {
        return this.show(plugin, player, object, null);
    }

    @Deprecated
    public boolean show(@NonNull NightPlugin plugin, @NonNull Player player, @NonNull T object, @Nullable Consumer<MenuViewer> preRender) {
        return this.showMenu(plugin.getMenuRegistry(), player, viewer -> {
            viewer.setCurrentObject(object);
            if (preRender != null) preRender.accept(viewer);
        });
    }

    public boolean show(@NonNull Player player, @NonNull T object) {
        return this.show(player, object, null);
    }

    public boolean show(@NonNull Player player, @NonNull T object, @Nullable Consumer<MenuViewer> preRender) {
        return this.showMenu(this.plugin.getMenuRegistry(), player, viewer -> {
            viewer.setCurrentObject(object);
            if (preRender != null) preRender.accept(viewer);
        });
    }


    protected void registerObjectAction(@NonNull String id, @NonNull ObjectAction<T> action) {
        this.dataRegistry.registerAction(id, this.createObjectAction(action));
    }

    @NonNull
    protected MenuItemAction createObjectAction(@NonNull ObjectAction<T> action) {
        return context -> {
            T boundObject = this.getObject(context);
            ObjectActionContext<T> objectContext = new ObjectActionContext<>(context.getViewer(), boundObject, context.getEvent());
            action.execute(objectContext);
        };
    }

    @NonNull
    protected Optional<T> object(@NonNull ViewerContext context) {
        return context.object(this.type);
    }

    @NonNull
    protected T getObject(@NonNull ViewerContext context) {
        return context.object(this.type)
            .orElseThrow(() -> new IllegalArgumentException("Viewer's object is defined as " + context.object().map(o -> o.getClass().getSimpleName()).orElse("null") + ", but " + this.type.getSimpleName() + " was expected"));
    }
}
