package su.nightexpress.nightcore.util.sound;

import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundGroup;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.BukkitThing;

public class VanillaSound extends AbstractSound {

    private final Sound sound;

    public VanillaSound(@NonNull Sound sound, float volume, float pitch) {
        super(volume, pitch);
        this.sound = sound;
    }

    @NonNull
    public static VanillaSound of(@NonNull SoundGroup group, @NonNull Function<SoundGroup, Sound> function) {
        return of(function.apply(group), group.getVolume(), group.getPitch());
    }

    @NonNull
    public static VanillaSound of(@NonNull Sound sound) {
        return of(sound, DEFAULT_VOLUME);
    }

    @NonNull
    public static VanillaSound of(@NonNull Sound sound, float volume) {
        return of(sound, volume, DEFAULT_PITCH);
    }

    @NonNull
    public static VanillaSound of(@NonNull Sound sound, float volume, float pitch) {
        return new VanillaSound(sound, volume, pitch);
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
        return BukkitThing.getAsString(this.sound);
    }

    @NonNull
    public Sound getSound() {
        return this.sound;
    }
}
