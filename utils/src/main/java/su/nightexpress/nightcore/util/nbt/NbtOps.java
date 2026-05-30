package su.nightexpress.nightcore.util.nbt;

import com.mojang.serialization.DynamicOps;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.util.Reflex;

import java.lang.reflect.Method;

public class NbtOps {

    private static final Class<?> CLS_NBT_OPS = Reflex.safeClass("net.minecraft.nbt", "NbtOps", "DynamicOpsNBT");

    private static final Method CONVERT_TO = Reflex.safeMethod(CLS_NBT_OPS, "convertTo", DynamicOps.class,
        Object.class);

    public static final Object INSTANCE = Reflex.getFieldValue(CLS_NBT_OPS, "INSTANCE", "a");

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> T convertTag(@NonNull DynamicOps<T> outOps, @NonNull Object compoundTag) {
        T element = (T) Reflex.invokeMethod(CONVERT_TO, INSTANCE, outOps, compoundTag);
        if (element == null) throw new IllegalStateException("Could not convert " + compoundTag + " to " + outOps);

        return element;
    }
}
