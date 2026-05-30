package su.nightexpress.nightcore.util;

import org.jspecify.annotations.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.function.Predicate;

public class Strings {

    @NonNull
    @Deprecated
    public static String filterForVariable(@NonNull String str) {
        return filterForVariable(str, -1);

    }

    @NonNull
    @Deprecated
    public static String filterForVariable(@NonNull String str, int maxLength) {
        return varStyle(str).orElse("");
    }

    @NonNull
    public static Optional<String> varStyle(@NonNull String str) {
        return varStyle(str, Strings::isValidVariableChar);
    }

    @NonNull
    public static Optional<String> varStyle(@NonNull String str, @NonNull Predicate<Character> predicate) {
        char[] chars = LowerCase.INTERNAL.apply(str).toCharArray();

        StringBuilder builder = new StringBuilder();
        for (char letter : chars) {
            if (Character.isWhitespace(letter)) {
                builder.append("_");
                continue;
            }
            if (!predicate.test(letter)) {
                continue;
            }
            builder.append(Character.toLowerCase(letter));
        }

        String result = builder.toString();
        return result.isBlank() ? Optional.empty() : Optional.of(result);
    }

    private static boolean isValidVariableChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '-';
    }

    @NonNull
    public static String toBase64(@NonNull String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(bytes);
    }

    @NonNull
    public static String fromBase64(@NonNull String encoded) {
        byte[] bytes = Base64.getDecoder().decode(encoded);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
