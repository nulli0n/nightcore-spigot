package su.nightexpress.nightcore.bridge.placeholder;

import org.jspecify.annotations.NonNull;

public interface PlaceholderProvider {

    void addPlaceholders(@NonNull PlaceholderRegistry registry);
}
