package su.nightexpress.nightcore.util.placeholder;

import org.jspecify.annotations.NonNull;

import java.util.function.UnaryOperator;

@Deprecated
public interface Placeholder {

    @NonNull
    PlaceholderMap getPlaceholders();

    @NonNull
    default UnaryOperator<String> replacePlaceholders() {
        return this.getPlaceholders().replacer();
    }
}
