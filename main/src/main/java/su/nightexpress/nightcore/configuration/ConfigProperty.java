package su.nightexpress.nightcore.configuration;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.config.FileConfig;

public class ConfigProperty<T> {

    private final ConfigType<T> type;
    private final String        path;
    private final T             defaultValue;
    private final String[]      description;

    private T configuredValue;

    public ConfigProperty(@NonNull ConfigType<T> type, @NonNull String path, @NonNull T defaultValue, @Nullable String... description) {
        this.type = type;
        this.path = path;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    @NonNull
    public static <T> ConfigProperty<T> of(@NonNull ConfigType<T> type, @NonNull String path, @NonNull T defaultValue, @Nullable String... description) {
        return new ConfigProperty<>(type, path, defaultValue, description);
    }

    /**
     * Gets the configured value if it exists and is valid, otherwise returns the default value.
     */
    @NonNull
    public T get() {
        return Optional.ofNullable(this.configuredValue).orElse(this.defaultValue);
    }

    public void set(@NonNull T value) {
        this.configuredValue = value;
    }

    @NonNull
    @Deprecated
    public T read(@NonNull FileConfig config) {
        return this.loadOrWriteDefault(config);
    }

    public void writeDefaults(@NonNull FileConfig config) {
        if (!config.contains(this.path)) {
            this.writeValue(config, this.defaultValue);
        }

        if (this.description != null && this.description.length > 0 && Stream.of(this.description).anyMatch(Predicate.not(String::isBlank))) {
            config.setComments(this.path, this.description);
        }
    }

    @NonNull
    public T resolve(@NonNull FileConfig config) {
        return this.type.read(config, this.path, this.defaultValue);
    }

    @NonNull
    public T resolveWithDefaults(@NonNull FileConfig config) {
        this.writeDefaults(config);

        return this.resolve(config);
    }

    @NonNull
    @Deprecated
    public T loadOrWriteDefault(@NonNull FileConfig config) {
        return this.loadWithDefaults(config);
    }

    @NonNull
    public T load(@NonNull FileConfig config) {
        this.set(this.resolve(config));

        return this.get();
    }

    @NonNull
    public T loadWithDefaults(@NonNull FileConfig config) {
        this.writeDefaults(config);

        return this.load(config);
    }

    public void write(@NonNull FileConfig config) {
        this.writeValue(config, this.get());
    }

    public void writeValue(@NonNull FileConfig config, @NonNull T value) {
        this.type.write(config, this.path, value);
    }

    public void remove(@NonNull FileConfig config) {
        config.set(this.path, null);
    }

    @NonNull
    public String getPath() {
        return this.path;
    }

    @NonNull
    public String[] getDescription() {
        return this.description;
    }

    @NonNull
    public T getDefaultValue() {
        return this.defaultValue;
    }
}
