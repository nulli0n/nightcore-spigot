package su.nightexpress.nightcore.bridge.dialog.response;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;

public class DialogClickResult {

    private final Player         player;
    private final NamespacedKey  identifier;
    private final NightNbtHolder nbtHolder;

    public DialogClickResult(@NotNull Player player, @NotNull NamespacedKey identifier, @Nullable NightNbtHolder nbtHolder) {
        this.player = player;
        this.identifier = identifier;
        this.nbtHolder = nbtHolder;
    }

    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    @NotNull
    public NamespacedKey getIdentifier() {
        return this.identifier;
    }

    @Nullable
    public NightNbtHolder getNbtHolder() {
        return this.nbtHolder;
    }
}
