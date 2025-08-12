package su.nightexpress.nightcore.bridge.dialog.wrap.input;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.adapter.DialogInputAdapter;

public interface WrappedDialogInput {

    @NotNull String key();

    @NotNull <I> I adapt(@NotNull DialogInputAdapter<I> adapter);
}
