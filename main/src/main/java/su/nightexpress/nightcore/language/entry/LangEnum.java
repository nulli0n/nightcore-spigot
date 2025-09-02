package su.nightexpress.nightcore.language.entry;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Deprecated
public class LangEnum<E extends Enum<E>> implements LangElement {

    private final String path;
    private final Class<E> clazz;

    private final Map<E, String> defaultsMap;
    private final Map<E, String> localeMap;

    public LangEnum(@NotNull String path, @NotNull Class<E> clazz, @NotNull Map<E, String> defaultsMap) {
        this.path = path;
        this.clazz = clazz;
        this.defaultsMap = defaultsMap;
        this.localeMap = new HashMap<>(defaultsMap);
    }

    @NotNull
    public static <E extends Enum<E>> LangEnum<E> of(@NotNull String path, @NotNull Class<E> clazz) {
        return of(path, clazz, map -> {});
    }

    @NotNull
    public static <E extends Enum<E>> LangEnum<E> of(@NotNull String path, @NotNull Class<E> clazz, @NotNull Consumer<Map<E, String>> consumer) {
        Map<E, String> defaults = new HashMap<>();
        consumer.accept(defaults);

        return new LangEnum<>(path, clazz, defaults);
    }

    @Override
    public void write(@NotNull FileConfig config) {

    }

    public void load(@NotNull NightCorePlugin plugin) {
        this.load(plugin.getLang());
    }

    @Override
    public void load(@NotNull FileConfig config) {
        Stream.of(this.clazz.getEnumConstants()).forEach(con -> {
            String def = this.defaultsMap.getOrDefault(con, StringUtil.capitalizeUnderscored(con.name()));
            String text = ConfigValue.create(this.path + "." + con.name(), def).read(config);
            this.localeMap.put(con, text);
        });
    }

    @NotNull
    public String getLocalized(@NotNull E con) {
        return this.localeMap.getOrDefault(con, con.name());
    }
}
