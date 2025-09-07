package su.nightexpress.nightcore.manager;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;

public interface ConfigBacked extends FileBacked {

    /**
     * Parses the YAML configuration from the object's file.
     * The config is not cached and is being loaded/parsed every time this method called.
     * @return A "fresh" FileConfig for the object's file.
     * @see FileBacked#getFile()
     */
    @NotNull
    default FileConfig getConfig() {
        return new FileConfig(this.getFile());
    }
}
