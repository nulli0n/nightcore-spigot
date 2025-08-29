package su.nightexpress.nightcore.util.text.night.tag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.LowerCase;

import java.util.HashMap;
import java.util.Map;

public class TagHandlerRegistry {

    private static final Map<String, Creator> HANDLER_BY_NAME = new HashMap<>();

    public static void clear() {
        HANDLER_BY_NAME.clear();
    }

    public static void register(@NotNull Creator creator, String... aliases) {
        for (String alias : aliases) {
            HANDLER_BY_NAME.put(LowerCase.INTERNAL.apply(alias), creator);
        }
    }

    public static boolean unregister(@NotNull String name) {
        return HANDLER_BY_NAME.remove(LowerCase.INTERNAL.apply(name)) != null;
    }

    public static boolean isValid(@NotNull String name) {
        return HANDLER_BY_NAME.containsKey(LowerCase.INTERNAL.apply(name));
    }

    @Nullable
    public static Creator getByName(@NotNull String name) {
        return HANDLER_BY_NAME.get(LowerCase.INTERNAL.apply(name));
    }

    @Nullable
    public static TagHandler create(@NotNull String name) {
        Creator creator = getByName(name);
        return creator == null ? null : creator.create();
    }

    public interface Creator {

        @NotNull TagHandler create();
    }
}
