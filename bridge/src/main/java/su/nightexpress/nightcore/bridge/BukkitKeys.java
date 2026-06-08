package su.nightexpress.nightcore.bridge;

import java.util.Optional;
import java.util.function.Predicate;

import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.key.exception.InvalidKeyException;
import su.nightexpress.nightcore.util.bridge.Software;

@NullMarked
public final class BukkitKeys {

    private BukkitKeys() {
    }

    public static AdaptedKey create(String namespace, String value) throws InvalidKeyException {
        return Software.get().createKey(namespace, value);
    }

    public static AdaptedKey create(Plugin plugin, String value) throws InvalidKeyException {
        return Software.get().createKey(plugin, value);
    }

    public static AdaptedKey create(String string) throws InvalidKeyException {
        return Software.get().createKey(string);
    }

    public static Optional<AdaptedKey> parse(String namespace, String value) {
        try {
            return Optional.of(create(namespace, value));
        }
        catch (InvalidKeyException exception) {
            return Optional.empty();
        }
    }

    public static Optional<AdaptedKey> parse(Plugin plugin, String value) {
        try {
            return Optional.of(create(plugin, value));
        }
        catch (InvalidKeyException exception) {
            return Optional.empty();
        }
    }

    public static Optional<AdaptedKey> parse(String string) {
        try {
            return Optional.of(create(string));
        }
        catch (InvalidKeyException exception) {
            return Optional.empty();
        }
    }

    public static boolean isValidNamespace(String namespace) {
        return Software.get().isValidKeyNamespace(namespace);
    }

    public static boolean isValidValue(String value) {
        return Software.get().isValidKeyValue(value);
    }

    public static boolean allowedInNamespace(char character) {
        return Software.get().allowedInNamespace(character);
    }

    public static boolean allowedInValue(char character) {
        return Software.get().allowedInValue(character);
    }

    public static Optional<String> sanitizeNamespace(String namespace) {
        return sanitize(namespace, BukkitKeys::allowedInNamespace);
    }

    public static Optional<String> sanitizeValue(String value) {
        return sanitize(value, BukkitKeys::allowedInValue);
    }

    private static Optional<String> sanitize(String key, Predicate<Character> predicate) {
        int len = key.length();
        if (len == 0) return Optional.empty();

        StringBuilder builder = new StringBuilder();

        for (char c : key.toCharArray()) {
            if (predicate.test(c)) {
                builder.append(c);
            }
        }

        return builder.isEmpty() ? Optional.empty() : Optional.of(builder.toString());
    }
}