package su.nightexpress.nightcore.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public class NbtUtil {

    private static final Class<?> CLS_NBT_OPS    = Reflex.getNMSClass("net.minecraft.nbt", "NbtOps", "DynamicOpsNBT");
    private static final Class<?> CLS_TAG        = Reflex.getNMSClass("net.minecraft.nbt", "Tag", "NBTBase");
    private static final Class<?> CLS_TAG_PARSER = Reflex.getNMSClass("net.minecraft.nbt", "TagParser", "MojangsonParser");

    private static final Method PARSE_TAG          = CLS_TAG_PARSER == null ? null : Reflex.getMethod(CLS_TAG_PARSER, "parseCompoundFully", "a", String.class);
    private static final Method NBT_OPS_CONVERT_TO = CLS_TAG == null || CLS_NBT_OPS == null ? null : Reflex.getMethod(CLS_NBT_OPS, "convertTo", DynamicOps.class, CLS_TAG);

    private static final Object NBT_OPS = CLS_NBT_OPS == null ? null : Reflex.getFieldValue(CLS_NBT_OPS, "INSTANCE", "a");

    @Nullable
    public static Object parseTag(@NotNull String json) {
        if (PARSE_TAG == null) return null;

        return Reflex.invokeMethod(PARSE_TAG, null, json);
    }

    @Nullable
    public static JsonElement snbtToJson(@NotNull Object compoundTag) {
        if (NBT_OPS_CONVERT_TO == null) return null;
        if (NBT_OPS == null) return null;

        return (JsonElement) Reflex.invokeMethod(NBT_OPS_CONVERT_TO, NBT_OPS, JsonOps.INSTANCE, compoundTag);
    }
}
