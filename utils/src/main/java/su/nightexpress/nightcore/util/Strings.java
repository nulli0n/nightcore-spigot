package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;

public class Strings {

    @NotNull
    public static String filterForVariable(@NotNull String str) {
        return filterForVariable(str, -1);
    }

    @NotNull
    public static String filterForVariable(@NotNull String str, int maxLength) {
        char[] chars = LowerCase.INTERNAL.apply(str).toCharArray();

        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < chars.length; index++) {
            if (maxLength > 0 && index >= maxLength) break;

            char letter = chars[index];
            if (Character.isWhitespace(letter)) {
                builder.append("_");
                continue;
            }
            if (!isValidVariableChar(letter)) {
                continue;
            }
            builder.append(Character.toLowerCase(letter));
        }
        return builder.toString();
    }

    private static boolean isValidVariableChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '-';
    }
}
