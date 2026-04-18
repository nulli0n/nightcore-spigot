package su.nightexpress.nightcore.util;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class Enums {

    private static final String DEFAULT_DELIMITER = ", ";

    @NonNull
    public static <T extends Enum<T>> Optional<@Nullable T> parse(String str, @NonNull Class<T> clazz) {
        try {
            return str == null ? Optional.empty() : Optional.of(Enum.valueOf(clazz, str.toUpperCase()));
        }
        catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Nullable
    public static <T extends Enum<T>> T get(String str, @NonNull Class<T> clazz) {
        return parse(str, clazz).orElse(null);
    }

    @NonNull
    public static String inline(@NonNull Class<? extends Enum<?>> clazz) {
        return inline(clazz, DEFAULT_DELIMITER);
    }

    @NonNull
    public static String inline(@NonNull Class<? extends Enum<?>> clazz, @NonNull String delimiter) {
        return String.join(delimiter, Enums.getNames(clazz));
    }

    @NonNull
    public static List<String> getNames(@NonNull Class<? extends Enum<?>> clazz) {
        return Stream.of(clazz.getEnumConstants()).sorted(Comparator.comparingInt(Enum::ordinal)).map(Object::toString)
            .toList();
    }
}
