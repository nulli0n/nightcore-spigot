package su.nightexpress.nightcore.language.entry;

import org.jspecify.annotations.NonNull;
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

    private final String   path;
    private final Class<E> clazz;

    private final Map<E, String> defaultsMap;
    private final Map<E, String> localeMap;

    public LangEnum(@NonNull String path, @NonNull Class<E> clazz, @NonNull Map<E, String> defaultsMap) {
        this.path = path;
        this.clazz = clazz;
        this.defaultsMap = defaultsMap;
        this.localeMap = new HashMap<>(defaultsMap);
    }

    @NonNull
    public static <E extends Enum<E>> LangEnum<E> of(@NonNull String path, @NonNull Class<E> clazz) {
        return of(path, clazz, map -> {
        });
    }

    @NonNull
    public static <E extends Enum<E>> LangEnum<E> of(@NonNull String path, @NonNull Class<E> clazz,
                                                     @NonNull Consumer<Map<E, String>> consumer) {
        Map<E, String> defaults = new HashMap<>();
        consumer.accept(defaults);

        return new LangEnum<>(path, clazz, defaults);
    }

    @Override
    public void write(@NonNull FileConfig config) {

    }

    public void load(@NonNull NightCorePlugin plugin) {
        this.load(plugin.getLang());
    }

    @Override
    public void load(@NonNull FileConfig config) {
        Stream.of(this.clazz.getEnumConstants()).forEach(con -> {
            String def = this.defaultsMap.getOrDefault(con, StringUtil.capitalizeUnderscored(con.name()));
            String text = ConfigValue.create(this.path + "." + con.name(), def).read(config);
            this.localeMap.put(con, text);
        });
    }

    @NonNull
    public String getLocalized(@NonNull E con) {
        return this.localeMap.getOrDefault(con, con.name());
    }
}
