package su.nightexpress.nightcore.util;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.Engine;
import su.nightexpress.nightcore.NightCore;
import su.nightexpress.nightcore.core.CoreConfig;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.*;

public class ItemNbt {

    private static final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();

    private static final Class<?> CLS_MINECRAFT_ITEM_STACK = Reflex.getNMSClass("net.minecraft.world.item", "ItemStack");

    private static final Class<?> CLS_CRAFT_ITEM_STACK = Reflex.getNMSClass(CRAFTBUKKIT_PACKAGE + ".inventory", "CraftItemStack");

    private static final Class<?> CLS_TAG          = Reflex.getNMSClass("net.minecraft.nbt", "Tag", "NBTBase");
    private static final Class<?> CLS_COMPOUND_TAG = Reflex.getNMSClass("net.minecraft.nbt", "CompoundTag", "NBTTagCompound");
    private static final Class<?> CLS_NBT_IO       = Reflex.getNMSClass("net.minecraft.nbt", "NbtIo", "NBTCompressedStreamTools");
    private static final Class<?> CLS_TAG_PARSER   = Reflex.getNMSClass("net.minecraft.nbt", "TagParser", "MojangsonParser");
    private static final Class<?> CLS_NBT_OPS      = Reflex.getNMSClass("net.minecraft.nbt", "NbtOps", "DynamicOpsNBT");

    private static final Class<?> CLS_DATA_FIXERS = Reflex.getNMSClass("net.minecraft.util.datafix", "DataFixers", "DataConverterRegistry");
    private static final Class<?> CLS_REFERENCES  = Reflex.getNMSClass("net.minecraft.util.datafix.fixes", "References", "DataConverterTypes");

    private static final Class<?> CLS_HOLDER_LOOKUP_PROVIDER = Reflex.getNMSClass("net.minecraft.core", "HolderLookup$Provider", "HolderLookup$a");

    private static Method mCraftItemStackAsNmsCopy;
    private static Method mCraftItemStackAsBukkitCopy;

    private static Method mNbtIoWrite;
    private static Method mNbtIoRead;
    private static Method mTagParserParseTag;

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

    // For 1.21.6+
    private static Codec<?> itemStackCodec;
    private static Method   mCreateSerializationContext;
    private static Method mEncodeStart;
    private static Method mParse;

    // Base
    private static boolean useRegistry;
    private static boolean useCodec;
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

        mNbtIoWrite = Reflex.getMethod(CLS_NBT_IO, "write", "a", CLS_COMPOUND_TAG, DataOutput.class);
        mNbtIoRead = Reflex.getMethod(CLS_NBT_IO, "read", "a", DataInput.class);
        mTagParserParseTag = Reflex.getMethod(CLS_TAG_PARSER, "parseCompoundFully", "a", String.class);

        if (mNbtIoWrite == null || mNbtIoRead == null || mTagParserParseTag == null) {
            core.error("[Item NBT] Could not find NbtIo or TagParser methods.");
            return;
        }

        // Load DataFixer components.
        Method mGetDataFixer = Reflex.getMethod(CLS_DATA_FIXERS, "getDataFixer", "a");
        if (mGetDataFixer == null) {
            core.error("[Item NBT] Could not load DataFixer components.");
            return;
        }

        dataFixer = (DataFixer) Reflex.invokeMethod(mGetDataFixer, CLS_DATA_FIXERS);
        nbtOps = Reflex.getFieldValue(CLS_NBT_OPS, "INSTANCE", "a");
        itemStackReference = Reflex.getFieldValue(CLS_REFERENCES, "ITEM_STACK", Version.isAtLeast(Version.MC_1_21_6) ? "u" : "t");

        // Load version specific item parsers.
        if (Version.isAtLeast(Version.MC_1_20_6)) {
            useRegistry = true;

            Class<?> clsCraftRegistry = Reflex.getNMSClass(CRAFTBUKKIT_PACKAGE, "CraftRegistry");
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

            Class<?> clsHolderLookupProvider = Reflex.getNMSClass("net.minecraft.core", "HolderLookup$Provider", "HolderLookup$a");
            if (clsHolderLookupProvider == null) {
                core.error("[Item NBT] Could not find MinecraftServer or HolderLookup#Provider.");
                return;
            }

            if (Version.isAtLeast(Version.MC_1_21_6)) {
                useCodec = true;

                if (CLS_HOLDER_LOOKUP_PROVIDER == null) {
                    core.error("Class not found: HoolderLookup.Provider");
                    return;
                }

                itemStackCodec = (Codec<?>) Reflex.getFieldValue(CLS_MINECRAFT_ITEM_STACK, "CODEC", "b");
                mCreateSerializationContext = Reflex.getMethod(CLS_HOLDER_LOOKUP_PROVIDER, "createSerializationContext", "a", DynamicOps.class);

                mEncodeStart = Reflex.getMethod(Encoder.class, "encodeStart", DynamicOps.class, Object.class);
                mParse = Reflex.getMethod(Decoder.class, "parse", DynamicOps.class, Object.class);
            }
            else {
                mItemStackParse = Reflex.getMethod(CLS_MINECRAFT_ITEM_STACK, "parse", "a", clsHolderLookupProvider, CLS_TAG);
                mItemStackSave = Reflex.getMethod(CLS_MINECRAFT_ITEM_STACK, "save", "a", clsHolderLookupProvider);

                if (mItemStackParse == null || mItemStackSave == null) {
                    core.error("[Item NBT] Could not find ItemStack#parse or ItemStack#save methods.");
                    return;
                }
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
            core.error("[Item NBT] Test failed.");
            loaded = false;
        }
        else core.info("[Item NBT] Test successful.");
    }

