package su.nightexpress.nightcore.util.time;

import org.jspecify.annotations.NonNull;
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

    public static void setDateTimeFormatter(@NonNull String pattern) {
        TimeFormats.dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    @NonNull
    public static String formatDateTime(long millis) {
        return formatDateTime(TimeUtil.getLocalDateTimeOf(millis));
    }

    @NonNull
    public static String formatDateTime(@NonNull LocalDateTime time) {
        return dateTimeFormatter.format(time);
    }

    @NonNull
    public static String formatDuration(long until, @NonNull TimeFormatType formatType) {
        return formatAmount(until - System.currentTimeMillis(), formatType);
    }

    @NonNull
    public static String formatSince(long since, @NonNull TimeFormatType formatType) {
        return formatAmount(System.currentTimeMillis() - since, formatType);
    }

    @NonNull
    public static String formatAmount(long millis, @NonNull TimeFormatType formatType) {
        return switch (formatType) {
            case LITERAL -> toLiteral(millis);
            case DIGITAL -> toDigital(millis);
            case SECONDS -> toSeconds(millis);
        };
    }

    @NonNull
    public static String toSeconds(long millis) {
        long whole = TimeUnit.MILLISECONDS.toSeconds(millis);

        return whole > 0 ? String.valueOf(whole) : NumberUtil.format(millis / 1000D);
    }

    @NonNull
    public static String toDigital(long millis) {
        return DIGITAL_FULL_FORMATTER.format(TimeUtil.getLocalTimeOf(millis));
    }

    @NonNull
    public static String toDigitalShort(long millis) {
        return DIGITAL_SHORT_FORMATTER.format(TimeUtil.getLocalTimeOf(millis));
    }

    @NonNull
    @Deprecated
    public static String toFormat(long millis, @NonNull DateTimeFormatter formatter) {
        return formatter.format(TimeUtil.getLocalTimeOf(millis));
        //return TimeUtil.getLocalTimeOf(millis).format(formatter);
    }


    @NonNull
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
