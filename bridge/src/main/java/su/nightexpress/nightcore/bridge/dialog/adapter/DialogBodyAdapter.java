package su.nightexpress.nightcore.bridge.dialog.adapter;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedItemDialogBody;
import su.nightexpress.nightcore.bridge.dialog.wrap.body.WrappedPlainMessageDialogBody;

public interface DialogBodyAdapter<D> {

    @NotNull D adaptBody(@NotNull WrappedDialogBody body);

    @NotNull D adaptBody(@NotNull WrappedPlainMessageDialogBody body);

    @NotNull D adaptBody(@NotNull WrappedItemDialogBody body);
}
