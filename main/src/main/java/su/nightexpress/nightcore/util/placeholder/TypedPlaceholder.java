package su.nightexpress.nightcore.util.placeholder;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class TypedPlaceholder<T> {

    private final Map<String, Function<T, String>> entries;

    private TypedPlaceholder(@NotNull Map<String, Function<T, String>> entries) {
        this.entries = new LinkedHashMap<>(entries);
    }

    @NotNull
    public static <T> Builder<T> builder(@NotNull Class<T> type) {
        return new Builder<>();
    }

    @NotNull
    public Map<String, Function<T, String>> getEntries() {
        return Map.copyOf(this.entries);
    }

    @NotNull
    public PlaceholderResolver resolver(@NotNull T source) {
        return key -> {
            var func = this.entries.get(key);
            return func == null ? null : func.apply(source);
        };
    }

    @NotNull
    public UnaryOperator<String> replacer(@NotNull T source) {
        return str -> StringUtil.replacePlaceholders(str, this.resolver(source));
    }

    public static class Builder<T> {

        private final Map<String, Function<T, String>> entries = new HashMap<>();

        @NotNull
        public TypedPlaceholder<T> build() {
            return new TypedPlaceholder<>(this.entries);
        }

        @NotNull
        public Builder<T> include(@NotNull TypedPlaceholder<? super T> parent) {
            parent.getEntries().forEach((key, fun) -> {
                @SuppressWarnings("unchecked")
                Function<T, String> adaptedFunction = (Function<T, String>) fun;

                this.entries.putIfAbsent(key, adaptedFunction);
            });
            return this;
        }

        @NotNull
        public Builder<T> with(@NotNull String key, @NotNull Function<T, String> function) {
            this.entries.put(key, function);
            return this;
        }
    }
}
