package su.nightexpress.nightcore.util.bukkit;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.StringUtil;

@Deprecated
public class NightSound implements Writeable {

    private static final String DELIMITER = ";";

    private static final float MIN_PITCH  = 0.5f;
    private static final float MAX_PITCH  = 2.0f;
    private static final float MIN_VOLUME = 0f;
    private static final float MAX_VOLUME = 1.0f;

    private final String name;
    private final Sound  bukkit;
    private final float  volume;
    private final float  pitch;

    public NightSound(@NonNull String name, @Nullable Sound bukkit, float volume, float pitch) {
        this.name = name.toLowerCase();
        this.bukkit = bukkit;

        this.volume = NumberUtil.clamp(volume, MIN_VOLUME, MAX_VOLUME);
        this.pitch = NumberUtil.clamp(pitch, MIN_PITCH, MAX_PITCH);
    }

    @NonNull
    public static NightSound of(@NonNull Sound sound) {
        return of(sound, MAX_VOLUME);
    }

    @NonNull
    public static NightSound of(@NonNull Sound sound, float volume) {
        return of(sound, volume, 1F);
    }

    @NonNull
    public static NightSound of(@NonNull Sound sound, float volume, float pitch) {
        return new NightSound(BukkitThing.toString(sound), sound, volume, pitch);
    }

    @NonNull
    public static NightSound of(@NonNull String name, float volume, float pitch) {
        Sound bukkit = BukkitThing.getSound(StringUtil.transformForID(name));

        return new NightSound(name, bukkit, volume, pitch);
    }

    @NonNull
    public static NightSound deserialize(@NonNull String from) {
        String[] split = fix(from).split(DELIMITER);

        String name = split[0];
        float volume = split.length >= 2 ? (float) NumberUtil.getDoubleAbs(split[1], MAX_VOLUME) : MAX_VOLUME;
        float pitch = split.length >= 3 ? (float) NumberUtil.getDoubleAbs(split[2], 1f) : 1f;

        return of(name, volume, pitch);
    }

    @NonNull
    public String serialize() {
        return this.name + DELIMITER + this.volume + DELIMITER + this.pitch;
    }

    @NonNull
    public static NightSound read(@NonNull FileConfig config, @NonNull String path) {
        if (config.contains(path + ".Name")) {
            String oldName = config.getString(path + ".Name", "null");
            float volume = (float) config.getDouble(path + ".Volume");
            float pitch = (float) config.getDouble(path + ".Putch");

            NightSound old = of(oldName, volume, pitch);
            config.remove(path);
            config.set(path, old.serialize());
        }

        String raw = config.getString(path, "null");

        return deserialize(raw);
    }

    private static String fix(String raw) {
        String[] split = raw.split(":");
        if (split.length == 4) {
            raw = split[0] + ":" + split[1] + DELIMITER + split[2] + DELIMITER + split[3];
        }
        else raw = raw.replace(":", DELIMITER);

        return raw;
    }

    @Override
    public void write(@NonNull FileConfig config, @NonNull String path) {
        config.set(path, this.serialize());
    }

    public boolean isEmpty() {
        return this.volume <= 0F || this.name.isBlank();
    }

    public void play(@NonNull Player player) {
        if (this.isEmpty()) return;

        Location location = player.getLocation();
        if (this.bukkit == null) {
            player.playSound(location, this.name, this.volume, this.pitch);
        }
        else {
            player.playSound(location, this.bukkit, this.volume, this.pitch);
        }
    }

    public void play(@NonNull Location location) {
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

    @NonNull
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
