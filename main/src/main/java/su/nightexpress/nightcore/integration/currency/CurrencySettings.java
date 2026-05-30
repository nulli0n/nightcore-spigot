package su.nightexpress.nightcore.integration.currency;

import org.bukkit.Material;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.Placeholders;
import su.nightexpress.nightcore.util.bukkit.NightItem;

public class CurrencySettings implements Writeable {

    private final String    name;
    private final String    format;
    private final NightItem icon;

    public CurrencySettings(@NonNull String name, @NonNull String format, @NonNull NightItem icon) {
        this.name = name;
        this.format = format;
        this.icon = icon.copy();
    }

    @NonNull
    public static CurrencySettings createDefault(@NonNull String name, @NonNull NightItem icon) {
        String format = Placeholders.GENERIC_AMOUNT + " " + Placeholders.GENERIC_NAME;
        return new CurrencySettings(name, format, icon);
    }

    @NonNull
    public static CurrencySettings load(@NonNull FileConfig config, @NonNull String path) {
        String name = ConfigValue.create(path + ".Name", "Null").read(config);
        String format = ConfigValue.create(path + ".Format", Placeholders.GENERIC_AMOUNT).read(config);
        NightItem icon = ConfigValue.create(path + ".Icon", NightItem.fromType(Material.SUNFLOWER)).read(config);

        return new CurrencySettings(name, format, icon);
    }

    @Override
    public void write(@NonNull FileConfig config, @NonNull String path) {
        config.set(path + ".Name", this.name);
        config.set(path + ".Format", this.format);
        config.set(path + ".Icon", this.icon);
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public String getFormat() {
        return this.format;
    }

    @NonNull
    public NightItem getIcon() {
        return this.icon.copy();
    }
}
