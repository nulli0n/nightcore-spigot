package su.nightexpress.nightcore.command.experimental.builder;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.command.experimental.flag.CommandFlag;
import su.nightexpress.nightcore.util.wrapper.UniPermission;

@Deprecated
public abstract class FlagBuilder<F extends CommandFlag, B extends FlagBuilder<F, B>> {

    protected final String name;

    protected String permission;

    public FlagBuilder(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    protected abstract FlagBuilder<F, B> getThis();

    @NonNull
    public FlagBuilder<F, B> permission(@NonNull UniPermission permission) {
        return this.permission(permission.getName());
    }

    @NonNull
    public FlagBuilder<F, B> permission(@Nullable String permission) {
        this.permission = permission;
        return this.getThis();
    }

    @NonNull
    public abstract F build();
}
