package su.nightexpress.nightcore.util.text.night.tag;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.LowerCase;

public class TagHandlerRegistry {

    private static final Map<String, Creator> HANDLER_BY_NAME = new HashMap<>();

    public static void clear() {
        HANDLER_BY_NAME.clear();
    }

    public static void register(@NonNull Creator creator, String... aliases) {
        for (String alias : aliases) {
            HANDLER_BY_NAME.put(LowerCase.INTERNAL.apply(alias), creator);
        }
    }

    public static boolean unregister(@NonNull String name) {
        return HANDLER_BY_NAME.remove(LowerCase.INTERNAL.apply(name)) != null;
    }

    public static boolean isValid(@NonNull String name) {
        return HANDLER_BY_NAME.containsKey(LowerCase.INTERNAL.apply(name));
    }

    @Nullable
    public static Creator getByName(@NonNull String name) {
        return HANDLER_BY_NAME.get(LowerCase.INTERNAL.apply(name));
    }

    @Nullable
    public static TagHandler create(@NonNull String name) {
        Creator creator = getByName(name);
        return creator == null ? null : creator.create();
    }

    public interface Creator {

        @NonNull
        TagHandler create();
    }
}
