package su.nightexpress.nightcore.configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.Optional;

public class ConfigType<T> {

    private final Loader<T> loader;
    private final Writer<T> writer;

    public ConfigType(@NotNull Loader<T> loader, @NotNull Writer<T> writer) {
        this.loader = loader;
        this.writer = writer;
    }

    @NotNull
    public static <T> ConfigType<T> of(@NotNull Loader<T> loader, @NotNull Writer<T> writer) {
        return new ConfigType<>(loader, writer);
    }

    @NotNull
    public Optional<T> read(@NotNull FileConfig config, @NotNull String path) {
        if (!config.contains(path)) return Optional.empty();

        return Optional.ofNullable(this.loader.read(config, path));
    }

    @NotNull
    public T read(@NotNull FileConfig config, @NotNull String path, @NotNull T defaultValue) {
        return this.read(config, path).orElse(defaultValue);
    }

    public void write(@NotNull FileConfig config, @NotNull String path, @NotNull T value) {
        this.writer.write(config, path, value);
    }

    @FunctionalInterface
    public interface Loader<T> {

        @Nullable T read(@NotNull FileConfig config, @NotNull String path);
    }

    @FunctionalInterface
    public interface Writer<T> {

        void write(@NotNull FileConfig config, @NotNull String path, @NotNull T value);
    }
}
