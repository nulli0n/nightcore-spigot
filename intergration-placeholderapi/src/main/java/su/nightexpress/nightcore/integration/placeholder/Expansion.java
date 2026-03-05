package su.nightexpress.nightcore.integration.placeholder;

import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public interface Expansion {

    boolean register();

    boolean unregister();

    @NonNull String getIdentifier();

    @NonNull String getAuthor();

    @NonNull String getVersion();

    boolean persist();

    @Nullable String onPlaceholderRequest(Player player, @NonNull String params);
}
