package su.nightexpress.nightcore.command.experimental.builder;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.command.experimental.flag.SimpleFlag;

@Deprecated
public class SimpleFlagBuilder extends FlagBuilder<SimpleFlag, SimpleFlagBuilder> {

    public SimpleFlagBuilder(@NonNull String name) {
        super(name);
    }

    @Override
    @NonNull
    protected SimpleFlagBuilder getThis() {
        return this;
    }

    @Override
    @NonNull
    public SimpleFlag build() {
        return new SimpleFlag(this.name, this.permission);
    }
}
