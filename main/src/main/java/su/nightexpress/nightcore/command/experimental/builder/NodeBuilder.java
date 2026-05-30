package su.nightexpress.nightcore.command.experimental.builder;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.command.experimental.node.CommandNode;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.util.wrapper.UniPermission;

import java.util.stream.Stream;

@Deprecated
public abstract class NodeBuilder<S extends CommandNode, B extends NodeBuilder<S, B>> {

    protected final NightCorePlugin plugin;
    protected final String          name;

    protected String[] aliases;
    protected String   description;
    protected String   permission;
    protected boolean  playerOnly;

    public NodeBuilder(@NonNull NightCorePlugin plugin, @NonNull String... aliases) {
        this.plugin = plugin;
        this.name = aliases[0];
        this.aliases = Stream.of(aliases).skip(1).toArray(String[]::new);
        this.description = "";
        this.permission = null;
        this.playerOnly = false;
    }

    @NonNull
    protected abstract B getThis();

    @NonNull
    public abstract S build();

    @NonNull
    public B aliases(@NonNull String... aliases) {
        this.aliases = aliases;
        return this.getThis();
    }

    @NonNull
    public B description(@NonNull LangString description) {
        return this.description(description.getString());
    }

    @NonNull
    public B description(@NonNull String description) {
        this.description = description;
        return this.getThis();
    }

    @NonNull
    public B permission(@NonNull UniPermission permission) {
        return this.permission(permission.getName());
    }

    @NonNull
    public B permission(@Nullable String permission) {
        this.permission = permission;
        return this.getThis();
    }

    @NonNull
    public B playerOnly() {
        this.playerOnly = true;
        return this.getThis();
    }
}
