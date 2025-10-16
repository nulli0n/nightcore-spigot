package su.nightexpress.nightcore.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.nbt.NbtUtil;

import java.io.*;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Deprecated
public class ItemNbt {

    private static final Class<?> CLS_COMPOUND_TAG = Reflex.safeClass("net.minecraft.nbt", "CompoundTag", "NBTTagCompound");
    private static final Class<?> CLS_NBT_IO       = Reflex.safeClass("net.minecraft.nbt", "NbtIo", "NBTCompressedStreamTools");

    private static final Method NBT_IO_WRITE = Reflex.getMethod(CLS_NBT_IO, "write", "a", CLS_COMPOUND_TAG, DataOutput.class);
    private static final Method NBT_IO_READ  = Reflex.getMethod(CLS_NBT_IO, "read", "a", DataInput.class);

    @NotNull
    @Deprecated
    public static List<String> compress(@NotNull ItemStack[] items) {
        return compress(Arrays.asList(items));
    }

    @NotNull
    @Deprecated
    public static List<String> compress(@NotNull List<ItemStack> items) {
        return new ArrayList<>(items.stream().map(ItemNbt::compress).filter(Objects::nonNull).toList());
    }

    @Nullable
    @Deprecated
    public static String compress(@NotNull ItemStack item) {
        Object compoundTag = toCompoundTag(item);
        if (compoundTag == null) return null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(outputStream);
        Reflex.invokeMethod(NBT_IO_WRITE, null, compoundTag, dataOutput);
        return new BigInteger(1, outputStream.toByteArray()).toString(32);
    }

    @Nullable
    @Deprecated
    public static ItemStack decompress(@NotNull String compressed) {
        if (compressed.isBlank()) return null;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(compressed, 32).toByteArray());
        DataInputStream dataInput = new DataInputStream(inputStream);

        Object compoundTag = Reflex.invokeMethod(NBT_IO_READ, null, dataInput);
        return compoundTag == null ? null : fromCompoundTag(compoundTag, Version.MC_1_21.getDataVersion());
    }

    @Deprecated
    public static ItemStack[] decompress(@NotNull List<String> list) {
        List<ItemStack> items = list.stream().map(ItemNbt::decompress).filter(Objects::nonNull).toList();
        return items.toArray(new ItemStack[list.size()]);
    }

    @Nullable
    @Deprecated
    public static String getTagString(@NotNull ItemStack item) {
        Object compoundTag = toCompoundTag(item);
        if (compoundTag == null) return null;

        return compoundTag.toString();
    }

    @Nullable
    @Deprecated
    public static ItemStack fromTagString(@NotNull String tagString) {
        return new ItemTag(tagString, Version.MC_1_21.getDataVersion()).getItemStack();
    }

    @Nullable
    @Deprecated
    public static ItemStack fromTag(@NotNull ItemTag itemTag) {
        return itemTag.getItemStack();
    }

    @Nullable
    @Deprecated
    public static ItemTag getTag(@NotNull ItemStack item) {
        try {
            return ItemTag.of(item);
        }
        catch (IllegalStateException e) {
            return null;
        }
    }

    @Nullable
    @Deprecated
    public static ItemStack getHoverEventItem(@NotNull String value) {
        ItemStack itemStack = null;

        try {
            itemStack = Bukkit.getItemFactory().createItemStack(value);
        }
        catch (IllegalArgumentException exception) {
            try {
                itemStack = ItemNbt.decompress(value);
            }
            catch (NumberFormatException ignored) {

            }
        }
        if (itemStack != null && itemStack.getType().isAir()) return null;

        return itemStack;
    }

    @Nullable
    @Deprecated
    private static Object toCompoundTag(@NotNull ItemStack bukkitStack) {
        if (bukkitStack.getType().isAir() || bukkitStack.getAmount() <= 0) return null;

        return NbtUtil.tagFromItemStack(bukkitStack);
    }

    @Nullable
    @Deprecated
    private static ItemStack fromCompoundTag(@NotNull Object tag, int sourceVersion) {
        return NbtUtil.tagToItemStack(tag, sourceVersion);
    }
}
