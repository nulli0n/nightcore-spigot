package su.nightexpress.nightcore.util.text.night.tag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TagHandlerRegistry {

    private static final Map<String, Creator> HANDLER_BY_NAME = new HashMap<>();

    public static void clear() {
        HANDLER_BY_NAME.clear();
    }

    public static void register(@NotNull Creator creator, String... aliases) {
        for (String alias : aliases) {
            HANDLER_BY_NAME.put(alias.toLowerCase(), creator);
        }
    }

    public static boolean unregister(@NotNull String name) {
        return HANDLER_BY_NAME.remove(name.toLowerCase()) != null;
    }

    public static boolean isValid(@NotNull String name) {
        return HANDLER_BY_NAME.containsKey(name.toLowerCase());
    }

    @Nullable
    public static Creator getByName(@NotNull String name) {
        return HANDLER_BY_NAME.get(name.toLowerCase());
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
