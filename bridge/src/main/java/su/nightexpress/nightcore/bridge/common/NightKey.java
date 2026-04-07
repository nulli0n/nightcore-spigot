package su.nightexpress.nightcore.bridge.common;

import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.Strings;

import java.util.Objects;
import java.util.Optional;

public class NightKey {

    public static final char DELIMITER = ':';

    private final String namespace;
    private final String value;

    private NightKey(@NonNull String namespace, @NonNull String value) {
        if (!parseableNamespace(namespace)) throw new IllegalArgumentException("Invalid namespace: '" + namespace + "'");
        if (!parseableValue(value)) throw new IllegalArgumentException("Invalid value: '" + value + "'");

        this.namespace = namespace;
        this.value = value;
    }

    @NonNull
    public static NightKey of(@NonNull String namespace, @NonNull String value) {
        return new NightKey(namespace, value);
    }

    @NonNull
    public static Optional<NightKey> parse(@NonNull String string) {
        try {
            return Optional.of(key(string));
        }
        catch (IllegalStateException exception) {
            return Optional.empty();
        }
    }

    @NonNull
    public static Optional<NightKey> parse(@NonNull String namespace, @NonNull String value) {
        try {
            return Optional.of(key(namespace, value));
        }
        catch (IllegalStateException exception) {
            return Optional.empty();
        }
    }

    @NonNull
    public static NightKey key(@NonNull String string) {
        return key(string, DELIMITER);
    }

    @NonNull
    public static NightKey key(@NonNull String string, char character) {
        int index = string.indexOf(character);
        String namespace = index >= 1 ? string.substring(0, index) : NamespacedKey.MINECRAFT;
        String value = index >= 0 ? string.substring(index + 1) : string;

        return key(namespace, value);
    }

    @NonNull
    public static NightKey key(@NonNull String namespace, @NonNull String value) {
        String namespaceFiltered = Strings.varStyle(namespace, NightKey::allowedInNamespace).orElseThrow(() -> new IllegalStateException("Invalid namespace: " + namespace));
        String valueFiltered = Strings.varStyle(value, NightKey::allowedInValue).orElseThrow(() -> new IllegalStateException("Invalid value: " + value));

        return of(namespaceFiltered, valueFiltered);
    }

    @NonNull
    @Deprecated
    public static NightKey fromString(@NonNull String string) {
        return key(string);
    }

    @NonNull
    public static NightKey fromBukkit(@NonNull NamespacedKey key) {
        return of(key.getNamespace(), key.getKey());
    }

    public static boolean parseable(@NonNull String string) {
        int index = string.indexOf(DELIMITER);
        String namespace = index >= 1 ? string.substring(0, index) : NamespacedKey.MINECRAFT;
        String value = index >= 0 ? string.substring(index + 1) : string;

        return parseableNamespace(namespace) && parseableValue(value);
    }

    public static boolean parseableNamespace(@NonNull String namespace) {
        for (int index = 0, length = namespace.length(); index < length; index++) {
            if (!allowedInNamespace(namespace.charAt(index))) {
                return false;
            }
        }
        return true;
    }

    public static boolean parseableValue(@NonNull String value) {
        for (int index = 0, length = value.length(); index < length; index++) {
            if (!allowedInValue(value.charAt(index))) {
                return false;
            }
        }
        return true;
    }

    public static boolean allowedInNamespace(char character) {
        return character == '_' || character == '-' || (character >= 'a' && character <= 'z') || (character >= '0' && character <= '9') || character == '.';
    }

    public static boolean allowedInValue(char character) {
        return allowedInNamespace(character) || character == '/';
    }

    @NonNull
    public String namespace() {
        return this.namespace;
    }

    @NonNull
    public String value() {
        return this.value;
    }

    @NonNull
    public String asString() {
        return this.namespace + DELIMITER + this.value;
    }

    @NonNull
    @Deprecated
    public NamespacedKey asBukkit() {
        return this.toBukkit();
    }

    @NonNull
    public NamespacedKey toBukkit() {
        return new NamespacedKey(this.namespace, this.value);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NightKey nightKey)) return false;
        return Objects.equals(namespace, nightKey.namespace) && Objects.equals(value, nightKey.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, value);
    }
}
