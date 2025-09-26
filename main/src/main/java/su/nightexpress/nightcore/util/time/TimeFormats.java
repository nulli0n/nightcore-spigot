package su.nightexpress.nightcore.util.time;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.TimeUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TimeFormats {

    private static final DateTimeFormatter DIGITAL_FULL_FORMATTER  = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DIGITAL_SHORT_FORMATTER = DateTimeFormatter.ofPattern("mm:ss");

    private static final Map<TimeUnit, TextLocale> LITERAL_TIME_UNITS = new LinkedHashMap<>();

    static {
        LITERAL_TIME_UNITS.put(TimeUnit.DAYS, CoreLang.TIME_LABEL_DAY);
        LITERAL_TIME_UNITS.put(TimeUnit.HOURS, CoreLang.TIME_LABEL_HOUR);
        LITERAL_TIME_UNITS.put(TimeUnit.MINUTES, CoreLang.TIME_LABEL_MINUTE);
        LITERAL_TIME_UNITS.put(TimeUnit.SECONDS, CoreLang.TIME_LABEL_SECOND);
    }

    private static DateTimeFormatter dateTimeFormatter;

    public static void setDateTimeFormatter(@NotNull String pattern) {
        TimeFormats.dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    @NotNull
    public static String formatDateTime(long millis) {
        return formatDateTime(TimeUtil.getLocalDateTimeOf(millis));
    }

    @NotNull
    public static String formatDateTime(@NotNull LocalDateTime time) {
        return dateTimeFormatter.format(time);
    }

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
    public static String toSeconds(long millis) {
        long whole = TimeUnit.MILLISECONDS.toSeconds(millis);

        return whole > 0 ? String.valueOf(whole) : NumberUtil.format(millis / 1000D);
    }

    @NotNull
    public static String toDigital(long millis) {
        return DIGITAL_FULL_FORMATTER.format(TimeUtil.getLocalTimeOf(millis));
    }

    @NotNull
    public static String toDigitalShort(long millis) {
        return DIGITAL_SHORT_FORMATTER.format(TimeUtil.getLocalTimeOf(millis));
    }

    @NotNull
    @Deprecated
    public static String toFormat(long millis, @NotNull DateTimeFormatter formatter) {
        return formatter.format(TimeUtil.getLocalTimeOf(millis));
        //return TimeUtil.getLocalTimeOf(millis).format(formatter);
    }



    @NotNull
    public static String toLiteral(long millis) {
        StringBuilder str = new StringBuilder();
        String delimiter = CoreLang.TIME_DELIMITER.text();

        long remaining = millis;
        for (Map.Entry<TimeUnit, TextLocale> entry : LITERAL_TIME_UNITS.entrySet()) {
            TimeUnit unit = entry.getKey();
            String label = entry.getValue().text();

            long value = unit.convert(remaining, TimeUnit.MILLISECONDS);
            if (value > 0 || (millis <= 1000L && unit == TimeUnit.SECONDS)) {
                if (!str.isEmpty()) {
                    str.append(delimiter);
                }
                str.append(label.formatted(String.valueOf(value)));
                remaining -= unit.toMillis(value);
            }
        }

        return str.toString().trim();
    }
}
