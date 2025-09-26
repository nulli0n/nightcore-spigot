package su.nightexpress.nightcore.bridge.dialog.response;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;

public interface DialogResponseHandler {

    void handle(@NotNull Player user, @NotNull NamespacedKey identifier, @Nullable NightNbtHolder nbtHolder);
}
