package su.nightexpress.nightcore.util.sound;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.util.Strings;

public class CustomSound extends AbstractSound {

    private final String sound;

    public CustomSound(@NotNull String sound, float volume, float pitch) {
        super(volume, pitch);
        this.sound = Strings.varStyle(sound, NightKey::allowedInValue).orElse("null");
    }

    @NotNull
    public static CustomSound of(@NotNull String sound) {
        return of(sound, DEFAULT_VOLUME);
    }

    @NotNull
    public static CustomSound of(@NotNull String sound, float volume) {
        return of(sound, volume, DEFAULT_PITCH);
    }

    @NotNull
    public static CustomSound of(@NotNull String sound, float volume, float pitch) {
        return new CustomSound(sound, volume, pitch);
    }

    @Override
    public void play(@NotNull Player player) {
        if (this.isSilent()) return;

        Location location = player.getLocation();
        player.playSound(location, this.sound, this.volume, this.pitch);
    }

    @Override
    public void play(@NotNull Location location) {
        if (this.isSilent()) return;

        World world = location.getWorld();
        if (world == null) return;

        world.playSound(location, this.sound, this.volume, this.pitch);
    }

    @Override
    @NotNull
    public String getName() {
        return this.sound;
    }
}
