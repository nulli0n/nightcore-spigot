package su.nightexpress.nightcore.command.experimental.builder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.command.experimental.flag.CommandFlag;
import su.nightexpress.nightcore.util.wrapper.UniPermission;

public abstract class FlagBuilder<F extends CommandFlag, B extends FlagBuilder<F, B>> {

    protected final String name;

    protected String permission;

    public FlagBuilder(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    protected abstract FlagBuilder<F, B> getThis();

    @NotNull
    public FlagBuilder<F, B> permission(@NotNull UniPermission permission) {
        return this.permission(permission.getName());
    }

    @NotNull
    public FlagBuilder<F, B> permission(@Nullable String permission) {
        this.permission = permission;
        return this.getThis();
    }

    @NotNull
    public abstract F build();
}
