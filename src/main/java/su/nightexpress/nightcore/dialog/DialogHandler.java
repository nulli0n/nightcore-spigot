package su.nightexpress.nightcore.dialog;

import org.jetbrains.annotations.NotNull;

public interface DialogHandler {

    boolean onInput(@NotNull WrappedInput input);
}
