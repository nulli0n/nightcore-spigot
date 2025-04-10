package su.nightexpress.nightcore.util;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.core.CoreConfig;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.*;

public class ItemNbt {

    private static final Class<?> CLS_MINECRAFT_ITEM_STACK = Reflex.getNMSClass("net.minecraft.world.item", "ItemStack");

    private static final Class<?> CLS_CRAFT_ITEM_STACK = Reflex.getNMSClass(Version.CRAFTBUKKIT_PACKAGE + ".inventory", "CraftItemStack");

    private static final Class<?> CLS_TAG          = Reflex.getNMSClass("net.minecraft.nbt", "Tag", "NBTBase");
    private static final Class<?> CLS_COMPOUND_TAG = Reflex.getNMSClass("net.minecraft.nbt", "CompoundTag", "NBTTagCompound");
    private static final Class<?> CLS_NBT_IO       = Reflex.getNMSClass("net.minecraft.nbt", "NbtIo", "NBTCompressedStreamTools");
    private static final Class<?> CLS_TAG_PARSER   = Reflex.getNMSClass("net.minecraft.nbt", "TagParser", "MojangsonParser");
    private static final Class<?> CLS_NBT_OPS      = Reflex.getNMSClass("net.minecraft.nbt", "NbtOps", "DynamicOpsNBT");

    private static final Class<?> CLS_DATA_FIXERS = Reflex.getNMSClass("net.minecraft.util.datafix", "DataFixers", "DataConverterRegistry");
    private static final Class<?> CLS_REFERENCES  = Reflex.getNMSClass("net.minecraft.util.datafix.fixes", "References", "DataConverterTypes");

    private static Method mCraftItemStackAsNmsCopy;
    private static Method mCraftItemStackAsBukkitCopy;

    private static Method mNbtIoWrite;
    private static Method mNbtIoRead;
    private static Method mTagParserParseTag;

    //private static int       DATA_FIXER_SOURCE_VERSION;
    //private static int       DATA_FIXER_TARGET_VERSION;
    private static DataFixer dataFixer;
    private static Object nbtOps;
    private static Object itemStackReference;

    // For 1.20.6+
    private static Method mItemStackParse;
    private static Method mItemStackSave;

    // For 1.20.4 and below.
    private static Constructor<?> conNewCompoundTag;
    private static Method mMinecraftItemStackOf;
    private static Method mMinecraftItemStackSave;

    // Base

    private static boolean useRegistry;
    private static Object registryAccess;
    private static boolean loaded;

