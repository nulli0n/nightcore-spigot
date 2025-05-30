package su.nightexpress.nightcore.util.time;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.TimeUtil;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class TimeFormats {

    private static final DateTimeFormatter DIGITAL_FULL_FORMATTER  = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DIGITAL_SHORT_FORMATTER = DateTimeFormatter.ofPattern("mm:ss");

    @NotNull
    public static String formatDuration(long until, @NotNull TimeFormatType formatType) {
        return formatAmount(until - System.currentTimeMillis(), formatType);
    }

    @NotNull
    public static String formatSince(long since, @NotNull TimeFormatType formatType) {
        return formatAmount(System.currentTimeMillis() - since, formatType);
    }

    @NotNull
    public static String formatAmount(long millis, @NotNull TimeFormatType formatType) {
        return switch (formatType) {
            case LITERAL -> toLiteral(millis);
            case DIGITAL -> toDigital(millis);
            case SECONDS -> toSeconds(millis);
        };
    }

    @NotNull
    public static String toDigital(long millis) {
        return toFormat(millis, DIGITAL_FULL_FORMATTER);
    }

    @NotNull
    public static String toDigitalShort(long millis) {
        return toFormat(millis, DIGITAL_SHORT_FORMATTER);
    }

    @NotNull
    public static String toSeconds(long millis) {
        long whole = TimeUnit.MILLISECONDS.toSeconds(millis);

        return whole > 0 ? String.valueOf(whole) : NumberUtil.format(millis / 1000D);
    }

    @NotNull
    public static String toFormat(long millis, @NotNull DateTimeFormatter formatter) {
        return TimeUtil.getLocalTimeOf(millis).format(formatter);
    }

    @NotNull
    public static String toLiteral(long millis) {
        TimeUnit[] units = {TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS};
        long[] scales = {0, 24, 60, 60};
        LangString[] literals = {CoreLang.TIME_DAY, CoreLang.TIME_HOUR, CoreLang.TIME_MINUTE, CoreLang.TIME_SECOND};
        String delimiter = CoreLang.TIME_DELIMITER.getString();

        StringBuilder str = new StringBuilder();
        for (int index = 0; index < units.length; index++) {
            TimeUnit unit = units[index];
            long scale = scales[index];
            LangString literal = literals[index];

            long converted = unit.convert(millis, TimeUnit.MILLISECONDS);
            long result = scale != 0 ? converted % scale : converted;

            if (result > 0 || (millis <= 1000L && unit == TimeUnit.SECONDS)) {
                if (!str.isEmpty()) {
                    str.append(delimiter);
                }
                str.append(literal.getString().replace(Placeholders.GENERIC_AMOUNT, String.valueOf(result)));
            }
        }

        return str.toString();
    }
}
