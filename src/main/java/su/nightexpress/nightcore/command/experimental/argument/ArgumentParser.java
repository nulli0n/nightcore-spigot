package su.nightexpress.nightcore.command.experimental.argument;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.command.experimental.CommandContext;

public interface ArgumentParser<T> {

    @Nullable T parse(@NotNull String string, @NotNull CommandContext context);
}
