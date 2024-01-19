package su.nightexpress.nightcore.util.placeholder;

import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public interface Placeholder {

    @NotNull PlaceholderMap getPlaceholders();

    @NotNull
    default UnaryOperator<String> replacePlaceholders() {
        return this.getPlaceholders().replacer();
    }
}
