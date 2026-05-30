package su.nightexpress.nightcore.bridge.wrap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public interface NightSound {

    boolean isSilent();

    void play(@NonNull Player player);

    void play(@NonNull Location location);

    @NonNull
    String serialize();

    @NonNull
    String getName();

    float getVolume();

    float getPitch();
}
