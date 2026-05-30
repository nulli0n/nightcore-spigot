package su.nightexpress.nightcore.configuration;

import java.util.Optional;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
@Deprecated(forRemoval = true)
public class ConfigType<T> implements ConfigCodec<T> {

    private final Loader<T> loader;
    private final Writer<T> writer;

    public ConfigType(Loader<T> loader, Writer<T> writer) {
        this.loader = loader;
        this.writer = writer;
    }

    public static <T> ConfigType<T> of(Loader<T> loader, Writer<T> writer) {
        return new ConfigType<>(loader, writer);
    }

    public static <T> ConfigType<T> of(Loader<T> loader) {
        return of(loader, FileConfig::set);
    }

    @Override
    public Optional<T> readOptional(FileConfig config, String path) {
        if (!config.contains(path)) return Optional.empty();

        return Optional.ofNullable(this.loader.read(config, path));
    }

    @Override
    public @Nullable T read(FileConfig config, String path) throws CodecReadException {
        return this.loader.read(config, path);
    }

    @Override
    public T read(FileConfig config, String path, T defaultValue) {
        return this.readOptional(config, path).orElse(defaultValue);
    }

    @Override
    public void write(FileConfig config, String path, T value) {
        this.writer.write(config, path, value);
    }

    @FunctionalInterface
    public interface Loader<T> {

        @Nullable
        T read(FileConfig config, String path);
    }

    @FunctionalInterface
    public interface Writer<T> {

        void write(FileConfig config, String path, T value);
    }
}
