package su.nightexpress.nightcore.configuration;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class AbstractConfig {

    protected final List<ConfigProperty<?>> properties = new ArrayList<>();

    protected final <T> ConfigProperty<T> addProperty(ConfigType<T> type, String path, T defaultValue,
                                                      @Nullable String... description) {
        return this.addProperty(ConfigProperty.of(type, path, defaultValue, description));
    }

    protected final <T> ConfigProperty<T> addProperty(ConfigProperty<T> property) {
        this.properties.add(property);
        return property;
    }

    public void load(FileConfig config) {
        this.properties.forEach(configProperty -> configProperty.loadWithDefaults(config));
    }
}
