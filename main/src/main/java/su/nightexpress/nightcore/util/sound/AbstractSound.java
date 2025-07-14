package su.nightexpress.nightcore.util.sound;

import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.wrap.NightSound;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.NumberUtil;

public abstract class AbstractSound implements NightSound, Writeable {

    protected static final String DELIMITER = ";";

    protected static final float MIN_PITCH  = 0.5f;
    protected static final float MAX_PITCH  = 2.0f;
    protected static final float MIN_VOLUME = 0f;
    protected static final float MAX_VOLUME = 1.0f;

    protected static final float DEFAULT_VOLUME = 0.8f;
    protected static final float DEFAULT_PITCH = 1f;

    protected final float  volume;
    protected final float  pitch;

    public AbstractSound(float volume, float pitch) {
        this.volume = NumberUtil.clamp(volume, MIN_VOLUME, MAX_VOLUME);
        this.pitch = NumberUtil.clamp(pitch, MIN_PITCH, MAX_PITCH);
    }

    @NotNull
    public static NightSound deserialize(@NotNull String from) {
        String[] split = from.split(DELIMITER);

        String name = split[0];
        float volume = split.length >= 2 ? (float) NumberUtil.getDoubleAbs(split[1], MAX_VOLUME) : MAX_VOLUME;
        float pitch = split.length >= 3 ? (float) NumberUtil.getDoubleAbs(split[2], 1f) : 1f;

        Sound bukkit = BukkitThing.getSound(name);
        return bukkit == null ? CustomSound.of(name, volume, pitch) : VanillaSound.of(bukkit, volume, pitch);
    }

    @NotNull
    public String serialize() {
        return this.getName() + DELIMITER + this.volume + DELIMITER + this.pitch;
    }

    @Nullable
    public static NightSound read(@NotNull FileConfig config, @NotNull String path) {
        String raw = config.getString(path);
        if (raw == null) return null;

        return deserialize(raw);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path, this.serialize());
    }

    @Override
    public boolean isSilent() {
        return this.volume <= 0f;
    }

    @Override
    public float getVolume() {
        return this.volume;
    }

    @Override
    public float getPitch() {
        return this.pitch;
    }
}
