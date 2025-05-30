package su.nightexpress.nightcore.language.entry;

import org.bukkit.Keyed;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.bridge.RegistryType;

import java.util.HashMap;
import java.util.Map;

public class LangRegistry<E extends Keyed> implements LangElement {

    private final String              path;
    private final RegistryType<E>     registry;
    private final Map<String, String> localeMap;

    public LangRegistry(@NotNull String path, @NotNull RegistryType<E> registry) {
        this.path = path;
        this.registry = registry;
        this.localeMap = new HashMap<>();
    }

    @NotNull
    public static <E extends Keyed> LangRegistry<E> of(@NotNull String path, @NotNull RegistryType<E> registry) {
        return new LangRegistry<>(path, registry);
    }

    @Override
    public void write(@NotNull FileConfig config) {

    }

    public void load(@NotNull NightCorePlugin plugin) {
        FileConfig config = plugin.getLang();

        BukkitThing.getAll(this.registry).forEach(keyed -> {
            String value = BukkitThing.getValue(keyed);
            String localized = StringUtil.capitalizeUnderscored(value);

            String text = ConfigValue.create(this.path + "." + value, localized).read(config);
            this.localeMap.put(value, text);
        });
    }

    @NotNull
    public String getLocalized(@NotNull E keyed) {
        String value = BukkitThing.getValue(keyed);

        return this.localeMap.getOrDefault(value, value);
    }
}
