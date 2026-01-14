package su.nightexpress.nightcore.locale.entry;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.LangValue;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class EnumLocale<E extends Enum<E>> extends LangEntry<EnumLocale.Value<E>> {

    public EnumLocale(@NotNull Class<E> clazz, @NotNull String path, @NotNull Value<E> defaultValue) {
        super((config, path1) -> Value.load(config, path1, clazz), path, defaultValue);
    }

    @NotNull
    public static <E extends Enum<E>> EnumLocale<E> create(@NotNull String path, @NotNull Class<E> clazz) {
        return create(path, clazz, con -> StringUtil.capitalizeUnderscored(con.name()));
    }

    @NotNull
    public static <E extends Enum<E>> EnumLocale<E> create(@NotNull String path, @NotNull Class<E> clazz, @NotNull Function<E, String> defaultMapper) {
        Map<E, String> localeMap = new HashMap<>();

        Stream.of(clazz.getEnumConstants()).forEach(con -> {
            localeMap.put(con, defaultMapper.apply(con));
        });

        return new EnumLocale<>(clazz, path, new Value<>(localeMap));
    }

    @NotNull
    public String getLocalized(@NotNull E con) {
        return this.value.getLocalized(con);
    }

    public static class Value<E extends Enum<E>> implements LangValue {

        private final Map<E, String> localeMap;

        public Value(@NotNull Map<E, String> localeMap) {
            this.localeMap = localeMap;
        }

        @NotNull
        public static <E extends Enum<E>> Value<E> load(@NotNull FileConfig config, @NotNull String path, @NotNull Class<E> clazz) {
            Map<E, String> localeMap = new HashMap<>();

            Stream.of(clazz.getEnumConstants()).forEach(con -> {
                String def = StringUtil.capitalizeUnderscored(con.name());
                String text = ConfigValue.create(path + "." + con.name(), def).read(config);
                localeMap.put(con, text);
            });

            return new Value<>(localeMap);
        }

        @Override
        public void write(@NotNull FileConfig config, @NotNull String path) {
            this.localeMap.forEach((con, value) -> {
                config.set(path + "." + con.name(), value);
            });
        }

        @NotNull
        public String getLocalized(@NotNull E con) {
            return this.localeMap.getOrDefault(con, con.name());
        }
    }
}
