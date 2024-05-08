package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.core.CoreConfig;
import su.nightexpress.nightcore.core.CoreLang;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Supplier;

public class NumberUtil {

    private final static TreeMap<Integer, String> ROMAN_MAP = new TreeMap<>();
    private final static TreeMap<Integer, Supplier<String>> NUMERIC_MAP = new TreeMap<>();

    static {
        NUMERIC_MAP.put(0, () -> "");
        NUMERIC_MAP.put(1, CoreLang.NUMBER_SHORT_THOUSAND::getString);
        NUMERIC_MAP.put(2, CoreLang.NUMBER_SHORT_MILLION::getString);
        NUMERIC_MAP.put(3, CoreLang.NUMBER_SHORT_BILLION::getString);
        NUMERIC_MAP.put(4, CoreLang.NUMBER_SHORT_TRILLION::getString);
        NUMERIC_MAP.put(5, CoreLang.NUMBER_SHORT_QUADRILLION::getString);

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
    public static String format(double value) {
        return CoreConfig.NUMBER_FORMAT.get().format(value);
    }

    @NotNull
    public static String compact(double value) {
        Pair<String, String> pair = formatCompact(value);
        return pair.getFirst() + pair.getSecond();
    }

    @NotNull
    public static Pair<String, String> formatCompact(double value) {
        boolean negative = false;
        if (value < 0) {
            value = Math.abs(value);
            negative = true;
        }
        int index = 0;
        while ((value / 1000) >= 1 && index < NUMERIC_MAP.size() - 1) {
            value = value / 1000;
            index++;
        }

        return Pair.of(CoreConfig.NUMBER_FORMAT.get().format(negative ? -value : value), NUMERIC_MAP.get(NUMERIC_MAP.floorKey(index)).get());
    }

    public static double round(double value) {
        return new BigDecimal(value).setScale(2, CoreConfig.NUMBER_FORMAT.get().getRounding()).doubleValue();
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

    public static double getDouble(@NotNull String input) {
        return getDouble(input, 0D);
    }

    public static double getDouble(@NotNull String input, double defaultValue) {
        return Math.abs(getAnyDouble(input, defaultValue));
    }

    public static double getAnyDouble(@NotNull String input, double defaultValue) {
        return parseDouble(input).orElse(defaultValue);
    }

    public static int getInteger(@NotNull String input) {
        return getInteger(input, 0);
    }

    public static int getInteger(@NotNull String input, int defaultValue) {
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

    public static int[] getIntArray(@NotNull String str) {
        String[] split = str.split(",");
        int[] array = new int[split.length];
        for (int index = 0; index < split.length; index++) {
            try {
                array[index] = Integer.parseInt(split[index].trim());
            } catch (NumberFormatException e) {
                array[index] = 0;
            }
        }
        return array;
    }
}
