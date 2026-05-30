package su.nightexpress.nightcore.ui.menu.data;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.ui.menu.MenuViewer;

@Deprecated
public interface Filled<I> {

    @NonNull
    MenuFiller<I> createFiller(@NonNull MenuViewer viewer);

    default void autoFill(@NonNull MenuViewer viewer) {
        this.createFiller(viewer).addItems(viewer);
    }
}
