package su.nightexpress.nightcore.locale.entry;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.ConfigType;
import su.nightexpress.nightcore.configuration.ConfigTypes;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.LangValue;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class EnumLocale<E extends Enum<E>> extends LangEntry<EnumLocale.Value<E>> {

    public EnumLocale(@NonNull ConfigType<Value<E>> type, @NonNull String path, @NonNull Value<E> defaultValue) {
        super(type, path, defaultValue);
    }

    @NonNull
    public static <E extends Enum<E>> EnumLocale<E> create(@NonNull String path, @NonNull Class<E> clazz) {
        return create(path, clazz, con -> StringUtil.capitalizeUnderscored(con.name()));
    }
    
    private static <E extends Enum<E>> ConfigType<Value<E>> createType(@NonNull Class<E> type) {
        return ConfigType.of((config, path) -> Value.load(config, path, type), FileConfig::set);
    }

    @NonNull
    public static <E extends Enum<E>> EnumLocale<E> create(@NonNull String path, @NonNull Class<E> clazz, @NonNull Function<E, String> defaultMapper) {
        Map<E, String> localeMap = new HashMap<>();

        Stream.of(clazz.getEnumConstants()).forEach(con -> {
            localeMap.put(con, defaultMapper.apply(con));
        });

        return new EnumLocale<>(createType(clazz), path, new Value<>(localeMap));
    }

    @NonNull
    public String getLocalized(@NonNull E con) {
        return this.value.getLocalized(con);
    }

    public static class Value<E extends Enum<E>> implements LangValue {

        private final Map<E, String> localeMap;

        public Value(@NonNull Map<E, String> localeMap) {
            this.localeMap = localeMap;
        }

        @NonNull
        public static <E extends Enum<E>> Value<E> load(@NonNull FileConfig config, @NonNull String path, @NonNull Class<E> clazz) {
            Map<E, String> localeMap = new HashMap<>();

            Stream.of(clazz.getEnumConstants()).forEach(con -> {
                String def = StringUtil.capitalizeUnderscored(con.name());
                String text = config.get(ConfigTypes.STRING, path + "." + con.name(), def);
                localeMap.put(con, text);
            });

            return new Value<>(localeMap);
        }

        @Override
        public void write(@NonNull FileConfig config, @NonNull String path) {
            this.localeMap.forEach((con, value) -> {
                config.set(path + "." + con.name(), value);
            });
        }

        @NonNull
        public String getLocalized(@NonNull E con) {
            return this.localeMap.getOrDefault(con, con.name());
        }
    }
}
