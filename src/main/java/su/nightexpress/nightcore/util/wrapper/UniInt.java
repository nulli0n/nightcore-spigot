package su.nightexpress.nightcore.util.wrapper;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.random.Rnd;

public final class UniInt {

    private final int minInclusive;
    private final int maxInclusive;

    private UniInt(int min, int max) {
        this.minInclusive = min;
        this.maxInclusive = max;
    }

    @NotNull
    public static UniInt of(int var0, int var1) {
        return new UniInt(var0, var1);
    }

    @NotNull
    public static UniInt read(@NotNull FileConfig config, @NotNull String path) {
        int min = config.getInt(path + ".Min");
        int max = config.getInt(path + ".Max");
        return of(min, max);
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
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
