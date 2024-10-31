package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.core.CoreLang;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

    @NotNull
    public static String formatTime(long time) {
        long days = TimeUnit.MILLISECONDS.toDays(time);
        long hours = TimeUnit.MILLISECONDS.toHours(time) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60;
        String delimiter = CoreLang.TIME_DELIMITER.getString();

        StringBuilder str = new StringBuilder();
        if (days > 0) {
            if (!str.isEmpty()) {
                str.append(delimiter);
            }
            str.append(CoreLang.TIME_DAY.getString().replace(Placeholders.GENERIC_AMOUNT, String.valueOf(days)));
        }
        if (hours > 0) {
            if (!str.isEmpty()) {
                str.append(delimiter);
            }
            str.append(CoreLang.TIME_HOUR.getString().replace(Placeholders.GENERIC_AMOUNT, String.valueOf(hours)));
        }
        if (minutes > 0) {
            if (!str.isEmpty()) {
                str.append(delimiter);
            }
            str.append(CoreLang.TIME_MINUTE.getString().replace(Placeholders.GENERIC_AMOUNT, String.valueOf(minutes)));
        }
        if (str.isEmpty() || seconds > 0) {
            if (!str.isEmpty()) {
                str.append(delimiter);
            }

            boolean doDecimal = seconds != 0 && seconds < 5 && str.isEmpty();
            String secondsStr = doDecimal ? NumberUtil.format((double) time / 1000D) : String.valueOf(seconds);

            str.append(CoreLang.TIME_SECOND.getString().replace(Placeholders.GENERIC_AMOUNT, secondsStr));
        }

        return str.toString();
    }

    public static boolean isPassed(long timestamp) {
        return timestamp >= 0 && System.currentTimeMillis() > timestamp;
    }

    public static long createFutureTimestamp(double seconds) {
        return seconds < 0 ? -1L : createTimestamp(Math.abs(seconds));
    }

    public static long createPastTimestamp(double seconds) {
        return createTimestamp(-Math.abs(seconds));
    }

    public static long createTimestamp(double seconds) {
        return System.currentTimeMillis() + (long) (seconds * 1000D);
    }

    public static long secondsToTicks(int seconds) {
        return seconds * 20L;
    }

    public static double ticksToSeconds(long ticks) {
        return (double) ticks / 20D;
    }

    @NotNull
    public static String formatDuration(long from, long to) {
        long time = to - from;
        return formatTime(time);
    }

    @NotNull
    public static String formatDuration(long until) {
        return formatTime(until - System.currentTimeMillis());
    }

    @NotNull
    public static LocalTime getLocalTimeOf(long ms) {
        long hours = TimeUnit.MILLISECONDS.toHours(ms) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60;

        return LocalTime.of((int) hours, (int) minutes, (int) seconds);
    }

    @NotNull
    public static LocalDateTime getLocalDateTimeOf(long ms) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), TimeZone.getDefault().toZoneId());
    }

    public static long toEpochMillis(@NotNull LocalDateTime dateTime) {
        Instant instant = dateTime.atZone(TimeZone.getDefault().toZoneId()).toInstant();
        return instant.toEpochMilli();
    }
}
