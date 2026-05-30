package su.nightexpress.nightcore.config;

import org.jspecify.annotations.NullMarked;

@NullMarked
@Deprecated(forRemoval = true)
public interface Writeable {

    void write(FileConfig config, String path);
}
