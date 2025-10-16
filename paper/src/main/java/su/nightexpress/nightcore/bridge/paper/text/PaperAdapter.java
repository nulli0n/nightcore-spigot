package su.nightexpress.nightcore.bridge.paper.text;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.common.NightKey;

public abstract class PaperAdapter {

    @NotNull
    protected Key adaptKey(@NotNull NightKey key) {
        return Key.key(key.namespace(), key.value());
    }
}
