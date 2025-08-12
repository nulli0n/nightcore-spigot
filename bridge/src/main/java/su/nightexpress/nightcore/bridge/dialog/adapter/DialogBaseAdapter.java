package su.nightexpress.nightcore.bridge.dialog.adapter;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.wrap.base.WrappedDialogBase;

public interface DialogBaseAdapter<B> {

    @NotNull B adaptBase(@NotNull WrappedDialogBase base);
}
