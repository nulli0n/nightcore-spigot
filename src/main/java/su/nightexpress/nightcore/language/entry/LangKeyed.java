package su.nightexpress.nightcore.language.entry;

import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class LangKeyed<E extends Keyed> implements LangElement {

    private final String              path;
    private final Registry<E>         registry;
    private final Map<String, String> localeMap;

    public LangKeyed(@NotNull String path, @NotNull Registry<E> registry) {
        this.path = path;
        this.registry = registry;
        this.localeMap = new HashMap<>();
    }

    @NotNull
    public static <E extends Keyed> LangKeyed<E> of(@NotNull String path, @NotNull Registry<E> registry) {
        return new LangKeyed<>(path, registry);
    }

    @Override
    public void write(@NotNull FileConfig config) {

    }

    public void load(@NotNull NightCorePlugin plugin) {
        FileConfig config = plugin.getLang();

        BukkitThing.allFromRegistry(this.registry).forEach(keyed -> {
            String namespace = BukkitThing.toString(keyed);
            String localized = StringUtil.capitalizeUnderscored(namespace);

            String text = ConfigValue.create(this.path + "." + namespace, localized).read(config);
            this.localeMap.put(namespace, text);
        });
    }

    @NotNull
    public String getLocalized(@NotNull E keyed) {
        String key = BukkitThing.toString(keyed);

        return this.localeMap.getOrDefault(key, key);
    }
}
