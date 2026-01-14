package su.nightexpress.nightcore.configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ConfigProperty<T> {

    private final ConfigType<T> type;
    private final String        path;
    private final T             defaultValue;
    private final String[]      description;

    private T configuredValue;

    public ConfigProperty(@NotNull ConfigType<T> type, @NotNull String path, @NotNull T defaultValue, @Nullable String... description) {
        this.type = type;
        this.path = path;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    @NotNull
    public static <T> ConfigProperty<T> of(@NotNull ConfigType<T> type, @NotNull String path, @NotNull T defaultValue, @Nullable String... description) {
        return new ConfigProperty<>(type, path, defaultValue, description);
    }

    /**
     * Gets the configured value if it exists and is valid, otherwise returns the default value.
     */
    @NotNull
    public T get() {
        return Optional.ofNullable(this.configuredValue).orElse(this.defaultValue);
    }

    public void set(@NotNull T value) {
        this.configuredValue = value;
    }

    @NotNull
    @Deprecated
    public T read(@NotNull FileConfig config) {
        return this.loadOrWriteDefault(config);
    }

    public void writeDefaults(@NotNull FileConfig config) {
        if (!config.contains(this.path)) {
            this.writeValue(config, this.defaultValue);
        }

        if (this.description != null && this.description.length > 0 && Stream.of(this.description).anyMatch(Predicate.not(String::isBlank))) {
            config.setComments(this.path, this.description);
        }
    }

    @NotNull
    public T resolve(@NotNull FileConfig config) {
        return this.type.read(config, this.path, this.defaultValue);
    }

    @NotNull
    public T resolveWithDefaults(@NotNull FileConfig config) {
        this.writeDefaults(config);

        return this.resolve(config);
    }

    @NotNull
    @Deprecated
    public T loadOrWriteDefault(@NotNull FileConfig config) {
        return this.loadWithDefaults(config);
    }

    @NotNull
    public T load(@NotNull FileConfig config) {
        this.set(this.resolve(config));

        return this.get();
    }

    @NotNull
    public T loadWithDefaults(@NotNull FileConfig config) {
        this.writeDefaults(config);

        return this.load(config);
    }

    public void write(@NotNull FileConfig config) {
        this.writeValue(config, this.get());
    }

    public void writeValue(@NotNull FileConfig config, @NotNull T value) {
        this.type.write(config, this.path, value);
    }

    public void remove(@NotNull FileConfig config) {
        config.set(this.path, null);
    }

    @NotNull
    public String getPath() {
        return this.path;
    }

    @NotNull
    public String[] getDescription() {
        return this.description;
    }

    @NotNull
    public T getDefaultValue() {
        return this.defaultValue;
    }
}
