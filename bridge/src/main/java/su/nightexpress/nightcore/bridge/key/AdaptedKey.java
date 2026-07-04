package su.nightexpress.nightcore.bridge.key;

import java.util.Optional;

import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public interface AdaptedKey {

    String NAMESPACE_SEPARATOR = ":";
    String PATH_SEPARATOR      = "/";
    String MINECRAFT_NAMESPACE = "minecraft";

    /**
     * Internal helper to build the appended value string.
     */
    default String buildChildValue(String... paths) {
        if (paths.length == 0) {
            return this.value();
        }

        String joined = LowerCase.internal(String.join(PATH_SEPARATOR, paths));
        return this.value() + PATH_SEPARATOR + joined;
    }

    /**
     * Mints a strict child key based on this key's path.
     * Throws an exception if the appended paths contain invalid characters.
     */
    default AdaptedKey child(String... paths) {
        return BukkitKeys.create(this.namespace(), this.buildChildValue(paths));
    }

    /**
     * Safely mints a child key based on this key's path.
     * Returns Optional.empty() if the appended paths contain invalid characters.
     */
    default Optional<AdaptedKey> safeChild(String... paths) {
        return BukkitKeys.parse(this.namespace(), this.buildChildValue(paths));
    }

    NamespacedKey bukkit();

    String namespace();

    String value();

    String asString();

    String asMinimalString();
}