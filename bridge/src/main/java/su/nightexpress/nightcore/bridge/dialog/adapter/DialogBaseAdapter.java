package su.nightexpress.nightcore.bridge.dialog.adapter;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.dialog.wrap.base.WrappedDialogBase;

public interface DialogBaseAdapter<B> {

    @NonNull
    B adaptBase(@NonNull WrappedDialogBase base);
}
