package su.nightexpress.nightcore.bridge.placeholder;

import java.util.List;

import org.jspecify.annotations.NonNull;

public interface PlaceholderReplacer {

    @NonNull
    List<String> apply(@NonNull List<String> list);

    @NonNull
    String apply(@NonNull String string);
}
