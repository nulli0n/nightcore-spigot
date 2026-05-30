package su.nightexpress.nightcore.util.placeholder;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class PlaceholderContext {

    private final int                         maxRecursion;
    private final List<PlaceholderResolver>   resolvers;
    private final List<UnaryOperator<String>> postReplacers;

    private PlaceholderContext(@NonNull List<PlaceholderResolver> resolvers,
                               @NonNull List<UnaryOperator<String>> postReplacers,
                               int maxRecursion) {
        this.resolvers = resolvers;
        this.postReplacers = postReplacers;
        this.maxRecursion = maxRecursion;
    }

    public static PlaceholderContext.@NonNull Builder builder() {
        return new Builder();
    }

    public @NonNull List<String> apply(@NonNull List<String> list) {
        if (list.isEmpty()) return List.of();

        List<String> result = new ArrayList<>(list.size() + 16);

        for (String original : list) {
            String replaced = this.apply(original);
            if (!original.isEmpty() && replaced.isEmpty()) continue; // Skip empty repalcements to keep item lore pretty.

            StringUtil.splitDelimiters(replaced, result::add);
        }

        return result;
    }

    public @NonNull String apply(@NonNull String string) {
        String replaced = StringUtil.replacePlaceholders(string, new RecursiveResolver(0));

        for (UnaryOperator<String> postReplacer : this.postReplacers) {
            replaced = postReplacer.apply(replaced);
        }

        return replaced;
    }

    private class RecursiveResolver implements PlaceholderResolver {

        private final int currentDepth;

        public RecursiveResolver(int currentDepth) {
            this.currentDepth = currentDepth;
        }

        @Override
        public @Nullable String resolve(@NonNull String key) {
            String value = null;
            for (PlaceholderResolver resolver : PlaceholderContext.this.resolvers) {
                value = resolver.resolve(key);
                if (value != null) break;
            }

            if (value == null) return null;

            if (value.indexOf('%') != -1) {
                if (this.currentDepth >= PlaceholderContext.this.maxRecursion) {
                    return value;
                }

                return StringUtil.replacePlaceholders(value, new RecursiveResolver(this.currentDepth + 1));
            }

            return value;
        }
    }

    public static class Builder {

        private final List<PlaceholderResolver>     resolvers     = new ArrayList<>();
        private final Map<String, Supplier<String>> directValues  = new LinkedHashMap<>();
        private final List<UnaryOperator<String>>   postReplacers = new ArrayList<>();

        private int maxRecursion = 0;

        private Builder() {
        }

        public @NonNull PlaceholderContext build() {
            this.resolvers.add(key -> {
                var supplier = this.directValues.get(key);
                return supplier == null ? null : supplier.get();
            });

            return new PlaceholderContext(this.resolvers, this.postReplacers, this.maxRecursion);
        }

        public @NonNull Builder maxRecursion(int maxRecursion) {
            this.maxRecursion = Math.max(0, maxRecursion);
            return this;
        }

        public <T> @NonNull Builder with(@NonNull TypedPlaceholder<T> placeholder, @NonNull T source) {
            return this.with(placeholder.resolver(source));
        }

        public @NonNull Builder with(@NonNull PlaceholderResolver resolver) {
            this.resolvers.add(resolver);
            return this;
        }

        public @NonNull Builder with(@NonNull Map<String, String> staticValues) {
            return this.with(staticValues::get);
        }

        public @NonNull Builder with(@NonNull String key, @NonNull Supplier<String> replacement) {
            this.directValues.put(key, replacement);
            return this;
        }

        public @NonNull Builder andThen(@NonNull UnaryOperator<String> operator) {
            this.postReplacers.add(operator);
            return this;
        }
    }
}
