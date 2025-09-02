package su.nightexpress.nightcore.commands.builder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    public ArgumentNodeBuilder(@NotNull String name, @NotNull ArgumentType<T> type) {
        this.name = name;
        this.type = type;
        this.required = true;
    }

    @Override
    @NotNull
    protected ArgumentNodeBuilder<T> getThis() {
        return this;
    }

    @NotNull
    public ArgumentNode<T> build() {
        return new ArgumentNode<>(this.name, this.type, this.permission, this.requirements, this.required, this.localizedName, this.suggestions);
    }

    @NotNull
    public ArgumentNodeBuilder<T> localized(@NotNull TextLocale locale) {
        return this.localized(locale.text());
    }

    @NotNull
    public ArgumentNodeBuilder<T> localized(@NotNull String localizedName) {
        this.localizedName = localizedName;
        return this.getThis();
    }

    @NotNull
    public ArgumentNodeBuilder<T> optional() {
        this.required = false;
        return this.getThis();
    }

    @NotNull
    public ArgumentNodeBuilder<T> suggestions(@Nullable SuggestionsProvider provider) {
        this.suggestions = provider;
        return this.getThis();
    }

    @Nullable
    public SuggestionsProvider getSuggestions() {
        return this.suggestions;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public ArgumentType<T> getType() {
        return this.type;
    }
}
