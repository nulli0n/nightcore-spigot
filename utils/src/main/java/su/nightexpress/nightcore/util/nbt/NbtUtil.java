package su.nightexpress.nightcore.util.nbt;

import com.google.gson.JsonElement;
import com.mojang.serialization.*;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.Reflex;
import su.nightexpress.nightcore.util.Version;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

public class NbtUtil {

    private static final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();
    private static final boolean USE_CODEC = Version.isAtLeast(Version.MC_1_21_6);

    private static final Class<?> CLS_CRAFT_REGISTRY   = Reflex.safeClass(CRAFTBUKKIT_PACKAGE, "CraftRegistry");
    private static final Class<?> CLS_CRAFT_ITEM_STACK = Reflex.safeClass(CRAFTBUKKIT_PACKAGE + ".inventory", "CraftItemStack");

    public static final Class<?> CLS_NMS_ITEM_STACK = Reflex.safeClass("net.minecraft.world.item", "ItemStack");

    public static final Class<?> CLS_TAG                    = Reflex.safeClass("net.minecraft.nbt", "Tag", "NBTBase");
    public static final Class<?> CLS_TAG_PARSER             = Reflex.safeClass("net.minecraft.nbt", "TagParser", "MojangsonParser");
    public static final Class<?> CLS_HOLDER_LOOKUP_PROVIDER = Reflex.safeClass("net.minecraft.core", "HolderLookup$Provider", "HolderLookup$a");

    private static final Method GET_MC_REGISTRY = Reflex.safeMethod(CLS_CRAFT_REGISTRY, "getMinecraftRegistry");
    private static final Method PARSE_TAG       = Reflex.safeMethod(CLS_TAG_PARSER, "parseCompoundFully", "a", String.class);

    public static final Method AS_NMS_COPY    = Reflex.safeMethod(CLS_CRAFT_ITEM_STACK, "asNMSCopy", ItemStack.class);
    public static final Method AS_BUKKIT_COPY = Reflex.safeMethod(CLS_CRAFT_ITEM_STACK, "asBukkitCopy", CLS_NMS_ITEM_STACK);

    private static final Method ITEM_STACK_PARSE = USE_CODEC ? null : Reflex.safeMethod(CLS_NMS_ITEM_STACK, "parse", "a", CLS_HOLDER_LOOKUP_PROVIDER, CLS_TAG);
    private static final Method ITEM_STACK_SAVE  = USE_CODEC ? null : Reflex.safeMethod(CLS_NMS_ITEM_STACK, "save", "a", CLS_HOLDER_LOOKUP_PROVIDER);

    public static final Object REGISTRY_ACCESS = Reflex.invokeMethod(GET_MC_REGISTRY, null);

    @Nullable
    public static Object tagFromString(@NotNull String json) {
        return Reflex.invokeMethod(PARSE_TAG, null, json);
    }

    @NotNull
    public static JsonElement tagToJson(@NotNull Object compoundTag) {
        return NbtOps.convertTag(JsonOps.INSTANCE, compoundTag);
    }

    @Nullable
    public static ItemStack tagToItemStack(@NotNull String tagString, int sourceVersion) {
        Object tag = tagFromString(tagString);
        return tag == null ? null : tagToItemStack(tag, sourceVersion);
    }

    @Nullable
    public static ItemStack tagToItemStack(@NotNull Object tag, int sourceVersion) {
        Object itemStack;
        Object compoundTag = DataFixerUtil.updateItemStack(tag, sourceVersion);

        if (USE_CODEC) {
            itemStack = NbtSerializer.decodeItemStack(compoundTag);
        }
        else {
            Optional<?> optional = (Optional<?>) Reflex.invokeMethod(ITEM_STACK_PARSE, null, REGISTRY_ACCESS, compoundTag);
            itemStack = Objects.requireNonNull(optional).orElse(null);
        }

        return itemStack == null ? null : (ItemStack) Reflex.invokeMethod(AS_BUKKIT_COPY, null, itemStack);
    }

    @NotNull
    public static Object tagFromItemStack(@NotNull ItemStack bukkitStack) {
        Object nmsStack = Reflex.invokeMethod(AS_NMS_COPY, null, bukkitStack);
        if (nmsStack == null) throw new IllegalStateException("Could not convert bukkit ItemStack to NMS copy");

        if (!USE_CODEC) {
            Reflex.safeInvoke(ITEM_STACK_SAVE, nmsStack, REGISTRY_ACCESS).orElseThrow(() -> new IllegalStateException("Could not #save() ItemStack"));
        }

        return NbtSerializer.encodeItemStack(nmsStack);
    }
}
