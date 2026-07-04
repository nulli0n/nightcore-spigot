package su.nightexpress.nightcore.bridge.key;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface KeyHolder {

    AdaptedKey getKey();

    default AdaptedKey key() {
        return this.getKey();
    }
}
