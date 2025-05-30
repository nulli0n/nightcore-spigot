package su.nightexpress.nightcore.util.number;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.core.CoreConfig;

public class NumberShortcut implements Writeable {

    private final int    magnitude;
    private final String symbol;

    public NumberShortcut(int magnitude, @NotNull String symbol) {
        this.magnitude = magnitude;
        this.symbol = symbol;
    }

    @NotNull
    public static NumberShortcut read(@NotNull FileConfig config, @NotNull String path) {
        int magnitude = config.getInt(path + ".Magnitude");
        String name = config.getString(path + ".Name", String.valueOf(magnitude));

        return new NumberShortcut(magnitude, name);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Magnitude", this.magnitude);
        config.set(path + ".Name", this.symbol);
    }

    public int getMagnitude() {
        return this.magnitude;
    }

    public double getMultiplier() {
        return Math.pow(CoreConfig.NUMBER_SHORTCUT_STEP.get(), this.magnitude);
    }

    @NotNull
    public String getSymbol() {
        return this.symbol;
    }

    @Override
    public String toString() {
        return "NumberShortcut{" +
            "magnitude=" + magnitude +
            ", symbol='" + symbol + '\'' +
            '}';
    }
}
