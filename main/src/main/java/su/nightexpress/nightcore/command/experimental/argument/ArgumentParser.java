package su.nightexpress.nightcore.command.experimental.argument;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.command.experimental.CommandContext;

@Deprecated
public interface ArgumentParser<T> {

    @Nullable
    T parse(@NonNull String string, @NonNull CommandContext context);
}
