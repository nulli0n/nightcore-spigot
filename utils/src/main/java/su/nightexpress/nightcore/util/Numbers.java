package su.nightexpress.nightcore.util;

import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.function.Function;

public class Numbers {

    public static int getIntegerAbs(@NonNull String input) {
        return getIntegerAbs(input, 0);
    }

    public static int getIntegerAbs(@NonNull String input, int defaultValue) {
        return Math.abs(getAnyInteger(input, defaultValue));
    }

    public static int getAnyInteger(@NonNull String input, int defaultValue) {
        return parseInteger(input).orElse(defaultValue);
    }

    @NonNull
    public static Optional<Integer> parseInteger(@NonNull String input) {
        return parseNumber(input, Integer::parseInt);
    }


    public static long getLongAbs(@NonNull String input) {
        return getLongAbs(input, 0L);
    }

    public static long getLongAbs(@NonNull String input, long defaultValue) {
        return Math.abs(getAnyLong(input, defaultValue));
    }

    public static long getAnyLong(@NonNull String input, long defaultValue) {
        return parseLong(input).orElse(defaultValue);
    }

    @NonNull
    public static Optional<Long> parseLong(@NonNull String input) {
        return parseNumber(input, Long::parseLong);
    }


    public static float getFloatAbs(@NonNull String input) {
        return getFloatAbs(input, 0f);
    }

    public static float getFloatAbs(@NonNull String input, float defaultValue) {
        return Math.abs(getFloat(input, defaultValue));
    }

    public static float getFloat(@NonNull String input, float defaultValue) {
        return parseFloat(input).orElse(defaultValue);
    }

    @NonNull
    public static Optional<Float> parseFloat(@NonNull String input) {
        return parseNumber(input, string -> {
            float amount = Float.parseFloat(input);
            return (!Float.isNaN(amount) && !Float.isInfinite(amount)) ? amount : null;
        });
    }


    public static double getDoubleAbs(@NonNull String input) {
        return getDoubleAbs(input, 0D);
    }

    public static double getDoubleAbs(@NonNull String input, double defaultValue) {
        return Math.abs(getDouble(input, defaultValue));
    }

    public static double getDouble(@NonNull String input, double defaultValue) {
        return parseDouble(input).orElse(defaultValue);
    }

    @NonNull
    public static Optional<Double> parseDouble(@NonNull String input) {
        return parseNumber(input, string -> {
            double amount = Double.parseDouble(input);
            return (!Double.isNaN(amount) && !Double.isInfinite(amount)) ? amount : null;
        });
    }

    @NonNull
    private static <T> Optional<T> parseNumber(@NonNull String input, @NonNull Function<String, T> converter) {
        if (input.isBlank()) return Optional.empty();

        try {
            T value = converter.apply(input);
            return value == null ? Optional.empty() : Optional.of(value);
        }
        catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }
}
