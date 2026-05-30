package su.nightexpress.nightcore.bridge.dialog;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;

public interface DialogViewer {

    void close();

    void closeFully();

    void callback();

    @NonNull
    Player getPlayer();

    @NonNull
    WrappedDialog getDialog();

    @Nullable
    Runnable getCallback();
}
