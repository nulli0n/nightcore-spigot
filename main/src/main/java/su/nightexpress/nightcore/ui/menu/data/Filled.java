package su.nightexpress.nightcore.ui.menu.data;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.ui.menu.MenuViewer;

@Deprecated
public interface Filled<I> {

    @NotNull MenuFiller<I> createFiller(@NotNull MenuViewer viewer);

    default void autoFill(@NotNull MenuViewer viewer) {
        this.createFiller(viewer).addItems(viewer);
    }
}
