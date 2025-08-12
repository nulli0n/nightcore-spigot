package su.nightexpress.nightcore.locale.entry;

import org.bukkit.Keyed;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.LangValue;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.bridge.RegistryType;

import java.util.HashMap;
import java.util.Map;

public class RegistryLocale<E extends Keyed> extends LangEntry<RegistryLocale.Value<E>> {

    public RegistryLocale(@NotNull RegistryType<E> registry, @NotNull String path, @NotNull RegistryLocale.Value<E> defaultValue) {
        super((config, path1) -> Value.load(config, path1, registry), path, defaultValue);
    }

    @NotNull
    public static <E extends Keyed> RegistryLocale<E> create(@NotNull String path, @NotNull RegistryType<E> registry) {
        return new RegistryLocale<>(registry, path, new Value<>(new HashMap<>()));
    }

    @NotNull
    public String getLocalized(@NotNull E value) {
        return this.value.getLocalized(value);
    }

    public static class Value<E extends Keyed> implements LangValue {

        private final Map<String, String> localeMap;

        public Value(@NotNull Map<String, String> localeMap) {
            this.localeMap = localeMap;
        }

        @NotNull
        public static <E extends Keyed> Value<E> load(@NotNull FileConfig config, @NotNull String path, @NotNull RegistryType<E> registry) {
            Map<String, String> localeMap = new HashMap<>();

            BukkitThing.getAll(registry).forEach(keyed -> {
                String value = BukkitThing.getValue(keyed);
                String localized = StringUtil.capitalizeUnderscored(value);

                String text = ConfigValue.create(path + "." + value, localized).read(config);
                localeMap.put(value, text);
            });

            return new Value<>(localeMap);
        }

        @Override
        public void write(@NotNull FileConfig config, @NotNull String path) {
            this.localeMap.forEach((key, value) -> {
                config.set(path + "." + key, value);
            });
        }

        @NotNull
        public String getLocalized(@NotNull E keyed) {
            String value = BukkitThing.getValue(keyed);

            return this.localeMap.getOrDefault(value, value);
        }
    }
}
