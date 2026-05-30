package su.nightexpress.nightcore.util.placeholder;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface PlaceholderResolver {

    @Nullable
    String resolve(@NonNull String key);
}
