package su.nightexpress.nightcore.commands.builder;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.commands.SuggestionsProvider;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.tree.ArgumentNode;
import su.nightexpress.nightcore.locale.entry.TextLocale;

public class ArgumentNodeBuilder<T> extends NodeBuilder<ArgumentNodeBuilder<T>> {

    private final String          name;
    private final ArgumentType<T> type;

    private String              localizedName;
    private boolean             required;
    private SuggestionsProvider suggestions;

    public ArgumentNodeBuilder(@NonNull String name, @NonNull ArgumentType<T> type) {
        this.name = name;
        this.type = type;
        this.required = true;
    }

    @Override
    @NonNull
    protected ArgumentNodeBuilder<T> getThis() {
        return this;
    }

    @NonNull
    public ArgumentNode<T> build() {
        return new ArgumentNode<>(this.name, this.type, this.permission, this.requirements, this.required, this.localizedName, this.suggestions);
    }

    @NonNull
    public ArgumentNodeBuilder<T> localized(@NonNull TextLocale locale) {
        return this.localized(locale.text());
    }

    @NonNull
    public ArgumentNodeBuilder<T> localized(@NonNull String localizedName) {
        this.localizedName = localizedName;
        return this.getThis();
    }

    @NonNull
    public ArgumentNodeBuilder<T> optional() {
        return this.optional(true);
    }

    @NonNull
    public ArgumentNodeBuilder<T> optional(boolean optional) {
        this.required = !optional;
        return this.getThis();
    }

    @NonNull
    public ArgumentNodeBuilder<T> suggestions(@Nullable SuggestionsProvider provider) {
        this.suggestions = provider;
        return this.getThis();
    }

    @Nullable
    public SuggestionsProvider getSuggestions() {
        return this.suggestions;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public ArgumentType<T> getType() {
        return this.type;
    }
}