    public static void load(@NotNull NightCore core) {
        if (CLS_CRAFT_ITEM_STACK == null) return;
        if (CLS_MINECRAFT_ITEM_STACK == null) return;
        if (CLS_NBT_IO == null) return;
        if (CLS_NBT_OPS == null) return;
        if (CLS_TAG_PARSER == null) return;
        if (CLS_TAG == null) return;
        if (CLS_COMPOUND_TAG == null) return;
        if (CLS_DATA_FIXERS == null) return;
        if (CLS_REFERENCES == null) return;

        // Load vital components for all versions.
        mCraftItemStackAsNmsCopy    = Reflex.getMethod(CLS_CRAFT_ITEM_STACK, "asNMSCopy", ItemStack.class);
        mCraftItemStackAsBukkitCopy = Reflex.getMethod(CLS_CRAFT_ITEM_STACK, "asBukkitCopy", CLS_MINECRAFT_ITEM_STACK);

        if (mCraftItemStackAsNmsCopy == null || mCraftItemStackAsBukkitCopy == null) {
            core.error("[Item NBT] Could not find CraftItemStack methods: #asNMSCopy, #asBukkitCopy.");
            return;
        }

        mNbtIoWrite = Reflex.getMethod(CLS_NBT_IO, "a", CLS_COMPOUND_TAG, DataOutput.class);
        mNbtIoRead = Reflex.getMethod(CLS_NBT_IO, "a", DataInput.class);
        mTagParserParseTag = Reflex.getMethod(CLS_TAG_PARSER, "a", String.class);

        if (mNbtIoWrite == null || mNbtIoRead == null || mTagParserParseTag == null) {
            core.error("[Item NBT] Could not find NbtIo or TagParser methods.");
            return;
        }

        // Load DataFixer components.
        //DATA_FIXER_SOURCE_VERSION = 4189;//3700; // 1.20.4
        //DATA_FIXER_TARGET_VERSION = 4325; // 1.21.5 or 1.21.4

        Method mGetDataFixer = Reflex.getMethod(CLS_DATA_FIXERS, "a");
        if (mGetDataFixer == null) {
            core.error("[Item NBT] Could not load DataFixer components.");
            return;
        }

        dataFixer = (DataFixer) Reflex.invokeMethod(mGetDataFixer, CLS_DATA_FIXERS);
        nbtOps = Reflex.getFieldValue(CLS_NBT_OPS, "a");
        itemStackReference = Reflex.getFieldValue(CLS_REFERENCES, "t");

        // Load version specific item parsers.
        if (Version.isAtLeast(Version.MC_1_20_6)) {
            useRegistry = true;

            Class<?> clsCraftRegistry = Reflex.getNMSClass(Version.CRAFTBUKKIT_PACKAGE, "CraftRegistry");
            if (clsCraftRegistry == null) {
                core.error("[Item NBT] Could not find CraftRegistry.");
                return;
            }

            Method mGetMinecraftRegistry = Reflex.getMethod(clsCraftRegistry, "getMinecraftRegistry");
            if (mGetMinecraftRegistry == null) {
                core.error("[Item NBT] Could not find CraftRegistry#getMinecraftRegistry.");
                return;
            }

            registryAccess = Reflex.invokeMethod(mGetMinecraftRegistry, null);
            if (registryAccess == null) {
                core.error("[Item NBT] Registry Access is null.");
                return;
            }

            Class<?> clsHolderLookupProvider = Reflex.getNMSClass("net.minecraft.core", "HolderLookup$a"); // HolderLookup.Provider
            if (clsHolderLookupProvider == null) {
                core.error("[Item NBT] Could not find MinecraftServer or HolderLookup#Provider.");
                return;
            }

            mItemStackParse = Reflex.getMethod(CLS_MINECRAFT_ITEM_STACK, "a", clsHolderLookupProvider, CLS_TAG);
            mItemStackSave = Reflex.getMethod(CLS_MINECRAFT_ITEM_STACK, "a", clsHolderLookupProvider);

            if (mItemStackParse == null || mItemStackSave == null) {
                core.error("[Item NBT] Could not find ItemStack#parse or ItemStack#save methods.");
                return;
            }
        }
        else {
            conNewCompoundTag = Reflex.getConstructor(CLS_COMPOUND_TAG);
            mMinecraftItemStackOf = Reflex.getMethod(CLS_MINECRAFT_ITEM_STACK, "a", CLS_COMPOUND_TAG);
            mMinecraftItemStackSave = Reflex.getMethod(CLS_MINECRAFT_ITEM_STACK, "b", CLS_COMPOUND_TAG);

            if (conNewCompoundTag == null || mMinecraftItemStackOf == null || mMinecraftItemStackSave == null) {
                core.error("[Item NBT] Could not find new CompoundTag() or ItemStack#of or ItemStack#save.");
                return;
            }
        }

        loaded = true;

        if (!test()) {
            core.error("[Item NBT] Compression test failed.");
            loaded = false;
        }
    }

