package su.nightexpress.nightcore.bridge.key;

import java.util.Optional;

import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.exception.InvalidKeyException;
import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public interface AdaptedKey {

    public static final String NAMESPACE_SEPARATOR = ":";
    public static final String PATH_SEPARATOR      = "/";
    public static final String MINECRAFT_NAMESPACE = "minecraft";

    default String buildChildValue(String value) {
        return this.value() + PATH_SEPARATOR + LowerCase.internal(value);
    }

    default AdaptedKey child(String value) throws InvalidKeyException {
        return BukkitKeys.create(this.namespace(), this.buildChildValue(value));
    }

    default Optional<AdaptedKey> safeChild(String value) {
        return BukkitKeys.parse(this.namespace(), this.buildChildValue(value));
    }

    NamespacedKey bukkit();

    String namespace();

    String value();

    String asString();

    String asMinimalString();
}