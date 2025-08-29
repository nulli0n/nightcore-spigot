package su.nightexpress.nightcore.commands.builder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.commands.NodeExecutor;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.locale.entry.TextLocale;

public abstract class ExecutableNodeBuilder<N extends ExecutableNode, B extends ExecutableNodeBuilder<N, B>> extends NodeBuilder<B> {

    protected final String name;

    protected String description;
    protected NodeExecutor executor;

    protected ExecutableNodeBuilder(@NotNull String name) {
        this.name = name;
        this.description = "";
    }

    @Override
    @NotNull
    protected abstract B getThis();

    @NotNull
    public abstract N build();

    @NotNull
    public B description(@NotNull TextLocale locale) {
        return this.description(locale.text());
    }

    @NotNull
    public B description(@NotNull String description) {
        this.description = description;
        return this.getThis();
    }

    @NotNull
    public B executes(@Nullable NodeExecutor executor) {
        this.executor = executor;
        return this.getThis();
    }

    @NotNull
    public NodeExecutor getExecutor() {
        return this.executor;
    }

    @NotNull
    public String getName() {
        return this.name;
    }
}
