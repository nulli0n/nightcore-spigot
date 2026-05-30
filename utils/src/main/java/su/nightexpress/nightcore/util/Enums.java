package su.nightexpress.nightcore.util;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class Enums {

    public static final String DEFAULT_DELIMITER = ", ";

    private Enums() {
    }

    public static <T extends Enum<T>> @NonNull Optional<T> parse(String str, @NonNull Class<T> type) {
        try {
            return str == null ? Optional.empty() : Optional.of(Enum.valueOf(type, str.toUpperCase()));
        }
        catch (Exception exception) {
            return Optional.empty();
        }
    }

    public static <T extends Enum<T>> @Nullable T get(@NonNull String str, @NonNull Class<T> type) {
        return parse(str, type).orElse(null);
    }

    public static @NonNull String inline(@NonNull Class<? extends Enum<?>> type) {
        return inline(type, DEFAULT_DELIMITER);
    }

    public static @NonNull String inline(@NonNull Class<? extends Enum<?>> type, @NonNull String delimiter) {
        return String.join(delimiter, Enums.getNames(type));
    }

    public static @NonNull List<String> getNames(@NonNull Class<? extends Enum<?>> type) {
        return Stream.of(type.getEnumConstants())
            .sorted(Comparator.comparingInt(Enum::ordinal))
            .map(Object::toString)
            .toList();
    }
}
