package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.*;

public class ArrayUtil {

    private static final String ARRAY_DELIMITER = ",";

    @NotNull
    public static String arrayToString(double[] array) {
        return arrayToString(DoubleStream.of(array).boxed());
    }

    @NotNull
    public static String arrayToString(int[] array) {
        return arrayToString(IntStream.of(array).boxed());
    }

    @NotNull
    public static String arrayToString(long[] array) {
        return arrayToString(LongStream.of(array).boxed());
    }

    @NotNull
    public static String arrayToString(String[] array) {
        return arrayToString(Stream.of(array));
    }

    public static String[] parseStringArray(@NotNull String string) {
        return string.split(Pattern.quote(ARRAY_DELIMITER));
    }

    public static int[] parseIntArray(@NotNull String string) {
        return Stream.of(arrayFromString(string, Integer[]::new, raw -> Numbers.parseInteger(raw).orElseThrow())).mapToInt(i -> i).toArray();
    }

    public static double[] parseDoubleArray(@NotNull String string) {
        return Stream.of(arrayFromString(string, Double[]::new, raw -> Numbers.parseDouble(raw).orElseThrow())).mapToDouble(d -> d).toArray();
    }

    public static long[] parseLongArray(@NotNull String string) {
        return Stream.of(arrayFromString(string, Long[]::new, raw -> Numbers.parseLong(raw).orElseThrow())).mapToLong(i -> i).toArray();
    }

    @NotNull
    private static <T> String arrayToString(@NotNull Stream<T> stream) {
        return stream.map(String::valueOf).collect(Collectors.joining(ARRAY_DELIMITER));
    }

    private static <T> T[] arrayFromString(@NotNull String string, @NotNull Function<Integer, T[]> creator, @NotNull Function<String, T> converter) {
        String[] split = string.split(Pattern.quote(ARRAY_DELIMITER));
        List<T> parsed = new ArrayList<>();

        for (String raw : split) {
            try {
                parsed.add(converter.apply(raw.trim()));
            }
            catch (NumberFormatException | NoSuchElementException ignored) {}
        }
        return parsed.toArray(creator.apply(0));
    }
}
