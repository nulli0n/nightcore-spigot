package su.nightexpress.nightcore.bridge.dialog.response;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;

public class DialogClickResult {

    private final Player         player;
    private final NamespacedKey  identifier;
    private final NightNbtHolder nbtHolder;

    public DialogClickResult(@NonNull Player player, @NonNull NamespacedKey identifier,
                             @Nullable NightNbtHolder nbtHolder) {
        this.player = player;
        this.identifier = identifier;
        this.nbtHolder = nbtHolder;
    }

    @NonNull
    public Player getPlayer() {
        return this.player;
    }

    @NonNull
    public NamespacedKey getIdentifier() {
        return this.identifier;
    }

    @Nullable
    public NightNbtHolder getNbtHolder() {
        return this.nbtHolder;
    }
}
