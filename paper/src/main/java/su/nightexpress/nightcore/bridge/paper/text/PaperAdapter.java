package su.nightexpress.nightcore.bridge.paper.text;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.common.NightKey;

public abstract class PaperAdapter {

    @NonNull
    protected Key adaptKey(@NonNull NightKey key) {
        return Key.key(key.namespace(), key.value());
    }
}
