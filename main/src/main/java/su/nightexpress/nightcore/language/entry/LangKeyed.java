package su.nightexpress.nightcore.language.entry;

import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class LangKeyed<E extends Keyed> implements LangElement {

    private final String              path;
    private final Registry<E>         registry;
    private final Map<String, String> localeMap;

    public LangKeyed(@NonNull String path, @NonNull Registry<E> registry) {
        this.path = path;
        this.registry = registry;
        this.localeMap = new HashMap<>();
    }

    @NonNull
    public static <E extends Keyed> LangKeyed<E> of(@NonNull String path, @NonNull Registry<E> registry) {
        return new LangKeyed<>(path, registry);
    }

    @Override
    public void write(@NonNull FileConfig config) {

    }

    public void load(@NonNull NightCorePlugin plugin) {
        this.load(plugin.getLang());
    }

    @Override
    public void load(@NonNull FileConfig config) {
        BukkitThing.allFromRegistry(this.registry).forEach(keyed -> {
            String namespace = BukkitThing.toString(keyed);
            String localized = StringUtil.capitalizeUnderscored(namespace);

            String text = ConfigValue.create(this.path + "." + namespace, localized).read(config);
            this.localeMap.put(namespace, text);
        });
    }

    @NonNull
    public String getLocalized(@NonNull E keyed) {
        String key = BukkitThing.toString(keyed);

        return this.localeMap.getOrDefault(key, key);
    }
}
