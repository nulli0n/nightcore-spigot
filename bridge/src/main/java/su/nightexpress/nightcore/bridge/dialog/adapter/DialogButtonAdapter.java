package su.nightexpress.nightcore.bridge.dialog.adapter;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;

public interface DialogButtonAdapter<B> {

    @NotNull B adaptButton(@NotNull WrappedActionButton button);
}
