package su.nightexpress.nightcore.util.wrapper;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class UniFormatter {

    private final DecimalFormat formatter;

    private final String format;
    private final RoundingMode rounding;

    private UniFormatter(@NotNull String format, @NotNull RoundingMode rounding) {
        this.format = format;
        this.rounding = rounding;
        this.formatter = new DecimalFormat(format, new DecimalFormatSymbols(Locale.US));
        this.formatter.setRoundingMode(rounding);
    }

    @NotNull
    public static UniFormatter of(@NotNull String format, @NotNull RoundingMode rounding) {
        return new UniFormatter(format, rounding);
    }

    @NotNull
    public static UniFormatter read(@NotNull FileConfig config, @NotNull String path) {
        String format = ConfigValue.create(path + ".Format", "#,###.##").read(config);
        RoundingMode rounding = ConfigValue.create(path + ".Rounding", RoundingMode.class, RoundingMode.HALF_EVEN).read(config);

        return of(format, rounding);
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Format", this.getFormat());
        config.set(path + ".Rounding", this.getRounding().name().toLowerCase());
    }

    public String getFormat() {
        return this.format;
    }

    public RoundingMode getRounding() {
        return this.rounding;
    }

    public String format(double value) {
        return this.formatter.format(value);
    }
}
