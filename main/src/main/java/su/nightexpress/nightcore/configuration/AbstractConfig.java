package su.nightexpress.nightcore.configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.ArrayList;
import java.util.List;

public class AbstractConfig {

    protected final List<ConfigProperty<?>> properties = new ArrayList<>();

    @NotNull
    protected final <T> ConfigProperty<T> addProperty(@NotNull ConfigType<T> type, @NotNull String path, @NotNull T defaultValue, @Nullable String... description) {
        return this.addProperty(ConfigProperty.of(type, path, defaultValue, description));
    }

    @NotNull
    protected final <T> ConfigProperty<T> addProperty(@NotNull ConfigProperty<T> property) {
        this.properties.add(property);
        return property;
    }

    public void load(@NotNull FileConfig config) {
        this.properties.forEach(configProperty -> configProperty.loadWithDefaults(config));
    }
}
