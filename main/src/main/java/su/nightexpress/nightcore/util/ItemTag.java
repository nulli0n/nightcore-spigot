package su.nightexpress.nightcore.util;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.nbt.NbtUtil;

public class ItemTag implements Writeable {

    private static final String EMPTY = "{}";

    private final String tag;
    private final int    dataVersion;

    public ItemTag(@NonNull String tag, int dataVersion) {
        this.tag = tag;
        this.dataVersion = dataVersion;
    }

    @NonNull
    public static ItemTag read(@NonNull FileConfig config, @NonNull String path) {
        String value = ConfigValue.create(path + ".Value", EMPTY).read(config);
        int dataVersion = ConfigValue.create(path + ".DataVersion", -1).read(config);

        return new ItemTag(value, dataVersion);
    }

    @Override
    public void write(@NonNull FileConfig config, @NonNull String path) {
        config.set(path + ".Value", this.tag);
        config.set(path + ".DataVersion", this.dataVersion);
    }

    @NonNull
    public static ItemTag of(@NonNull ItemStack itemStack) {
        Object compoundTag = NbtUtil.tagFromItemStack(itemStack);

        return new ItemTag(compoundTag.toString(), Version.getCurrent().getDataVersion());
    }

    @NonNull
    public static String getTagString(@NonNull ItemStack itemStack) {
        return of(itemStack).getTag();
    }

    @NonNull
    public static String getTagStringEncoded(@NonNull ItemStack itemStack) {
        return Strings.toBase64(getTagString(itemStack));
    }

    @Nullable
    public ItemStack getItemStack() {
        if (this.isEmpty()) return null;

        return NbtUtil.tagToItemStack(this.tag, this.dataVersion);
    }

    public boolean isEmpty() {
        return this.tag.isBlank() || this.tag.equalsIgnoreCase(EMPTY);
    }

    @NonNull
    public String getTag() {
        return this.tag;
    }

    public int getDataVersion() {
        return this.dataVersion;
    }
}
