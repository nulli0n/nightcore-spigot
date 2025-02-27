package su.nightexpress.nightcore.util.bukkit;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.NumberUtil;

public class NightSound implements Writeable {

    private static final String DELIMITER = ":";

    private static final float MIN_PITCH  = 0.5f;
    private static final float MAX_PITCH  = 2.0f;
    private static final float MIN_VOLUME = 0f;
    private static final float MAX_VOLUME = 1.0f;

    private final String name;
    private final Sound  bukkit;
    private final float  volume;
    private final float  pitch;

    public NightSound(@NotNull String name, @Nullable Sound bukkit, float volume, float pitch) {
        this.name = name.toLowerCase();
        this.bukkit = bukkit;

        this.volume = NumberUtil.clamp(volume, MIN_VOLUME, MAX_VOLUME);
        this.pitch = NumberUtil.clamp(pitch, MIN_PITCH, MAX_PITCH);
    }

    @NotNull
    public static NightSound of(@NotNull Sound sound) {
        return of(sound, MAX_VOLUME);
    }

    @NotNull
    public static NightSound of(@NotNull Sound sound, float volume) {
        return of(sound, volume, 1F);
    }

    @NotNull
    public static NightSound of(@NotNull Sound sound, float volume, float pitch) {
        return new NightSound(BukkitThing.toString(sound), sound, volume, pitch);
    }

    @NotNull
    public static NightSound of(@NotNull String name, float volume, float pitch) {
        Sound bukkit = BukkitThing.getSound(name);

        return new NightSound(name, bukkit, volume, pitch);
    }

    @NotNull
    public static NightSound deserialize(@NotNull String from) {
        String[] split = from.split(DELIMITER);

        String name = split[0];
        float volume = split.length >= 2 ? (float) NumberUtil.getDoubleAbs(split[1], MAX_VOLUME) : MAX_VOLUME;
        float pitch = split.length >= 3 ? (float) NumberUtil.getDoubleAbs(split[2], 1f) : 1f;

        return of(name, volume, pitch);
    }

    @NotNull
    public String serialize() {
        return this.name + DELIMITER + this.volume + DELIMITER + this.pitch;
    }

    @NotNull
    @Deprecated
    public static NightSound read(@NotNull FileConfig config, @NotNull String path) {

        if (config.contains(path + ".Name")) {
            String oldName = config.getString(path + ".Name", "null");
            float volume = (float) config.getDouble(path + ".Volume");
            float pitch = (float) config.getDouble(path + ".Putch");

            NightSound old = of(oldName, volume, pitch);
            config.remove(path);
            config.set(path, old.serialize());
        }

        return deserialize(config.getString(path, "null"));
    }

    @Override
    @Deprecated
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path, this.serialize());
    }

    public boolean isEmpty() {
        return this.volume <= 0F || this.name.isBlank();
    }

    public void play(@NotNull Player player) {
        if (this.isEmpty()) return;

        Location location = player.getLocation();
        if (this.bukkit == null) {
            player.playSound(location, this.name, this.volume, this.pitch);
        }
        else {
            player.playSound(location, this.bukkit, this.volume, this.pitch);
        }
    }

    public void play(@NotNull Location location) {
        if (this.isEmpty()) return;

        World world = location.getWorld();
        if (world == null) return;

        if (this.bukkit == null) {
            world.playSound(location, this.name, this.volume, this.pitch);
        }
        else {
            world.playSound(location, this.bukkit, this.volume, this.pitch);
        }
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @Nullable
    public Sound getBukkit() {
        return this.bukkit;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }
}
