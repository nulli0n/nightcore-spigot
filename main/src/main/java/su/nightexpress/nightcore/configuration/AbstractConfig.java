package su.nightexpress.nightcore.configuration;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.config.FileConfig;

public class AbstractConfig {

    protected final List<ConfigProperty<?>> properties = new ArrayList<>();

    @NonNull
    protected final <T> ConfigProperty<T> addProperty(@NonNull ConfigType<T> type, @NonNull String path, @NonNull T defaultValue, @Nullable String... description) {
        return this.addProperty(ConfigProperty.of(type, path, defaultValue, description));
    }

    @NonNull
    protected final <T> ConfigProperty<T> addProperty(@NonNull ConfigProperty<T> property) {
        this.properties.add(property);
        return property;
    }

    public void load(@NonNull FileConfig config) {
        this.properties.forEach(configProperty -> configProperty.loadWithDefaults(config));
    }
}
