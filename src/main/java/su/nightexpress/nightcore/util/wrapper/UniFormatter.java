package su.nightexpress.nightcore.util.wrapper;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class UniFormatter implements Writeable {

    private final DecimalFormat formatter;

    private final String       format;
    private final RoundingMode rounding;
    private final Locale       locale;

    private UniFormatter(@NotNull String format, @NotNull RoundingMode rounding, @NotNull Locale locale) {
        this.format = format;
        this.rounding = rounding;
        this.locale = locale;
        this.formatter = new DecimalFormat(format, new DecimalFormatSymbols(locale));
        this.formatter.setRoundingMode(rounding);
    }

    @NotNull
    public static UniFormatter of(@NotNull String format, @NotNull RoundingMode rounding) {
        return of(format, rounding, Locale.US);
    }

    @NotNull
    public static UniFormatter of(@NotNull String format, @NotNull RoundingMode rounding, @NotNull Locale locale) {
        return new UniFormatter(format, rounding, locale);
    }

    @NotNull
    public static UniFormatter read(@NotNull FileConfig config, @NotNull String path) {
        String format = ConfigValue.create(path + ".Format", "#,###.##").read(config);
        RoundingMode rounding = ConfigValue.create(path + ".Rounding", RoundingMode.class, RoundingMode.HALF_EVEN).read(config);
        String lang = ConfigValue.create(path + ".Locale", Locale.US.toLanguageTag()).read(config);
        Locale locale = Locale.forLanguageTag(lang);

        return of(format, rounding, locale);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Format", this.format);
        config.set(path + ".Rounding", this.rounding.name());
        config.set(path + ".Locale", this.locale.toLanguageTag());
    }

    @NotNull
    public String format(double value) {
        return this.formatter.format(value);
    }

    @NotNull
    public String getFormat() {
        return this.format;
    }

    @NotNull
    public RoundingMode getRounding() {
        return this.rounding;
    }

    @NotNull
    public Locale getLocale() {
        return this.locale;
    }
}
