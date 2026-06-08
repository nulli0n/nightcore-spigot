package su.nightexpress.nightcore.bridge.key;

import java.util.Optional;

import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public class KeyDomain {

    private final String namespace;
    private final String basePath;

    private KeyDomain(String namespace, String basePath) {
        this.namespace = namespace;
        this.basePath = basePath;
    }

    /**
     * Creates a new top-level domain.
     */
    public static KeyDomain of(String namespace, String basePath) {
        if (!BukkitKeys.isValidNamespace(namespace)) {
            throw new IllegalArgumentException("Invalid namespace: " + namespace);
        }
        if (!BukkitKeys.isValidValue(basePath)) {
            throw new IllegalArgumentException("Invalid base path: " + basePath);
        }
        return new KeyDomain(namespace, basePath);
    }

    public static KeyDomain of(Plugin plugin, String basePath) {
        String namespace = LowerCase.internal(plugin.getName());
        String safePath = LowerCase.internal(basePath);

        return of(namespace, safePath);
    }

    /**
     * Resolves input into a valid key.
     * If the input contains a ':', it is parsed as a raw, fully-qualified key.
     * Otherwise, it is treated as a shorthand ID and expanded into this domain.
     */
    public Optional<AdaptedKey> resolve(String input) {
        if (input.isBlank()) {
            return Optional.empty();
        }

        if (input.contains(AdaptedKey.NAMESPACE_SEPARATOR)) {
            return BukkitKeys.parse(input);
        }

        return this.makeSafe(input);
    }

    private String buildPath(String id) {
        String safeId = LowerCase.internal(id);
        return this.basePath.isEmpty() ? safeId : this.basePath + "." + safeId;
    }

    /**
     * Mints a new key within this domain.
     */
    public AdaptedKey make(String id) {
        return BukkitKeys.create(this.namespace, this.buildPath(id));
    }

    /**
     * Mints a new key within this domain.
     */
    public Optional<AdaptedKey> makeSafe(String id) {
        return BukkitKeys.parse(this.namespace, this.buildPath(id));
    }
}