package su.nightexpress.nightcore.config;

import org.jetbrains.annotations.NotNull;

public interface Writeable {

    void write(@NotNull FileConfig config, @NotNull String path);
}
