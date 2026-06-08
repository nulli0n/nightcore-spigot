package su.nightexpress.nightcore.util;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum LowerCase {

    INTERNAL(str -> str.toLowerCase(Locale.ROOT)),
    USER_LOCALE(str -> str.toLowerCase(Locale.getDefault()));

    private final Function<String, String> function;

    LowerCase(UnaryOperator<String> function) {
        this.function = function;
    }

    public String apply(String string) {
        return this.function.apply(string);
    }

    public static String internal(String string) {
        return INTERNAL.apply(string);
    }

    public static String localeRuled(String string) {
        return USER_LOCALE.apply(string);
    }
}
