package su.nightexpress.nightcore.language.entry;

import org.bukkit.Keyed;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.bridge.RegistryType;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class LangRegistry<E extends Keyed> implements LangElement {

    private final String              path;
    private final RegistryType<E>     registry;
    private final Map<String, String> localeMap;

    public LangRegistry(@NonNull String path, @NonNull RegistryType<E> registry) {
        this.path = path;
        this.registry = registry;
        this.localeMap = new HashMap<>();
    }

    @NonNull
    public static <E extends Keyed> LangRegistry<E> of(@NonNull String path, @NonNull RegistryType<E> registry) {
        return new LangRegistry<>(path, registry);
    }

    @Override
    public void write(@NonNull FileConfig config) {

    }

    public void load(@NonNull NightCorePlugin plugin) {
        this.load(plugin.getLang());
    }

    @Override
    public void load(@NonNull FileConfig config) {
        BukkitThing.getAll(this.registry).forEach(keyed -> {
            String value = BukkitThing.getValue(keyed);
            String localized = StringUtil.capitalizeUnderscored(value);

            String text = ConfigValue.create(this.path + "." + value, localized).read(config);
            this.localeMap.put(value, text);
        });
    }

    @NonNull
    public String getLocalized(@NonNull E keyed) {
        String value = BukkitThing.getValue(keyed);

        return this.localeMap.getOrDefault(value, value);
    }
}
