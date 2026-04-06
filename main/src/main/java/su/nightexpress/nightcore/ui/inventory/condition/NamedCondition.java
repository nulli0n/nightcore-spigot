package su.nightexpress.nightcore.ui.inventory.condition;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;

public record NamedCondition(@NonNull String name, @NonNull ItemStateCondition condition) implements ItemStateCondition {

    @Override
    public boolean canSeenBy(@NotNull ViewerContext context) {
        return this.condition.canSeenBy(context);
    }
}
