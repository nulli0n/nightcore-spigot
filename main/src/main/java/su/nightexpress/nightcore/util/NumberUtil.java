package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.util.number.CompactNumber;
import su.nightexpress.nightcore.util.number.NumberShortcut;
import su.nightexpress.nightcore.util.wrapper.UniFormatter;

import java.math.BigDecimal;
import java.util.*;

public class NumberUtil {

    private final static TreeMap<Integer, String> ROMAN_MAP = new TreeMap<>();
    //private final static TreeMap<Integer, Supplier<String>> NUMERIC_MAP = new TreeMap<>();

    static {
//        NUMERIC_MAP.put(0, () -> "");
//        NUMERIC_MAP.put(1, CoreLang.NUMBER_SHORT_THOUSAND::getString);
//        NUMERIC_MAP.put(2, CoreLang.NUMBER_SHORT_MILLION::getString);
//        NUMERIC_MAP.put(3, CoreLang.NUMBER_SHORT_BILLION::getString);
//        NUMERIC_MAP.put(4, CoreLang.NUMBER_SHORT_TRILLION::getString);
//        NUMERIC_MAP.put(5, CoreLang.NUMBER_SHORT_QUADRILLION::getString);

        ROMAN_MAP.put(1000, "M");
        ROMAN_MAP.put(900, "CM");
        ROMAN_MAP.put(500, "D");
        ROMAN_MAP.put(400, "CD");
        ROMAN_MAP.put(100, "C");
        ROMAN_MAP.put(90, "XC");
        ROMAN_MAP.put(50, "L");
        ROMAN_MAP.put(40, "XL");
        ROMAN_MAP.put(10, "X");
        ROMAN_MAP.put(9, "IX");
        ROMAN_MAP.put(5, "V");
        ROMAN_MAP.put(4, "IV");
        ROMAN_MAP.put(1, "I");
    }

    @NotNull
    public static UniFormatter getNumberFormat() {
        return CoreConfig.NUMBER_FORMAT.get();
    }

    @NotNull
    public static List<NumberShortcut> getNumberShortcuts() {
        return CoreConfig.NUMBER_SHORTCUT_LIST.get().values().stream().sorted(Comparator.comparing(NumberShortcut::getMagnitude)).toList();
    }

    public static double round(double value) {
        return round(value, 2);
    }

    public static double round(double value, int scale) {
        return new BigDecimal(value).setScale(scale, getNumberFormat().getRounding()).doubleValue();
    }

    @NotNull
    public static String format(double value) {
        return getNumberFormat().format(value);
    }

    @NotNull
    @Deprecated
    public static String compact(double value) {
        return formatCompact(value);
    }

    @NotNull
    public static String formatCompact(double value) {
        return asCompact(value).format();
    }

    @NotNull
    public static CompactNumber asCompact(double value) {
        boolean negative = value < 0;
        value = Math.abs(value);

        List<NumberShortcut> shortcuts = getNumberShortcuts();
        int step = CoreConfig.NUMBER_SHORTCUT_STEP.get();
        int index = -1;
        int maxIndex = shortcuts.size() - 1;

        while ((value / step) >= 1 && index < maxIndex) {
            value = value / step;
            index++;
        }

        double result = negative ? -value : value;
        NumberShortcut shortcut = index < 0 ? null : shortcuts.get(index);

        return new CompactNumber(result, shortcut);
    }

    public static double getDoubleCompact(@NotNull String input) {
        return parseCompact(input).orElse(0D);
    }

    public static double getDoubleCompactAbs(@NotNull String input) {
        return Math.abs(getDoubleCompact(input));
    }

    @NotNull
    public static Optional<Double> parseDecimalCompact(@NotNull String input) {
        return parseCompact(input);
    }

    public static double getIntCompact(@NotNull String input) {
        return parseIntCompact(input).orElse(0);
    }

    public static double getIntCompactAbs(@NotNull String input) {
        return Math.abs(getIntCompact(input));
    }

    @NotNull
    public static Optional<Integer> parseIntCompact(@NotNull String input) {
        return parseCompact(input).map(Double::intValue);
    }

    @NotNull
    public static Optional<Double> parseCompact(@NotNull String input) {
        input = input.toLowerCase();
        double multiplier = 1D;

        List<NumberShortcut> shortcuts = getNumberShortcuts();

        for (NumberShortcut shortcut : shortcuts) {
            int index = input.indexOf(shortcut.getSymbol());
            if (index < 0) continue;

            input = input.substring(0, index);
            multiplier = shortcut.getMultiplier();
            break;
        }

        var optional = parseDouble(input);
        if (optional.isEmpty()) return Optional.empty();

        double value = optional.get() * multiplier;
        if (Double.isInfinite(value) || Double.isNaN(value)) return Optional.empty();

        return Optional.of(value);
    }

    @NotNull
    public static String toRoman(int number) {
        if (number <= 0) return String.valueOf(number);

        int key = ROMAN_MAP.floorKey(number);
        if (number == key) {
            return ROMAN_MAP.get(number);
        }
        return ROMAN_MAP.get(key) + toRoman(number - key);
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static int[] splitIntoParts(int whole, int parts) {
        int[] arr = new int[parts];
        int remain = whole;
        int partsLeft = parts;
        for (int i = 0; partsLeft > 0; i++) {
            int size = (remain + partsLeft - 1) / partsLeft; // rounded up, aka ceiling
            arr[i] = size;
            remain -= size;
            partsLeft--;
        }
        return arr;
    }

    @Deprecated
    public static double getDouble(@NotNull String input) {
        return getDoubleAbs(input);
    }

    @Deprecated
    public static double getDouble(@NotNull String input, double defaultValue) {
        return getDoubleAbs(input, defaultValue);
    }

    public static double getDoubleAbs(@NotNull String input) {
        return getDoubleAbs(input, 0D);
    }

    public static double getDoubleAbs(@NotNull String input, double defaultValue) {
        return Math.abs(getAnyDouble(input, defaultValue));
    }

    public static double getAnyDouble(@NotNull String input, double defaultValue) {
        return parseDouble(input).orElse(defaultValue);
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

    @Deprecated
    public static int getInteger(@NotNull String input) {
        return getIntegerAbs(input);
    }

    @Deprecated
    public static int getInteger(@NotNull String input, int defaultValue) {
        return getIntegerAbs(input, defaultValue);
    }

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

    @NotNull
    public static Optional<Double> parseDouble(@NotNull String input) {
        try {
            double amount = Double.parseDouble(input);
            if (!Double.isNaN(amount) && !Double.isInfinite(amount)) {
                return Optional.of(amount);
            }
            return Optional.empty();
        }
        catch (NumberFormatException exception) {
            return Optional.empty();
        }
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

    public static int[] getIntArray(@NotNull String str) {
        String[] split = str.split(",");
        int[] array = new int[split.length];
        for (int index = 0; index < split.length; index++) {
            try {
                array[index] = Integer.parseInt(split[index].trim());
            }
            catch (NumberFormatException exception) {
                array[index] = 0;
            }
        }
        return array;
    }
}
