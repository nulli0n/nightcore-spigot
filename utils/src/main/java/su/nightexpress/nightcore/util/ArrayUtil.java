package su.nightexpress.nightcore.util;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.jspecify.annotations.NonNull;

public class ArrayUtil {

    private static final String DEFAULT_DELIMITER = ",";

    private ArrayUtil() {
    }

    public static @NonNull String arrayToString(double[] array) {
        return arrayToString(array, DEFAULT_DELIMITER);
    }

    public static @NonNull String arrayToString(double[] array, @NonNull String delimiter) {
        return arrayToString(DoubleStream.of(array).boxed(), delimiter);
    }

    public static @NonNull String arrayToString(int[] array) {
        return arrayToString(array, DEFAULT_DELIMITER);
    }

    public static @NonNull String arrayToString(int[] array, @NonNull String delimiter) {
        return arrayToString(IntStream.of(array).boxed(), delimiter);
    }

    public static @NonNull String arrayToString(long[] array) {
        return arrayToString(array, DEFAULT_DELIMITER);
    }

    public static @NonNull String arrayToString(long[] array, @NonNull String delimiter) {
        return arrayToString(LongStream.of(array).boxed(), delimiter);
    }

    public static @NonNull String arrayToString(String[] array) {
        return arrayToString(array, DEFAULT_DELIMITER);
    }

    public static @NonNull String arrayToString(String[] array, @NonNull String delimiter) {
        return arrayToString(Stream.of(array), delimiter);
    }

    public static String[] parseStringArray(@NonNull String string) {
        return parseStringArray(string, DEFAULT_DELIMITER);
    }

    public static String[] parseStringArray(@NonNull String string, @NonNull String delimiter) {
        return string.split(Pattern.quote(delimiter));
    }

    public static int[] parseIntArray(@NonNull String string) {
        return parseIntArray(string, DEFAULT_DELIMITER);
    }

    public static int[] parseIntArray(@NonNull String string, @NonNull String delimiter) {
        return Stream.of(arrayFromString(string, delimiter, Integer[]::new, raw -> Numbers.parseInteger(raw)
            .orElseThrow()))
            .mapToInt(i -> i).toArray();
    }

    public static double[] parseDoubleArray(@NonNull String string) {
        return parseDoubleArray(string, DEFAULT_DELIMITER);
    }

    public static double[] parseDoubleArray(@NonNull String string, @NonNull String delimiter) {
        return Stream.of(arrayFromString(string, delimiter, Double[]::new, raw -> Numbers.parseDouble(raw)
            .orElseThrow()))
            .mapToDouble(d -> d).toArray();
    }

    public static long[] parseLongArray(@NonNull String string) {
        return parseLongArray(string, DEFAULT_DELIMITER);
    }

    public static long[] parseLongArray(@NonNull String string, @NonNull String delimiter) {
        return Stream.of(arrayFromString(string, delimiter, Long[]::new, raw -> Numbers.parseLong(raw)
            .orElseThrow()))
            .mapToLong(i -> i).toArray();
    }

    @NonNull
    private static <T> String arrayToString(@NonNull Stream<T> stream, @NonNull String delimiter) {
        return stream.map(String::valueOf).collect(Collectors.joining(delimiter));
    }

    private static <T> T[] arrayFromString(@NonNull String string,
                                           @NonNull String delimiter,
                                           @NonNull IntFunction<T[]> creator,
                                           @NonNull Function<String, T> converter) {
        String[] split = string.split(Pattern.quote(delimiter));
        List<T> parsed = new ArrayList<>();

        for (String raw : split) {
            try {
                parsed.add(converter.apply(raw.trim()));
            }
            catch (NumberFormatException | NoSuchElementException ignored) {
                // Ignore failures
            }
        }
        return parsed.toArray(creator.apply(0));
    }
}
