package su.nightexpress.nightcore.bridge.key;

import java.nio.file.Path;

import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public final class KeyPathResolver {

    private static final String DEFAULT_FOLDER = "default";

    private final Path targetDir;

    @Nullable
    private final String defaultNamespace;
    @Nullable
    private final String prefixToRemove;

    /**
     * @param targetDir        The absolute directory where these files should be saved (e.g.,
     *                         plugins/MyPlugin/properties)
     * @param defaultNamespace The namespace that should be mapped to the "default" folder.
     * @param baseFolder       The prefix to strip from key values (e.g., "properties"). Can be null or empty.
     */
    private KeyPathResolver(Path targetDir, @Nullable String defaultNamespace, @Nullable String baseFolder) {
        this.targetDir = targetDir;
        this.defaultNamespace = defaultNamespace;

        if (baseFolder != null && !baseFolder.isBlank()) {
            this.prefixToRemove = baseFolder + AdaptedKey.PATH_SEPARATOR;
        }
        else this.prefixToRemove = null;
    }

    public static KeyPathResolver of(JavaPlugin plugin, Path targetDir, @Nullable String baseFolder) {
        String namespace = LowerCase.internal(plugin.getName());
        return new KeyPathResolver(targetDir, namespace, baseFolder);
    }

    public static KeyPathResolver of(JavaPlugin plugin, Path targetDir) {
        return of(plugin, targetDir, null);
    }

    /**
     * Creates a FileLocator directly from a KeyDomain.
     *
     * @param targetDir The absolute directory where these files should be saved (e.g.,
     *                  plugins/MyPlugin/properties)
     * @param domain    The KeyDomain dictating the namespace and prefix.
     */
    public static KeyPathResolver of(Path targetDir, KeyDomain domain) {
        return new KeyPathResolver(targetDir, domain.getNamespace(), domain.getBasePath());
    }

    /**
     * @param targetDir        The absolute directory where these files should be saved (e.g.,
     *                         plugins/MyPlugin/properties)
     * @param defaultNamespace The namespace that should be mapped to the "default" folder.
     * @param baseFolder       The prefix to strip from key values (e.g., "properties"). Can be null or empty.
     */
    public static KeyPathResolver of(Path targetDir, @Nullable String defaultNamespace, @Nullable String baseFolder) {
        return new KeyPathResolver(targetDir, defaultNamespace, baseFolder);
    }

    /**
     * @param targetDir        The absolute directory where these files should be saved (e.g.,
     *                         plugins/MyPlugin/properties)
     * @param defaultNamespace The namespace that should be mapped to the "default" folder.
     */
    public static KeyPathResolver of(Path targetDir, @Nullable String defaultNamespace) {
        return of(targetDir, defaultNamespace, null);
    }

    private Path resolveBase(AdaptedKey key, boolean isFile) {
        String folderName;
        if (this.defaultNamespace != null && key.namespace().equals(this.defaultNamespace)) {
            folderName = DEFAULT_FOLDER;
        }
        else folderName = key.namespace();

        String value = key.value();
        String prefix = this.prefixToRemove;

        if (prefix != null && value.startsWith(prefix)) {
            value = value.substring(prefix.length());
        }

        if (isFile) {
            value += ".yml";
        }

        return this.targetDir.resolve(folderName).resolve(value).normalize();
    }

    /**
     * Resolves the key to an absolute directory path without appending an extension.
     */
    public Path resolveDirectory(AdaptedKey key) {
        return this.resolveBase(key, false);
    }

    /**
     * Resolves the key to a configuration file path (appends .yml).
     */
    @Deprecated
    public Path resolve(AdaptedKey key) {
        return this.resolveFile(key);
    }

    /**
     * Resolves the key to a configuration file path (appends .yml).
     */
    public Path resolveFile(AdaptedKey key) {
        return this.resolveBase(key, true);
    }
}
