package su.nightexpress.nightcore.bridge.common;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.Strings;

import java.util.Objects;
import java.util.Optional;

public class NightKey {

    public static final char DELIMITER = ':';

    private final String namespace;
    private final String value;

    private NightKey(@NotNull String namespace, @NotNull String value) {
        if (!parseableNamespace(namespace)) throw new IllegalArgumentException("Invalid namespace: '" + namespace + "'");
        if (!parseableValue(value)) throw new IllegalArgumentException("Invalid value: '" + value + "'");

        this.namespace = namespace;
        this.value = value;
    }

    @NotNull
    public static NightKey of(@NotNull String namespace, @NotNull String value) {
        return new NightKey(namespace, value);
    }

    @NotNull
    public static Optional<NightKey> parse(@NotNull String string) {
        try {
            return Optional.of(key(string));
        }
        catch (IllegalStateException exception) {
            return Optional.empty();
        }
    }

    @NotNull
    public static Optional<NightKey> parse(@NotNull String namespace, @NotNull String value) {
        try {
            return Optional.of(key(namespace, value));
        }
        catch (IllegalStateException exception) {
            return Optional.empty();
        }
    }

    @NotNull
    public static NightKey key(@NotNull String string) {
        return key(string, DELIMITER);
    }

    @NotNull
    public static NightKey key(@NotNull String string, char character) {
        int index = string.indexOf(character);
        String namespace = index >= 1 ? string.substring(0, index) : NamespacedKey.MINECRAFT;
        String value = index >= 0 ? string.substring(index + 1) : string;

        return key(namespace, value);
    }

    @NotNull
    public static NightKey key(@NotNull String namespace, @NotNull String value) {
        String namespaceFiltered = Strings.varStyle(namespace, NightKey::allowedInNamespace).orElseThrow(() -> new IllegalStateException("Invalid namespace: " + namespace));
        String valueFiltered = Strings.varStyle(value, NightKey::allowedInValue).orElseThrow(() -> new IllegalStateException("Invalid value: " + value));

        return of(namespaceFiltered, valueFiltered);
    }

    @NotNull
    @Deprecated
    public static NightKey fromString(@NotNull String string) {
        return key(string);
    }

    @NotNull
    public static NightKey fromBukkit(@NotNull NamespacedKey key) {
        return of(key.getNamespace(), key.getKey());
    }

    public static boolean parseable(@NotNull String string) {
        int index = string.indexOf(DELIMITER);
        String namespace = index >= 1 ? string.substring(0, index) : NamespacedKey.MINECRAFT;
        String value = index >= 0 ? string.substring(index + 1) : string;

        return parseableNamespace(namespace) && parseableValue(value);
    }

    public static boolean parseableNamespace(@NotNull String namespace) {
        for (int index = 0, length = namespace.length(); index < length; index++) {
            if (!allowedInNamespace(namespace.charAt(index))) {
                return false;
            }
        }
        return true;
    }

    public static boolean parseableValue(@NotNull String value) {
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

    @NotNull
    public String namespace() {
        return this.namespace;
    }

    @NotNull
    public String value() {
        return this.value;
    }

    @NotNull
    public String asString() {
        return this.namespace + DELIMITER + this.value;
    }

    @NotNull
    @Deprecated
    public NamespacedKey asBukkit() {
        return this.toBukkit();
    }

    @NotNull
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
