package su.nightexpress.nightcore.dialog;

import org.jspecify.annotations.NonNull;

@Deprecated
public interface DialogHandler {

    boolean onInput(@NonNull Dialog dialog, @NonNull WrappedInput input);
}
