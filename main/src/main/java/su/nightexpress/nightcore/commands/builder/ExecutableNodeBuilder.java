package su.nightexpress.nightcore.commands.builder;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.commands.NodeExecutor;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.locale.entry.TextLocale;

public abstract class ExecutableNodeBuilder<N extends ExecutableNode, B extends ExecutableNodeBuilder<N, B>> extends NodeBuilder<B> {

    protected final String name;

    protected String       description;
    protected NodeExecutor executor;

    protected ExecutableNodeBuilder(@NonNull String name) {
        this.name = name;
        this.description = "";
    }

    @Override
    @NonNull
    protected abstract B getThis();

    @NonNull
    public abstract N build();

    @NonNull
    public B description(@NonNull TextLocale locale) {
        return this.description(locale.text());
    }

    @NonNull
    public B description(@NonNull String description) {
        this.description = description;
        return this.getThis();
    }

    @NonNull
    public B executes(@Nullable NodeExecutor executor) {
        this.executor = executor;
        return this.getThis();
    }

    @NonNull
    public NodeExecutor getExecutor() {
        return this.executor;
    }

    @NonNull
    public String getName() {
        return this.name;
    }
}
