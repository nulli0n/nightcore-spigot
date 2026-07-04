package su.nightexpress.nightcore.bridge.key;

import java.util.Optional;

import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.exception.InvalidKeyException;
import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public final class KeyDomain {

    private final String namespace;
    private final String basePath;

    private KeyDomain(String namespace, String basePath) {
        this.namespace = namespace;
        this.basePath = basePath;
    }

    /**
     * Creates a new top-level domain without a base path prefix.
     *
     * @param namespace The plugin namespace (e.g., "myplugin")
     * @return A new KeyDomain instance
     * @throws IllegalArgumentException if the namespace contains invalid characters
     */
    public static KeyDomain of(String namespace) {
        return of(namespace, "");
    }

    /**
     * Creates a new top-level domain with a base path prefix.
     *
     * @param namespace The plugin namespace (e.g., "myplugin")
     * @param basePath  The base directory for keys in this domain (e.g., "crates")
     * @return A new KeyDomain instance
     * @throws IllegalArgumentException if the namespace or base path contains invalid characters
     */
    public static KeyDomain of(String namespace, String basePath) {
        if (!BukkitKeys.isValidNamespace(namespace)) {
            throw new IllegalArgumentException("Invalid namespace format: " + namespace);
        }

        // Only validate base path if it's not empty
        if (!basePath.isEmpty() && !BukkitKeys.isValidValue(basePath)) {
            throw new IllegalArgumentException("Invalid base path format: " + basePath);
        }

        return new KeyDomain(namespace, basePath);
    }

    public static KeyDomain of(Plugin plugin) {
        return of(plugin, "");
    }

    public static KeyDomain of(Plugin plugin, String basePath) {
        String namespace = LowerCase.internal(plugin.getName());
        String safePath = LowerCase.internal(basePath);

        return of(namespace, safePath);
    }

    /**
     * Safely resolves user input into a valid key.
     * If the input contains a ':', it is parsed as an explicit, fully-qualified key.
     * Otherwise, it is treated as a shorthand ID and expanded into this specific domain.
     *
     * @param input The raw string from a user command or config
     * @return An Optional containing the key, or empty if the format is entirely invalid
     */
    public Optional<AdaptedKey> resolve(String input) {
        if (input.isBlank()) {
            return Optional.empty();
        }

        if (input.contains(AdaptedKey.NAMESPACE_SEPARATOR)) {
            return BukkitKeys.parse(input);
        }

        String safeInput = LowerCase.internal(input);
        if (!this.basePath.isEmpty()) {
            if (safeInput.equals(this.basePath) || safeInput.startsWith(this.basePath + AdaptedKey.PATH_SEPARATOR)) {
                return BukkitKeys.parse(this.namespace, safeInput);
            }
        }

        return this.makeSafe(input);
    }

    private String buildPath(String... paths) {
        return this.buildPath(this.basePath, paths);
    }

    private String buildPath(String basePath, String... paths) {
        if (paths.length == 0) return basePath;

        String joined = LowerCase.internal(String.join(AdaptedKey.PATH_SEPARATOR, paths));

        return basePath.isEmpty() ? joined : basePath + AdaptedKey.PATH_SEPARATOR + joined;
    }

    private String[] combineKeyAndPath(AdaptedKey prefixKey, String... paths) {
        String[] combined = new String[paths.length + 2];
        combined[0] = prefixKey.namespace();
        combined[1] = prefixKey.value();

        if (paths.length > 0) {
            System.arraycopy(paths, 0, combined, 2, paths.length);
        }

        return combined;
    }

    /**
     * Mints a strictly validated new key within this domain.
     * 
     * @param paths The directories/ID to append to the base path
     * 
     * @return A fully constructed AdaptedKey
     * @throws InvalidKeyException if the resulting path contains invalid characters
     */
    public AdaptedKey make(String... paths) throws InvalidKeyException {
        return BukkitKeys.create(this.namespace, this.buildPath(paths));
    }

    /**
     * Safely mints a new key within this domain, suppressing exceptions.
     * 
     * @param paths The directories/ID to append to the base path
     * 
     * @return An Optional containing the key, or empty if invalid
     */
    public Optional<AdaptedKey> makeSafe(String... paths) {
        return BukkitKeys.parse(this.namespace, this.buildPath(paths));
    }

    /**
     * Mints a strictly validated new key by safely embedding an existing key's path.
     * Automatically converts the prefix key's namespace to a valid directory format.
     * 
     * @param prefixKey The key to embed (e.g., a world key like minecraft:overworld)
     * @param paths     Additional directories/ID to append
     * 
     * @return A fully constructed AdaptedKey
     * 
     * @throws InvalidKeyException if the resulting path contains invalid characters
     */
    public AdaptedKey combine(AdaptedKey prefixKey, String... paths) throws InvalidKeyException {
        return this.make(this.combineKeyAndPath(prefixKey, paths));
    }

    /**
     * Safely mints a new key by embedding an existing key, suppressing exceptions.
     * 
     * @param prefixKey The key to embed
     * 
     * @param paths     Additional directories/ID to append
     * @return An Optional containing the key, or empty if invalid
     */
    public Optional<AdaptedKey> combineSafe(AdaptedKey prefixKey, String... paths) {
        return this.makeSafe(this.combineKeyAndPath(prefixKey, paths));
    }

    public String getNamespace() {
        return namespace;
    }

    public String getBasePath() {
        return basePath;
    }
}