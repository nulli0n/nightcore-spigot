package su.nightexpress.nightcore.util.placeholder;

import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public class PlaceholderEntry<T> {

    private final String              key;
    private final Function<T, String> function;

    public PlaceholderEntry(@NonNull String key, @NonNull Function<T, String> function) {
        this.key = key;
        this.function = function;
    }

    @NonNull
    public String get(@NonNull T source) {
        return this.function.apply(source);
    }

    @NonNull
    public String getKey() {
        return this.key;
    }

    @NonNull
    public Function<T, String> getFunction() {
        return this.function;
    }
}