    public static boolean test() {
        ItemStack testItem = new ItemStack(Material.DIAMOND_SWORD);
        ItemUtil.editMeta(testItem, meta -> {
            meta.setDisplayName("Test Item");
            meta.setLore(Lists.newList("Test Lore 1", "Test Lore 2", "Test Lore 3"));
            meta.addEnchant(Enchantment.FIRE_ASPECT, 10, true);
            meta.setCustomModelData(100500);
        });

        ItemTag tag = getTag(testItem);
        if (tag == null) return false;

        ItemStack parsed = fromTag(tag);
        return parsed != null && parsed.isSimilar(testItem);

//        String nbt = ItemNbt.compress(testItem);
//        if (nbt == null) return false;
//
//        ItemStack decompressed = ItemNbt.decompress(nbt);
//        return decompressed != null && decompressed.isSimilar(testItem);
    }

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
        Reflex.invokeMethod(mNbtIoWrite, null, compoundTag, dataOutput);
        return new BigInteger(1, outputStream.toByteArray()).toString(32);
    }

    @Nullable
    @Deprecated
    public static ItemStack decompress(@NotNull String compressed) {
        if (compressed.isBlank()) return null;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(compressed, 32).toByteArray());
        DataInputStream dataInput = new DataInputStream(inputStream);

        Object compoundTag = Reflex.invokeMethod(mNbtIoRead, null, dataInput);
        return compoundTag == null ? null : fromCompoundTag(compoundTag, CoreConfig.DATA_FIXER_MISSING_VERSION.get());
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
//        if (tagString.isBlank() || tagString.equalsIgnoreCase("{}")) return null;
//
//        Object compoundTag = Reflex.invokeMethod(mTagParserParseTag, null, tagString);
//        if (compoundTag != null) System.out.println("fromCompoundTag(compoundTag) = " + fromCompoundTag(compoundTag));
//        return compoundTag == null ? null : fromCompoundTag(compoundTag);

        return fromTag(new ItemTag(tagString, CoreConfig.DATA_FIXER_MISSING_VERSION.get()));
    }

    @Nullable
    public static ItemStack fromTag(@NotNull ItemTag itemTag) {
        if (!loaded) return null;
        if (itemTag.isEmpty()) return null;

        String tagString = itemTag.getTag();
        int sourceVersion = itemTag.getDataVersion();

        Object tag = Reflex.invokeMethod(mTagParserParseTag, null, tagString);
        return tag == null ? null : fromCompoundTag(tag, sourceVersion);
    }

    @Nullable
    public static ItemTag getTag(@NotNull ItemStack item) {
        Object compoundTag = toCompoundTag(item);
        if (compoundTag == null) return null;

        return new ItemTag(compoundTag.toString(), Version.getCurrent().getDataVersion());
    }



    @NotNull
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
        if (itemStack == null) itemStack = new ItemStack(Material.AIR);

        return itemStack;
    }

    @Nullable
    private static Object toCompoundTag(@NotNull ItemStack bukkitStack) {
        if (!loaded) return null;
        if (bukkitStack.getType().isAir() || bukkitStack.getAmount() <= 0) return null;

        Object nmsStack = Reflex.invokeMethod(mCraftItemStackAsNmsCopy, null, bukkitStack);
        if (useRegistry) {
            return Reflex.invokeMethod(mItemStackSave, nmsStack, registryAccess);
        }

        Object compoundTag = Reflex.invokeConstructor(conNewCompoundTag);
        Reflex.invokeMethod(mMinecraftItemStackSave, nmsStack, compoundTag);
        return compoundTag;
    }

    @Nullable
    private static ItemStack fromCompoundTag(@NotNull Object tag, int sourceVersion) {
        if (!loaded) return null;

        Object itemStack;
        Object compoundTag = ItemNbt.applyDataFixer(tag, sourceVersion);

        if (useRegistry) {
            Optional<?> optional = (Optional<?>) Reflex.invokeMethod(mItemStackParse, null, registryAccess, compoundTag);
            itemStack = Objects.requireNonNull(optional).orElse(null);
        }
        else {
            itemStack = Reflex.invokeMethod(mMinecraftItemStackOf, null, compoundTag);
        }

        return itemStack == null ? null : (ItemStack) Reflex.invokeMethod(mCraftItemStackAsBukkitCopy, null, itemStack);
    }

//    @SuppressWarnings({"rawtypes", "unchecked"})
//    private static Object applyDataFixer(@NotNull Object compoundTag) {
//        Dynamic<?> dynamic = new Dynamic<>((DynamicOps) nbtOps, compoundTag);
//        return dataFixer.update((DSL.TypeReference) itemStackReference, dynamic, DATA_FIXER_SOURCE_VERSION, DATA_FIXER_TARGET_VERSION).getValue();
//    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Object applyDataFixer(@NotNull Object compoundTag, int sourceVersion) {
        int targetVersion = Version.getCurrent() == Version.UNKNOWN ? CoreConfig.DATA_FIXER_UNKNOWN_VERSION.get() : Version.getCurrent().getDataVersion();
        if (targetVersion <= 0) return compoundTag;
        if (sourceVersion > targetVersion || sourceVersion <= 0) return compoundTag;

        Dynamic<?> dynamic = new Dynamic<>((DynamicOps) nbtOps, compoundTag);
        return dataFixer.update((DSL.TypeReference) itemStackReference, dynamic, sourceVersion, targetVersion).getValue();
    }
}
