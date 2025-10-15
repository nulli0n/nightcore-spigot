package su.nightexpress.nightcore.bridge.dialog.wrap.input;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogInputAdapter;

import java.util.function.UnaryOperator;

public interface WrappedDialogInput {

    @NotNull String key();

    @NotNull <I> I adapt(@NotNull DialogInputAdapter<I> adapter);

    @Deprecated
    @NotNull WrappedDialogInput replace(@NotNull UnaryOperator<String> operator);
}
