package su.nightexpress.nightcore.util.placeholder;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class TypedPlaceholder<T> {

    private final Map<String, Function<T, String>> entries;

    private TypedPlaceholder(@NonNull Map<String, Function<T, String>> entries) {
        this.entries = new LinkedHashMap<>(entries);
    }

    @NonNull
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    @NonNull
    public static <T> Builder<T> builder(@NonNull Class<T> type) {
        return new Builder<>();
    }

    @NonNull
    public Map<String, Function<T, String>> getEntries() {
        return Map.copyOf(this.entries);
    }

    @NonNull
    public PlaceholderResolver resolver(@NonNull T source) {
        return key -> {
            var func = this.entries.get(key);
            return func == null ? null : func.apply(source);
        };
    }

    @NonNull
    public UnaryOperator<String> replacer(@NonNull T source) {
        return str -> StringUtil.replacePlaceholders(str, this.resolver(source));
    }

    public static class Builder<T> {

        private final Map<String, Function<T, String>> entries = new HashMap<>();

        @NonNull
        public TypedPlaceholder<T> build() {
            return new TypedPlaceholder<>(this.entries);
        }

        @NonNull
        public Builder<T> include(@NonNull TypedPlaceholder<? super T> parent) {
            parent.getEntries().forEach((key, fun) -> {
                @SuppressWarnings("unchecked") Function<T, String> adaptedFunction = (Function<T, String>) fun;

                this.entries.putIfAbsent(key, adaptedFunction);
            });
            return this;
        }

        @NonNull
        public Builder<T> with(@NonNull String key, @NonNull Function<T, String> function) {
            this.entries.put(key, function);
            return this;
        }
    }
}
