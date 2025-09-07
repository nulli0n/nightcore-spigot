package su.nightexpress.nightcore.manager;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface FileBacked {

    /**
     * Gets the file associated with the given object.
     * @return Object's file.
     */
    @NotNull File getFile();
}
