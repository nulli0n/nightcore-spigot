package su.nightexpress.nightcore.util.placeholder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class PlaceholderContext {

    private final int maxRecursion;
    private final List<PlaceholderResolver>   resolvers;
    private final List<UnaryOperator<String>> postReplacers;

    private PlaceholderContext(@NotNull List<PlaceholderResolver> resolvers,
                               @NotNull List<UnaryOperator<String>> postReplacers,
                               int maxRecursion) {
        this.resolvers = resolvers;
        this.postReplacers = postReplacers;
        this.maxRecursion = maxRecursion;
    }

    @NotNull
    public static PlaceholderContext.Builder builder() {
        return new Builder();
    }

    @NotNull
    public List<String> apply(@NotNull List<String> list) {
        if (list.isEmpty()) return List.of();

        List<String> result = new ArrayList<>(list.size() + 16);

        for (String original : list) {
            String replaced = this.apply(original);
            if (!original.isEmpty() && replaced.isEmpty()) continue; // Skip empty repalcements to keep item lore pretty.

            StringUtil.splitDelimiters(replaced, result::add);
        }

        return result;
    }

    @NotNull
    public String apply(@NotNull String string) {
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
        @Nullable
        public String resolve(@NotNull String key) {
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

        private Builder() {}

        @NotNull
        public PlaceholderContext build() {
            this.resolvers.add(key -> {
                var supplier = this.directValues.get(key);
                return supplier == null ? null : supplier.get();
            });

            return new PlaceholderContext(this.resolvers, this.postReplacers, this.maxRecursion);
        }

        @NotNull
        public Builder maxRecursion(int maxRecursion) {
            this.maxRecursion = Math.max(0, maxRecursion);
            return this;
        }

        @NotNull
        public <T> Builder with(@NotNull TypedPlaceholder<T> placeholder, @NotNull T source) {
            return this.with(placeholder.resolver(source));
        }

        @NotNull
        public Builder with(@NotNull PlaceholderResolver resolver) {
            this.resolvers.add(resolver);
            return this;
        }

        @NotNull
        public Builder with(@NotNull Map<String, String> staticValues) {
            return this.with(staticValues::get);
        }

        @NotNull
        public Builder with(@NotNull String key, @NotNull Supplier<String> replacement) {
            this.directValues.put(key, replacement);
            return this;
        }

        @NotNull
        public Builder andThen(@NotNull UnaryOperator<String> operator) {
            this.postReplacers.add(operator);
            return this;
        }
    }
}
