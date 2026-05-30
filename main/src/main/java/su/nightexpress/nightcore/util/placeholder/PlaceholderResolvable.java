package su.nightexpress.nightcore.util.placeholder;

import org.jspecify.annotations.NonNull;

@FunctionalInterface
public interface PlaceholderResolvable {

    @NonNull
    PlaceholderResolver placeholders();
}
