package su.nightexpress.nightcore.util.placeholder;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class PlaceholderEntry<T> {

    private final String key;
    private final Function<T, String> function;

    public PlaceholderEntry(@NotNull String key, @NotNull Function<T, String> function) {
        this.key = key;
        this.function = function;
    }

    @NotNull
    public String get(@NotNull T source) {
        return this.function.apply(source);
    }

    @NotNull
    public String getKey() {
        return this.key;
    }

    @NotNull
    public Function<T, String> getFunction() {
        return this.function;
    }
}
