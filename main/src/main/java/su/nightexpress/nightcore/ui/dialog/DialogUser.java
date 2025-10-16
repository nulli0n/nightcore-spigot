package su.nightexpress.nightcore.ui.dialog;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.DialogViewer;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.util.Players;

public class DialogUser implements DialogViewer {

    private final Player        player;
    private final WrappedDialog dialog;
    private final Runnable      callback;

    public DialogUser(@NotNull Player player, @NotNull WrappedDialog dialog, @Nullable Runnable callback) {
        this.player = player;
        this.dialog = dialog;
        this.callback = callback;
    }

    @Override
    public void close() {
        Players.closeDialog(this.player);
    }

    @Override
    public void closeFully() {
        this.close();
        this.callback();
    }

    @Override
    public void callback() {
        if (this.callback != null) {
            this.callback.run();
        }
    }

    @Override
    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    @Override
    @NotNull
    public WrappedDialog getDialog() {
        return this.dialog;
    }

    @Override
    @Nullable
    public Runnable getCallback() {
        return this.callback;
    }
}
