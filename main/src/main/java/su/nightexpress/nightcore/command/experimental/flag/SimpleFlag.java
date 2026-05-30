package su.nightexpress.nightcore.command.experimental.flag;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.command.experimental.builder.SimpleFlagBuilder;

@Deprecated
public class SimpleFlag extends CommandFlag {

    public SimpleFlag(@NonNull String name, @Nullable String permission) {
        super(name, permission);
    }

    @NonNull
    public static SimpleFlagBuilder builder(@NonNull String name) {
        return new SimpleFlagBuilder(name);
    }
}
