package su.nightexpress.nightcore.util.wrapper;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.random.Rnd;

public final class UniInt implements Writeable {

    private final int minInclusive;
    private final int maxInclusive;

    private UniInt(int min, int max) {
        this.minInclusive = min;
        this.maxInclusive = max;
    }

    @NonNull
    public static UniInt of(int var0, int var1) {
        return new UniInt(var0, var1);
    }

    @NonNull
    public static UniInt read(@NonNull FileConfig config, @NonNull String path) {
        int min = config.getInt(path + ".Min");
        int max = config.getInt(path + ".Max");
        return of(min, max);
    }

    @Override
    public void write(@NonNull FileConfig config, @NonNull String path) {
        config.set(path + ".Min", this.getMinValue());
        config.set(path + ".Max", this.getMaxValue());
    }

    public int roll() {
        return Rnd.get(this.minInclusive, this.maxInclusive);
    }

    public int getMinValue() {
        return this.minInclusive;
    }

    public int getMaxValue() {
        return this.maxInclusive;
    }

    @Override
    public String toString() {
        return "[" + this.minInclusive + "-" + this.maxInclusive + "]";
    }
}
