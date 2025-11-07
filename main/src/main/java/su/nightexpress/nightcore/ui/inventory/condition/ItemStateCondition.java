package su.nightexpress.nightcore.ui.inventory.condition;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;

@FunctionalInterface
public interface ItemStateCondition {

    boolean canSeenBy(@NotNull ViewerContext context);
}
