package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

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
        try {
            return Optional.of(Integer.parseInt(input));
        }
        catch (NumberFormatException exception) {
            return Optional.empty();
        }
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
        try {
            float amount = Float.parseFloat(input);
            if (!Float.isNaN(amount) && !Float.isInfinite(amount)) {
                return Optional.of(amount);
            }
            return Optional.empty();
        }
        catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }
}
