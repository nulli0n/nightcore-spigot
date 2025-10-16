package su.nightexpress.nightcore.manager;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;

public interface ConfigBacked extends FileBacked {

    @NotNull
    @Deprecated
    default FileConfig getConfig() {
        return this.loadConfig();
    }

    /**
     * Parses the YAML configuration from the object's file.
     * The config is not cached and is being loaded/parsed every time this method called.
     * @return A "fresh" FileConfig for the object's file.
     * @see FileBacked#getPath()
     */
    @NotNull
    default FileConfig loadConfig() {
        return FileConfig.load(this.getPath());
    }
}
