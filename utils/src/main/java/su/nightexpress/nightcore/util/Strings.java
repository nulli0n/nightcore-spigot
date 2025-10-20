package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.function.Predicate;

public class Strings {

    @NotNull
    @Deprecated
    public static String filterForVariable(@NotNull String str) {
        return filterForVariable(str, -1);

    }

    @NotNull
    @Deprecated
    public static String filterForVariable(@NotNull String str, int maxLength) {
        return varStyle(str).orElse("");
    }

    @NotNull
    public static Optional<String> varStyle(@NotNull String str) {
        return varStyle(str, Strings::isValidVariableChar);
    }

    @NotNull
    public static Optional<String> varStyle(@NotNull String str, @NotNull Predicate<Character> predicate) {
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

    @NotNull
    public static String toBase64(@NotNull String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(bytes);
    }

    @NotNull
    public static String fromBase64(@NotNull String encoded) {
        byte[] bytes = Base64.getDecoder().decode(encoded);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
