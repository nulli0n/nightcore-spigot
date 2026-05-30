package su.nightexpress.nightcore.manager;

import org.jspecify.annotations.NonNull;

import java.io.File;
import java.nio.file.Path;

public interface FileBacked {

    /**
     * Gets the file associated with the given object.
     * 
     * @return the file.
     */
    @Deprecated
    @NonNull
    default File getFile() {
        return this.getPath().toFile();
    }

    /**
     * Gets the file associated with the given object.
     * 
     * @return the file.
     */
    @NonNull
    Path getPath();
}
