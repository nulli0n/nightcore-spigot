package su.nightexpress.nightcore.config;

import org.jspecify.annotations.NonNull;

public interface Writeable {

    void write(@NonNull FileConfig config, @NonNull String path);
}
