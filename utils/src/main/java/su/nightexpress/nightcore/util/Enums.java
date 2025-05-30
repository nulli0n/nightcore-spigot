package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Enums {

    private static final String DEFAULT_DELIMITER = ", ";

    @NotNull
    public static <T extends Enum<T>> Optional<T> parse(String str, @NotNull Class<T> clazz) {
        try {
            return str == null ? Optional.empty() : Optional.of(Enum.valueOf(clazz, str.toUpperCase()));
        }
        catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Nullable
    public static <T extends Enum<T>> T get(String str, @NotNull Class<T> clazz) {
        return parse(str, clazz).orElse(null);
    }

    @NotNull
    public static String inline(@NotNull Class<? extends Enum<?>> clazz) {
        return inline(clazz, DEFAULT_DELIMITER);
    }

    @NotNull
    public static String inline(@NotNull Class<? extends Enum<?>> clazz, @NotNull String delimiter) {
        return String.join(delimiter, Enums.getNames(clazz));
    }

    @NotNull
    public static List<String> getNames(@NotNull Class<? extends Enum<?>> clazz) {
        return Stream.of(clazz.getEnumConstants()).sorted(Comparator.comparingInt(Enum::ordinal)).map(Object::toString).toList();
    }
}
