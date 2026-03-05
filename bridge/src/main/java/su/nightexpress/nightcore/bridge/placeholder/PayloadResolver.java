package su.nightexpress.nightcore.bridge.placeholder;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface PayloadResolver<T> {

    @Nullable T resolve(@NonNull Player player, @NonNull String payload);
}
