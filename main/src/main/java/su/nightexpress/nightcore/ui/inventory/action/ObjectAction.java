package su.nightexpress.nightcore.ui.inventory.action;

import org.jspecify.annotations.NonNull;

@FunctionalInterface
public interface ObjectAction<T> {

    void execute(@NonNull ObjectActionContext<T> context);
}
