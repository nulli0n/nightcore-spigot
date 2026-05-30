package su.nightexpress.nightcore.bridge.dialog.response;

import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.DialogViewer;

public interface DialogResponseHandler {

    void handle(@NonNull DialogViewer viewer, @NonNull NamespacedKey identifier, @Nullable NightNbtHolder nbtHolder);
}
