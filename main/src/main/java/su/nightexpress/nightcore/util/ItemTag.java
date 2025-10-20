package su.nightexpress.nightcore.util;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.nbt.NbtUtil;

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
        int dataVersion = ConfigValue.create(path + ".DataVersion", -1).read(config);

        return new ItemTag(value, dataVersion);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Value", this.tag);
        config.set(path + ".DataVersion", this.dataVersion);
    }

    @NotNull
    public static ItemTag of(@NotNull ItemStack itemStack) {
        Object compoundTag = NbtUtil.tagFromItemStack(itemStack);

        return new ItemTag(compoundTag.toString(), Version.getCurrent().getDataVersion());
    }

    @NotNull
    public static String getTagString(@NotNull ItemStack itemStack) {
        return of(itemStack).getTag();
    }

    @NotNull
    public static String getTagStringEncoded(@NotNull ItemStack itemStack) {
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

    @NotNull
    public String getTag() {
        return this.tag;
    }

    public int getDataVersion() {
        return this.dataVersion;
    }
}
