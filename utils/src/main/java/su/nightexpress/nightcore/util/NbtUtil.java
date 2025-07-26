package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public class NbtUtil {

    private static final Class<?> CLS_TAG_PARSER = Reflex.getNMSClass("net.minecraft.nbt", "TagParser", "MojangsonParser");

    private static final Method PARSE_TAG = CLS_TAG_PARSER == null ? null : Reflex.getMethod(CLS_TAG_PARSER, "parseCompoundFully", "a", String.class);

    @Nullable
    public static Object parseTag(@NotNull String json) {
        if (PARSE_TAG == null) return null;

        return Reflex.invokeMethod(PARSE_TAG, null, json);
    }
}
