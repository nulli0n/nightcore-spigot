package su.nightexpress.nightcore.config;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public interface ReadFunction<V> {

    @Nullable
    V read(@NonNull FileConfig config, @NonNull String path, @NonNull String id);

}
