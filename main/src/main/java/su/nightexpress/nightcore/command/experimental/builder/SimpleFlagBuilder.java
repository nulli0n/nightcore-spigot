package su.nightexpress.nightcore.command.experimental.builder;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.command.experimental.flag.SimpleFlag;

public class SimpleFlagBuilder extends FlagBuilder<SimpleFlag, SimpleFlagBuilder> {

    public SimpleFlagBuilder(@NotNull String name) {
        super(name);
    }

    @Override
    @NotNull
    protected SimpleFlagBuilder getThis() {
        return this;
    }

    @Override
    @NotNull
    public SimpleFlag build() {
        return new SimpleFlag(this.name, this.permission);
    }
}
