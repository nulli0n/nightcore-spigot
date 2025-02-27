package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.time.TimeFormatType;
import su.nightexpress.nightcore.util.time.TimeFormats;

import java.time.*;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

    private static TimeZone timeZone;

    public static void setTimeZone(@NotNull String name) {
        timeZone = TimeZone.getTimeZone(name);
    }

    @NotNull
    public static TimeZone getTimeZone() {
        return timeZone;
    }

    @NotNull
    public static ZoneId getZoneId() {
        return timeZone.toZoneId();
    }

    @NotNull
    @Deprecated
    public static String formatTime(long time) {
        return TimeFormats.toLiteral(time);
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
    @Deprecated
    public static String formatDuration(long from, long to) {
        long time = to - from;
        //return formatTime(time);
        return TimeFormats.toLiteral(time);
    }

    @NotNull
    @Deprecated
    public static String formatDuration(long until) {
        return TimeFormats.formatDuration(until, TimeFormatType.LITERAL);
        //return formatTime(until - System.currentTimeMillis());
    }

    @NotNull
    public static LocalTime getLocalTimeOf(long ms) {
        long hours = TimeUnit.MILLISECONDS.toHours(ms) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60;

        return LocalTime.of((int) hours, (int) minutes, (int) seconds);
    }

    @NotNull
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(getZoneId());
    }

    @NotNull
    public static LocalDate getCurrentDate() {
        return LocalDate.now(getZoneId());
    }

    @NotNull
    public static LocalTime getCurrentTime() {
        return LocalTime.now(getZoneId());
    }

    @NotNull
    public static LocalDateTime getLocalDateTimeOf(long ms) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), getZoneId());
    }

    public static long toEpochMillis(@NotNull LocalDateTime dateTime) {
        Instant instant = dateTime.atZone(getZoneId()).toInstant();
        return instant.toEpochMilli();
    }
}
