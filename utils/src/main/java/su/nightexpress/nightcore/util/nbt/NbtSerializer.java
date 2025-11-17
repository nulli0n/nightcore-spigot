package su.nightexpress.nightcore.util.nbt;

import com.mojang.serialization.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import su.nightexpress.nightcore.util.Reflex;

import java.lang.reflect.Method;

public class NbtSerializer {

    private static final Method CREATE_SERIALIZATION_CONTEXT = Reflex.safeMethod(NbtUtil.CLS_HOLDER_LOOKUP_PROVIDER, "createSerializationContext", "a", DynamicOps.class);
    private static final Method ENCODER_START = Reflex.safeMethod(Encoder.class, "encodeStart", DynamicOps.class, Object.class);
    private static final Method DECODER_PARSE = Reflex.safeMethod(Decoder.class, "parse", DynamicOps.class, Object.class);

    private static final Codec<?> ITEM_STACK_CODEC = (Codec<?>) Reflex.getFieldValue(NbtUtil.CLS_NMS_ITEM_STACK, "CODEC", "b");

    private static final Logger LOGGER = LoggerFactory.getLogger(NbtSerializer.class);

    @NotNull
    public static Object createSerializationContext() {
        Object context = Reflex.invokeMethod(CREATE_SERIALIZATION_CONTEXT, NbtUtil.REGISTRY_ACCESS, NbtOps.INSTANCE);
        if (context == null) throw new IllegalStateException("Could not create serialization context");

        return context;
    }

    @Nullable
    public static Object decodeItemStack(@NotNull Object compoundTag) {
        Object context = createSerializationContext();
        DataResult<?> result = (DataResult<?>) Reflex.invokeMethod(DECODER_PARSE, ITEM_STACK_CODEC, context, compoundTag);
        if (result == null) return null;

        return result.resultOrPartial(itemId -> LOGGER.error("Could not decode ItemStack from tag: {}", itemId)).orElse(null);
    }

    @NotNull
    public static Object encodeItemStack(@NotNull Object nmsStack) {
        Object context = createSerializationContext();

        return Reflex.safeInvoke(ENCODER_START, ITEM_STACK_CODEC, context, nmsStack)
            .filter(DataResult.Success.class::isInstance)
            .map(DataResult.Success.class::cast)
            .map(DataResult::getOrThrow)
            .orElseThrow(() -> new IllegalStateException("Could not encode ItemStack into CompoundTag"));
    }
}
