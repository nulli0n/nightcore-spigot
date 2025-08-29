package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.Function;

public enum LowerCase {

    INTERNAL(str -> str.toLowerCase(Locale.ROOT)),
    USER_LOCALE(str -> str.toLowerCase(Locale.getDefault()));

    private final Function<String, String> function;

    LowerCase(@NotNull Function<String, String> function) {
        this.function = function;
    }

    @NotNull
    public String apply(@NotNull String string) {
        return this.function.apply(string);
    }
}
