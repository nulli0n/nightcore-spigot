package su.nightexpress.nightcore.ui.inventory.viewer;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ViewerContext {

    protected final MenuViewer viewer;
    protected final Object     object;

    public ViewerContext(@NotNull MenuViewer viewer, @Nullable Object object) {
        this.viewer = viewer;
        this.object = object;
    }

    @NotNull
    public Player getPlayer() {
        return this.viewer.getPlayer();
    }

    @NotNull
    public MenuViewer getViewer() {
        return this.viewer;
    }

    @Nullable
    public Object getObject() {
        return this.object;
    }

    @NotNull
    public Optional<Object> object() {
        return Optional.ofNullable(this.object);
    }

    @NotNull
    public <T> Optional<T> object(@NotNull Class<T> type) {
        return this.object().filter(type::isInstance).map(type::cast);
    }
}
