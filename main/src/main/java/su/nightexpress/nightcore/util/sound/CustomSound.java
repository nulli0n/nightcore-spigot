package su.nightexpress.nightcore.util.sound;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.common.NightKey;

public class CustomSound extends AbstractSound {

    private final String sound;

    public CustomSound(@NonNull String sound, float volume, float pitch) {
        super(volume, pitch);
        this.sound = NightKey.parse(sound).map(NightKey::toString).orElse("null");
    }

    @NonNull
    public static CustomSound of(@NonNull String sound) {
        return of(sound, DEFAULT_VOLUME);
    }

    @NonNull
    public static CustomSound of(@NonNull String sound, float volume) {
        return of(sound, volume, DEFAULT_PITCH);
    }

    @NonNull
    public static CustomSound of(@NonNull String sound, float volume, float pitch) {
        return new CustomSound(sound, volume, pitch);
    }

    @Override
    public void play(@NonNull Player player) {
        if (this.isSilent()) return;

        player.playSound(player, this.sound, this.volume, this.pitch);
    }

    @Override
    public void play(@NonNull Location location) {
        if (this.isSilent()) return;

        World world = location.getWorld();
        if (world == null) return;

        world.playSound(location, this.sound, this.volume, this.pitch);
    }

    @Override
    @NonNull
    public String getName() {
        return this.sound;
    }
}
