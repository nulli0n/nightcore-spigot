package su.nightexpress.nightcore.bridge.dialog;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;

public interface DialogViewer {

    void close();

    void closeFully();

    void callback();

    @NotNull Player getPlayer();

    @NotNull WrappedDialog getDialog();

    @Nullable Runnable getCallback();
}
