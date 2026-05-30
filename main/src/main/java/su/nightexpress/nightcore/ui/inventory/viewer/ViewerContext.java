package su.nightexpress.nightcore.ui.inventory.viewer;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class ViewerContext {

    protected final MenuViewer viewer;
    protected final Object     object;

    public ViewerContext(@NonNull MenuViewer viewer, @Nullable Object object) {
        this.viewer = viewer;
        this.object = object;
    }

    public boolean hasObject() {
        return this.object != null;
    }

    public @NonNull Player getPlayer() {
        return this.viewer.getPlayer();
    }

    public @NonNull MenuViewer getViewer() {
        return this.viewer;
    }

    public @Nullable Object getObject() {
        return this.object;
    }

    public @NonNull Optional<Object> object() {
        return Optional.ofNullable(this.object);
    }

    public <T> @NonNull Optional<T> object(@NonNull Class<T> type) {
        return this.object().filter(type::isInstance).map(type::cast);
    }

    public <T> @NonNull T getObject(@NonNull Class<T> type) {
        if (this.object == null) {
            throw new NullPointerException("Object is null! Check #hasObject before calling this method");
        }

        return this.object(type)
            .orElseThrow(() -> new IllegalArgumentException("Viewer's object is defined as %s, but %s was expected"
                .formatted(
                    this.object().map(Object::getClass).map(Class::getSimpleName),
                    type.getSimpleName()
                ))
            );
    }
}
