package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

public class Numbers {

    public static int getIntegerAbs(@NotNull String input) {
        return getIntegerAbs(input, 0);
    }

    public static int getIntegerAbs(@NotNull String input, int defaultValue) {
        return Math.abs(getAnyInteger(input, defaultValue));
    }

    public static int getAnyInteger(@NotNull String input, int defaultValue) {
        return parseInteger(input).orElse(defaultValue);
    }

    @NotNull
    public static Optional<Integer> parseInteger(@NotNull String input) {
        return parseNumber(input, Integer::parseInt);
    }



    public static long getLongAbs(@NotNull String input) {
        return getLongAbs(input, 0L);
    }

    public static long getLongAbs(@NotNull String input, long defaultValue) {
        return Math.abs(getAnyLong(input, defaultValue));
    }

    public static long getAnyLong(@NotNull String input, long defaultValue) {
        return parseLong(input).orElse(defaultValue);
    }

    @NotNull
    public static Optional<Long> parseLong(@NotNull String input) {
        return parseNumber(input, Long::parseLong);
    }



    public static float getFloatAbs(@NotNull String input) {
        return getFloatAbs(input, 0f);
    }

    public static float getFloatAbs(@NotNull String input, float defaultValue) {
        return Math.abs(getFloat(input, defaultValue));
    }

    public static float getFloat(@NotNull String input, float defaultValue) {
        return parseFloat(input).orElse(defaultValue);
    }

    @NotNull
    public static Optional<Float> parseFloat(@NotNull String input) {
        return parseNumber(input, string -> {
            float amount = Float.parseFloat(input);
            return (!Float.isNaN(amount) && !Float.isInfinite(amount)) ? amount : null;
        });
    }




    public static double getDoubleAbs(@NotNull String input) {
        return getDoubleAbs(input, 0D);
    }

    public static double getDoubleAbs(@NotNull String input, double defaultValue) {
        return Math.abs(getDouble(input, defaultValue));
    }

    public static double getDouble(@NotNull String input, double defaultValue) {
        return parseDouble(input).orElse(defaultValue);
    }

    @NotNull
    public static Optional<Double> parseDouble(@NotNull String input) {
        return parseNumber(input, string -> {
            double amount = Double.parseDouble(input);
            return (!Double.isNaN(amount) && !Double.isInfinite(amount)) ? amount : null;
        });
    }

    @NotNull
    private static <T> Optional<T> parseNumber(@NotNull String input, @NotNull Function<String, T> converter) {
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
