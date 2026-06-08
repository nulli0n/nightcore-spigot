package su.nightexpress.nightcore.bridge.spigot.key;

import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class SpigotKey implements AdaptedKey {

    private final NamespacedKey key;

    public SpigotKey(NamespacedKey key) {
        this.key = key;
    }

    @Override
    public String asMinimalString() {
        if (this.namespace().equals(MINECRAFT_NAMESPACE)) {
            return this.value();
        }
        return this.asString();
    }

    @Override
    public String asString() {
        return this.namespace() + NAMESPACE_SEPARATOR + this.value();
    }

    @Override
    public NamespacedKey bukkit() {
        return this.key;
    }

    @Override
    public String namespace() {
        return this.key.getNamespace();
    }

    @Override
    public String value() {
        return this.key.getKey();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SpigotKey other = (SpigotKey) obj;
        return key.equals(other.key);
    }

    @Override
    public String toString() {
        return this.key.toString();
    }
}