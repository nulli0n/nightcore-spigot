package su.nightexpress.nightcore.bridge.dialog.adapter;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;

public interface DialogAdapter<D> {

    @NonNull
    D adaptDialog(@NonNull WrappedDialog dialog);
}
