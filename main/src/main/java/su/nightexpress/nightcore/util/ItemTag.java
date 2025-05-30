package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.core.CoreConfig;

public class ItemTag implements Writeable {

    private static final String EMPTY = "{}";

    private final String tag;
    private final int dataVersion;

    public ItemTag(@NotNull String tag, int dataVersion) {
        this.tag = tag;
        this.dataVersion = dataVersion;
    }

    @NotNull
    public static ItemTag read(@NotNull FileConfig config, @NotNull String path) {
        String value = ConfigValue.create(path + ".Value", EMPTY).read(config);
        int dataVersion = ConfigValue.create(path + ".DataVersion", CoreConfig.DATA_FIXER_MISSING_VERSION.get()).read(config);

        return new ItemTag(value, dataVersion);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Value", this.tag);
        config.set(path + ".DataVersion", this.dataVersion);
    }

    public boolean isEmpty() {
        return this.tag.isBlank() || this.tag.equalsIgnoreCase(EMPTY);
    }

    @NotNull
    public String getTag() {
        return this.tag;
    }

    public int getDataVersion() {
        return this.dataVersion;
    }
}
