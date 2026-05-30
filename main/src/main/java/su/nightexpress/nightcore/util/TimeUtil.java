package su.nightexpress.nightcore.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.time.TimeFormatType;
import su.nightexpress.nightcore.util.time.TimeFormats;

public class TimeUtil {

    private static TimeZone timeZone;

    public static void setTimeZone(@NonNull String name) {
        timeZone = TimeZone.getTimeZone(name);
    }

    @NonNull
    public static TimeZone getTimeZone() {
        return timeZone;
    }

    @NonNull
    public static ZoneId getZoneId() {
        return timeZone.toZoneId();
    }

    @NonNull
    @Deprecated
    public static String formatTime(long time) {
        return TimeFormats.toLiteral(time);
    }

    /**
     * Checks whether specified timestamp is positive and is passed (timestamp < System.currentTimeMillis()).
     *
     * @param timestamp Timestamp to check.
     * @return True if passed.
     */
    public static boolean isPassed(long timestamp) {
        return timestamp >= 0 && System.currentTimeMillis() > timestamp;
    }

    public static long createFutureTimestamp(double seconds) {
        return seconds < 0 ? -1L : createTimestamp(Math.abs(seconds));
    }

    public static long createFutureTimestamp(long duration, @NonNull TimeUnit timeUnit) {
        if (duration < 0L) return -1L;

        long millis = TimeUnit.MILLISECONDS.convert(duration, timeUnit);
        return System.currentTimeMillis() + millis;
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
        return ticks / 20D;
    }

    @NonNull
    @Deprecated
    public static String formatDuration(long from, long to) {
        long time = to - from;
        //return formatTime(time);
        return TimeFormats.toLiteral(time);
    }

    @NonNull
    @Deprecated
    public static String formatDuration(long until) {
        return TimeFormats.formatDuration(until, TimeFormatType.LITERAL);
        //return formatTime(until - System.currentTimeMillis());
    }

    @NonNull
    public static LocalTime getLocalTimeOf(long ms) {
        long hours = TimeUnit.MILLISECONDS.toHours(ms) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60;

        return LocalTime.of((int) hours, (int) minutes, (int) seconds);
    }

    @NonNull
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(getZoneId());
    }

    @NonNull
    public static LocalDate getCurrentDate() {
        return LocalDate.now(getZoneId());
    }

    @NonNull
    public static LocalTime getCurrentTime() {
        return LocalTime.now(getZoneId());
    }

    @NonNull
    public static LocalDateTime getLocalDateTimeOf(long ms) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), getZoneId());
    }

    public static long toEpochMillis(@NonNull LocalDateTime dateTime) {
        Instant instant = dateTime.atZone(getZoneId()).toInstant();
        return instant.toEpochMilli();
    }
}
