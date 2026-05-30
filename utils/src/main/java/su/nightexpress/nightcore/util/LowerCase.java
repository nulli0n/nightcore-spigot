package su.nightexpress.nightcore.util;

import org.jspecify.annotations.NonNull;

import java.util.Locale;
import java.util.function.Function;

public enum LowerCase {

    INTERNAL(str -> str.toLowerCase(Locale.ROOT)),
    USER_LOCALE(str -> str.toLowerCase(Locale.getDefault()));

    private final Function<String, String> function;

    LowerCase(@NonNull Function<String, String> function) {
        this.function = function;
    }

    @NonNull
    public String apply(@NonNull String string) {
        return this.function.apply(string);
    }
}
