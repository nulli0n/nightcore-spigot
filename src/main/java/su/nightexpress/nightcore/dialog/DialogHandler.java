package su.nightexpress.nightcore.dialog;

import org.jetbrains.annotations.NotNull;

@Deprecated
public interface DialogHandler {

    boolean onInput(@NotNull Dialog dialog, @NotNull WrappedInput input);
}