    public static boolean test() {
        ItemStack testItem = new ItemStack(Material.DIAMOND_SWORD);
        ItemUtil.editMeta(testItem, meta -> {
            ItemUtil.setCustomName(meta, "Test Item");
            ItemUtil.setLore(meta, Lists.newList("Test Lore 1", "Test Lore 2", "Test Lore 3"));
            ItemUtil.setCustomModelData(meta, 100500);
            meta.addEnchant(Enchantment.FIRE_ASPECT, 10, true);
        });

        ItemTag tag = getTag(testItem);
        if (tag == null) return false;

        ItemStack parsed = fromTag(tag);
        return parsed != null && parsed.isSimilar(testItem);
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
    private static Object toCompoundTag(@NotNull ItemStack bukkitStack) {
        if (!loaded) return null;
        if (bukkitStack.getType().isAir() || bukkitStack.getAmount() <= 0) return null;

        Object nmsStack = Reflex.invokeMethod(mCraftItemStackAsNmsCopy, null, bukkitStack);

        if (useCodec) {
            Object context = createSerializationContext();
            DataResult.Success<?> result = (DataResult.Success<?>) Reflex.invokeMethod(mEncodeStart, itemStackCodec, context, nmsStack);
            if (result == null) return null;

            return result.getOrThrow();
        }

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

        if (useCodec) {
            Object context = createSerializationContext();
            DataResult<?> decoded = (DataResult<?>) Reflex.invokeMethod(mParse, itemStackCodec, context, compoundTag);
            if (decoded == null) return null;

            itemStack = decoded.resultOrPartial(itemId -> Engine.core().error("Could not decode ItemStack from tag: " + itemId)).orElse(null);
        }
        else if (useRegistry) {
            Optional<?> optional = (Optional<?>) Reflex.invokeMethod(mItemStackParse, null, registryAccess, compoundTag);
            itemStack = Objects.requireNonNull(optional).orElse(null);
        }
        else {
            itemStack = Reflex.invokeMethod(mMinecraftItemStackOf, null, compoundTag);
        }

        return itemStack == null ? null : (ItemStack) Reflex.invokeMethod(mCraftItemStackAsBukkitCopy, null, itemStack);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Object applyDataFixer(@NotNull Object compoundTag, int sourceVersion) {
        int targetVersion = Version.getCurrent() == Version.UNKNOWN ? CoreConfig.DATA_FIXER_UNKNOWN_VERSION.get() : Version.getCurrent().getDataVersion();
        if (targetVersion <= 0) return compoundTag;
        if (sourceVersion > targetVersion || sourceVersion <= 0) return compoundTag;

        // Probably not necessary, but they used it for a reason probably.
        /*if (Version.isPaper()) {
            Class<?> mcDataTypeClass = Reflex.getClass("ca.spottedleaf.dataconverter.minecraft.datatypes", "MCDataType");
            Class<?> mcDataConvertedClass = Reflex.getClass("ca.spottedleaf.dataconverter.minecraft", "MCDataConverter");
            Class<?> mcTypeRegistryClass = Reflex.getClass("ca.spottedleaf.dataconverter.minecraft.datatypes", "MCTypeRegistry");

            Object itemStackRegistry = Reflex.getFieldValue(mcTypeRegistryClass, "ITEM_STACK");

            Method convert = Reflex.getMethod(mcDataConvertedClass, "convertTag", mcDataTypeClass, CLS_COMPOUND_TAG, Integer.TYPE, Integer.TYPE);

            return Reflex.invokeMethod(convert, null, itemStackRegistry, compoundTag, sourceVersion, targetVersion);
        }*/

        Dynamic<?> dynamic = new Dynamic<>((DynamicOps) nbtOps, compoundTag);
        return dataFixer.update((DSL.TypeReference) itemStackReference, dynamic, sourceVersion, targetVersion).getValue();
    }

    private static Object createSerializationContext() {
        return Reflex.invokeMethod(mCreateSerializationContext, registryAccess, nbtOps);
    }
}
