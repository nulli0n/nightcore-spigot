package su.nightexpress.nightcore.util.placeholder;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class PlaceholderContext {

    private final List<PlaceholderResolver>   resolvers;
    private final List<UnaryOperator<String>> postReplacers;

    private PlaceholderContext(@NotNull List<PlaceholderResolver> resolvers, @NotNull List<UnaryOperator<String>> postReplacers) {
        this.resolvers = resolvers;
        this.postReplacers = postReplacers;
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

            StringUtil.splitDelimiters(replaced, result::add);
        }

        return result;
    }

    @NotNull
    public String apply(@NotNull String string) {
        String replaced = StringUtil.replacePlaceholders(string, key -> {

            for (PlaceholderResolver resolver : this.resolvers) {
                String result = resolver.resolve(key);
                if (result != null) return result;
            }

            return null;
        });

        for (UnaryOperator<String> postReplacer : this.postReplacers) {
            replaced = postReplacer.apply(replaced);
        }

        return replaced;
    }

    public static class Builder {

        private final List<PlaceholderResolver>     resolvers     = new ArrayList<>();
        private final Map<String, Supplier<String>> directValues  = new LinkedHashMap<>();
        private final List<UnaryOperator<String>>   postReplacers = new ArrayList<>();

        private Builder() {}

        @NotNull
        public PlaceholderContext build() {
            this.resolvers.add(key -> {
                var supplier = this.directValues.get(key);
                return supplier == null ? null : supplier.get();
            });

            return new PlaceholderContext(this.resolvers, this.postReplacers);
        }

        @NotNull
        public <T> Builder with(@NotNull TypedPlaceholder<T> placeholder, @NotNull T source) {
            this.resolvers.add(placeholder.resolver(source));
            return this;
        }

        @NotNull
        public Builder with(@NotNull PlaceholderResolver resolver) {
            this.resolvers.add(resolver);
            return this;
        }

        @NotNull
        public Builder with(@NotNull Map<String, String> staticValues) {
            this.resolvers.add(staticValues::get);
            return this;
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
