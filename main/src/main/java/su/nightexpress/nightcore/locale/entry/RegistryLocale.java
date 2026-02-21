package su.nightexpress.nightcore.locale.entry;

import org.bukkit.Keyed;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.ConfigType;
import su.nightexpress.nightcore.configuration.ConfigTypes;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.LangValue;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.bridge.RegistryType;

import java.util.HashMap;
import java.util.Map;

public class RegistryLocale<E extends Keyed> extends LangEntry<RegistryLocale.Value<E>> {

    public RegistryLocale(@NonNull ConfigType<Value<E>> type, @NonNull String path, RegistryLocale.@NonNull Value<E> defaultValue) {
        super(type, path, defaultValue);
    }

    @NonNull
    public static <E extends Keyed> RegistryLocale<E> create(@NonNull String path, @NonNull RegistryType<E> registry) {
        return new RegistryLocale<>(createType(registry), path, new Value<>(new HashMap<>()));
    }
    
    @NonNull
    private static <E extends Keyed> ConfigType<Value<E>> createType(@NonNull RegistryType<E> registryType) {
        return ConfigType.of((config, path1) -> Value.load(config, path1, registryType), FileConfig::set);
    }

    @NonNull
    public String getLocalized(@NonNull E value) {
        return this.value.getLocalized(value);
    }

    public static class Value<E extends Keyed> implements LangValue {

        private final Map<String, String> localeMap;

        public Value(@NonNull Map<String, String> localeMap) {
            this.localeMap = localeMap;
        }

        @NonNull
        public static <E extends Keyed> Value<E> load(@NonNull FileConfig config, @NonNull String path, @NonNull RegistryType<E> registry) {
            Map<String, String> localeMap = new HashMap<>();

            BukkitThing.getAll(registry).forEach(keyed -> {
                String value = BukkitThing.getValue(keyed);
                String localized = StringUtil.capitalizeUnderscored(value);

                String text = config.get(ConfigTypes.STRING, path + "." + value, localized);
                localeMap.put(value, text);
            });

            return new Value<>(localeMap);
        }

        @Override
        public void write(@NonNull FileConfig config, @NonNull String path) {
            this.localeMap.forEach((key, value) -> {
                config.set(path + "." + key, value);
            });
        }

        @NonNull
        public String getLocalized(@NonNull E keyed) {
            String value = BukkitThing.getValue(keyed);

            return this.localeMap.getOrDefault(value, value);
        }
    }
}
