package su.nightexpress.nightcore.bridge.paper.key;

import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import net.kyori.adventure.key.Key;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class PaperKey implements AdaptedKey {

    private final Key key;

    public PaperKey(Key key) {
        this.key = key;
    }

    @Override
    public String asMinimalString() {
        return this.key.asMinimalString();
    }

    @Override
    public String asString() {
        return this.key.asString();
    }

    @Override
    public NamespacedKey bukkit() {
        return new NamespacedKey(this.namespace(), this.value());
    }

    @Override
    public String namespace() {
        return this.key.namespace();
    }

    @Override
    public String value() {
        return this.key.value();
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
        PaperKey other = (PaperKey) obj;
        return key.equals(other.key);
    }

    @Override
    public String toString() {
        return this.key.toString();
    }
}