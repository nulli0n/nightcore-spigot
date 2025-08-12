package su.nightexpress.nightcore.util.sound;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.BukkitThing;

public class VanillaSound extends AbstractSound {

    private final Sound sound;

    public VanillaSound(@NotNull Sound sound, float volume, float pitch) {
        super(volume, pitch);
        this.sound = sound;
    }

    @NotNull
    public static VanillaSound of(@NotNull Sound sound) {
        return of(sound, DEFAULT_VOLUME);
    }

    @NotNull
    public static VanillaSound of(@NotNull Sound sound, float volume) {
        return of(sound, volume, DEFAULT_PITCH);
    }

    @NotNull
    public static VanillaSound of(@NotNull Sound sound, float volume, float pitch) {
        return new VanillaSound(sound, volume, pitch);
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
        return BukkitThing.getAsString(this.sound);
    }

    @NotNull
    public Sound getSound() {
        return this.sound;
    }
}
