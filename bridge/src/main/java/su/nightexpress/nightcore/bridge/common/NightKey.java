package su.nightexpress.nightcore.bridge.common;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.BukkitThing;

import java.util.Objects;

public class NightKey {

    public static final char DELIMITER = ':';

    private final String namespace;
    private final String value;

    public NightKey(@NotNull String namespace, @NotNull String value) {
        this.namespace = BukkitThing.validateNamespace(namespace);
        this.value = BukkitThing.validateValue(value);
    }

    @NotNull
    public static NightKey fromString(@NotNull String string) {
        return fromBukkit(BukkitThing.parseKey(string));
    }

    @NotNull
    public static NightKey fromBukkit(@NotNull NamespacedKey key) {
        return new NightKey(key.getNamespace(), key.getKey());
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
    public NamespacedKey asBukkit() {
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
