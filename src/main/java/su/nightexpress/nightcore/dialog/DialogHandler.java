package su.nightexpress.nightcore.dialog;

import org.jetbrains.annotations.NotNull;

public interface DialogHandler {

    boolean onInput(@NotNull Dialog dialog, @NotNull WrappedInput input);
}
