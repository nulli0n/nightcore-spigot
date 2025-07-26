package su.nightexpress.nightcore.bridge.dialog.adapter;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;

public interface DialogAdapter<D> {

    @NotNull D adaptDialog(@NotNull WrappedDialog dialog);
}
