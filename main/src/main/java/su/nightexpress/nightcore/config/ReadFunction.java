package su.nightexpress.nightcore.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ReadFunction<V> {

    @Nullable V read(@NotNull FileConfig config, @NotNull String path, @NotNull String id);

}
