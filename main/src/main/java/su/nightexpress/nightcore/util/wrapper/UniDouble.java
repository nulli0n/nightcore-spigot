package su.nightexpress.nightcore.util.wrapper;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.random.Rnd;

public final class UniDouble implements Writeable {

    private final double minInclusive;
    private final double maxInclusive;

    private UniDouble(double min, double max) {
        this.minInclusive = min;
        this.maxInclusive = max;
    }

    @NotNull
    public static UniDouble of(double min, double max) {
        return new UniDouble(min, max);
    }

    @NotNull
    public static UniDouble read(@NotNull FileConfig cfg, @NotNull String path) {
        double min = cfg.getDouble(path + ".Min");
        double max = cfg.getDouble(path + ".Max");
        return of(min, max);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Min", this.getMinValue());
        config.set(path + ".Max", this.getMaxValue());
    }

    @NotNull
    public UniInt asInt() {
        return UniInt.of((int) this.getMinValue(), (int) this.getMaxValue());
    }

    public double roll() {
        return Rnd.getDouble(this.minInclusive, this.maxInclusive);
    }

    public double getMinValue() {
        return this.minInclusive;
    }

    public double getMaxValue() {
        return this.maxInclusive;
    }

    @Override
    public String toString() {
        return "[" + this.minInclusive + "-" + this.maxInclusive + "]";
    }
}
