package su.nightexpress.nightcore.manager;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;

public interface FileBacked {

    /**
     * Gets the file associated with the given object.
     * @return the file.
     */
    @Deprecated
    @NotNull default File getFile() {
        return this.getPath().toFile();
    }

    /**
     * Gets the file associated with the given object.
     * @return the file.
     */
    @NotNull Path getPath();
}
