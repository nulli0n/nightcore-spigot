package su.nightexpress.nightcore.ui.inventory.action;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ObjectAction<T> {

    void execute(@NotNull ObjectActionContext<T> context);
}
