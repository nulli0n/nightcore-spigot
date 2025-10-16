package su.nightexpress.nightcore.bridge.dialog.response;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.DialogViewer;

public interface DialogResponseHandler {

    void handle(@NotNull DialogViewer viewer, @NotNull NamespacedKey identifier, @Nullable NightNbtHolder nbtHolder);
}
