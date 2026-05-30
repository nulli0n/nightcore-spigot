package su.nightexpress.nightcore.bridge.dialog.adapter;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedItemDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedPlainMessageDialogBody;

public interface DialogBodyAdapter<D> {

    @NonNull
    D adaptBody(@NonNull WrappedDialogBody body);

    @NonNull
    D adaptBody(@NonNull WrappedPlainMessageDialogBody body);

    @NonNull
    D adaptBody(@NonNull WrappedItemDialogBody body);
}
