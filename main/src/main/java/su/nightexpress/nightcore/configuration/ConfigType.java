package su.nightexpress.nightcore.configuration;

import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.config.FileConfig;

public class ConfigType<T> {

    private final Loader<T> loader;
    private final Writer<T> writer;

    public ConfigType(@NonNull Loader<T> loader, @NonNull Writer<T> writer) {
        this.loader = loader;
        this.writer = writer;
    }

    @NonNull
    public static <T> ConfigType<T> of(@NonNull Loader<T> loader, @NonNull Writer<T> writer) {
        return new ConfigType<>(loader, writer);
    }

    @NonNull
    public static <T> ConfigType<T> of(@NonNull Loader<T> loader) {
        return of(loader, FileConfig::set);
    }

    @NonNull
    public Optional<T> read(@NonNull FileConfig config, @NonNull String path) {
        if (!config.contains(path)) return Optional.empty();

        return Optional.ofNullable(this.loader.read(config, path));
    }

    @NonNull
    public T read(@NonNull FileConfig config, @NonNull String path, @NonNull T defaultValue) {
        return this.read(config, path).orElse(defaultValue);
    }

    public void write(@NonNull FileConfig config, @NonNull String path, @NonNull T value) {
        this.writer.write(config, path, value);
    }

    @FunctionalInterface
    public interface Loader<T> {

        @Nullable T read(@NonNull FileConfig config, @NonNull String path);
    }

    @FunctionalInterface
    public interface Writer<T> {

        void write(@NonNull FileConfig config, @NonNull String path, @NonNull T value);
    }
}
