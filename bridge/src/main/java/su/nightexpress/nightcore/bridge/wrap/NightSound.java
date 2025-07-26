package su.nightexpress.nightcore.bridge.wrap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface NightSound {

    boolean isSilent();

    void play(@NotNull Player player);

    void play(@NotNull Location location);

    @NotNull String serialize();

    @NotNull String getName();

    float getVolume();

    float getPitch();
}
