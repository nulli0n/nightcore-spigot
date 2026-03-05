package su.nightexpress.nightcore.bridge.placeholder;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface PlaceholderHandler {

    @Nullable String handle(@NonNull Player player, @NonNull String payload);
}