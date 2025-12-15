package su.nightexpress.nightcore.util.placeholder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface PlaceholderResolver {

    @Nullable String resolve(@NotNull String key);
}
