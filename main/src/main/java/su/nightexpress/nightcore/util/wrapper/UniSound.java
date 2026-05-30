package su.nightexpress.nightcore.util.wrapper;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.BukkitThing;

@Deprecated
public class UniSound {

    private final String soundName;
    private final Sound  soundType;
    private final float  volume;
    private final float  pitch;

    public UniSound(@NonNull String soundName, @Nullable Sound soundType, float volume, float pitch) {
        this.soundName = soundName.toLowerCase();
        this.soundType = soundType;
        this.volume = volume;
        this.pitch = pitch;
    }

    @NonNull
    public static UniSound of(@NonNull Sound sound) {
        return of(sound, 0.8F);
    }

    @NonNull
    public static UniSound of(@NonNull Sound sound, float volume) {
        return of(sound, volume, 1F);
    }

    @NonNull
    public static UniSound of(@NonNull Sound sound, float volume, float pitch) {
        return new UniSound(BukkitThing.toString(sound), sound, volume, pitch);
    }

    @NonNull
    public static UniSound read(@NonNull FileConfig cfg, @NonNull String path) {
        String soundName = ConfigValue.create(path + ".Name", "null",
            "Sound name. You can use Spigot sound names, or ones from your resource pack.",
            "Spigot Sounds: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html"
        ).read(cfg);

        float volume = ConfigValue.create(path + ".Volume", 0.8D,
            "Sound volume. From 0.0 to 1.0."
        ).read(cfg).floatValue();

        float pitch = ConfigValue.create(path + ".Pitch", 1D,
            "Sound speed. From 0.5 to 2.0"
        ).read(cfg).floatValue();

        Sound soundType = BukkitThing.getSound(soundName);//StringUtil.getEnum(soundName, Sound.class).orElse(null);

        return new UniSound(soundName, soundType, volume, pitch);
    }

    public void write(@NonNull FileConfig cfg, @NonNull String path) {
        cfg.set(path + ".Name", this.getSoundName());
        cfg.set(path + ".Volume", this.getVolume());
        cfg.set(path + ".Pitch", this.getPitch());
    }

    public boolean isEmpty() {
        return this.getVolume() <= 0F || this.getSoundName().isEmpty();
    }

    public void play(@NonNull Player player) {
        if (this.isEmpty()) return;

        Location location = player.getLocation();
        if (this.getSoundType() == null) {
            player.playSound(location, this.getSoundName(), this.getVolume(), this.getPitch());
        }
        else {
            player.playSound(location, this.getSoundType(), this.getVolume(), this.getPitch());
        }
    }

    public void play(@NonNull Location location) {
        if (this.isEmpty()) return;

        World world = location.getWorld();
        if (world == null) return;

        if (this.getSoundType() == null) {
            world.playSound(location, this.getSoundName(), this.getVolume(), this.getPitch());
        }
        else {
            world.playSound(location, this.getSoundType(), this.getVolume(), this.getPitch());
        }
    }

    @NonNull
    public String getSoundName() {
        return soundName;
    }

    @Nullable
    public Sound getSoundType() {
        return soundType;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}
