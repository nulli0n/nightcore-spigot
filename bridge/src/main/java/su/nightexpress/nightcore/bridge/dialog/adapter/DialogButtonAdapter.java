package su.nightexpress.nightcore.bridge.dialog.adapter;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;

public interface DialogButtonAdapter<B> {

    @NonNull
    B adaptButton(@NonNull WrappedActionButton button);
}
