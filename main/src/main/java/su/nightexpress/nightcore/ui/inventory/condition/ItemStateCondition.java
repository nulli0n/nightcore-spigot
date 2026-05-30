package su.nightexpress.nightcore.ui.inventory.condition;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;

@FunctionalInterface
public interface ItemStateCondition {

    boolean canSeenBy(@NonNull ViewerContext context);
}
