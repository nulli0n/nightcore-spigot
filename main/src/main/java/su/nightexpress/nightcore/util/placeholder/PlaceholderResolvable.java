package su.nightexpress.nightcore.util.placeholder;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface PlaceholderResolvable {

    @NotNull PlaceholderResolver placeholders();
}
