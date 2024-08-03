package su.nightexpress.nightcore.util;

import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.random.Rnd;
import su.nightexpress.nightcore.util.regex.TimedMatcher;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class StringUtil {

    private static final Pattern ID_PATTERN = Pattern.compile("[<>\\%\\$\\!\\@\\#\\^\\&\\*\\(\\)\\,\\.\\'\\:\\;\\\"\\}\\]\\{\\[\\=\\+\\`\\~\\\\]");
    private static final Pattern ID_STRICT_PATTERN = Pattern.compile("[^a-zA-Zа-яА-Я_0-9]");

    @NotNull
    @Deprecated
    public static String oneSpace(@NotNull String str) {
        return str.trim().replaceAll("\\s+", " ");
    }

    @NotNull
    @Deprecated
    public static String noSpace(@NotNull String str) {
        return str.trim().replaceAll("\\s+", "");
    }

    @NotNull
    public static String replaceEach(@NotNull String text, @NotNull List<Pair<String, Supplier<String>>> replacements) {
        if (text.isEmpty() || replacements.isEmpty()) {
            return text;
        }

        final int searchLength = replacements.size();
        // keep track of which still have matches
        final boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];

        // index on index that the match was found
        int textIndex = -1;
        int replaceIndex = -1;
        int tempIndex;

        // index of replace array that will replace the search string found
        for (int i = 0; i < searchLength; i++) {
            if (noMoreMatchesForReplIndex[i]) {
                continue;
            }
            tempIndex = text.indexOf(replacements.get(i).getFirst());

            // see if we need to keep searching for this
            if (tempIndex == -1) {
                noMoreMatchesForReplIndex[i] = true;
            }
            else if (textIndex == -1 || tempIndex < textIndex) {
                textIndex = tempIndex;
                replaceIndex = i;
            }
        }

        // no search strings found, we are done
        if (textIndex == -1) {
            return text;
        }

        int start = 0;
        final StringBuilder buf = new StringBuilder();
        while (textIndex != -1) {
            for (int i = start; i < textIndex; i++) {
                buf.append(text.charAt(i));
            }
            buf.append(replacements.get(replaceIndex).getSecond().get());

            start = textIndex + replacements.get(replaceIndex).getFirst().length();

            textIndex = -1;
            replaceIndex = -1;
            // find the next earliest match
            for (int i = 0; i < searchLength; i++) {
                if (noMoreMatchesForReplIndex[i]) {
                    continue;
                }
                tempIndex = text.indexOf(replacements.get(i).getFirst(), start);

                // see if we need to keep searching for this
                if (tempIndex == -1) {
                    noMoreMatchesForReplIndex[i] = true;
                }
                else if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }

        final int textLength = text.length();
        for (int i = start; i < textLength; i++) {
            buf.append(text.charAt(i));
        }
        return buf.toString();
    }

    public static String replacePlaceholders(@NotNull String string, @NotNull Map<String, Supplier<String>> placeholders) {
        if (string.isBlank()) return string;

        StringBuilder builder = new StringBuilder();

        char[] chars = string.toCharArray();
        int length = string.length();
        //int index = 0;
        for (int index = 0; index < length; index++) {
            if (index == length - 1) continue;

            char letter = string.charAt(index);
            if (letter == '%') {
                int indexNext = string.indexOf(letter, index + 1);
                if (indexNext != -1) {
                    String key = string.substring(index + 1, indexNext);
                    Supplier<String> supplier = placeholders.get(key);
                    if (supplier != null) {
                        builder.append(supplier.get());
                        index = indexNext;
                        continue;
                    }
                }
            }
            builder.append(letter);
        }

        return builder.toString();
    }

    /*@Nullable
    public static String parseQuotedContent(@NotNull String string) {
        char quote = string.charAt(0);
        if (quote != '\'' && quote != '"') return null;
        if (string.length() < 3) return null;

        int indexEnd = -1;
        for (int index = 1; index < string.length(); index++) {
            char letter = string.charAt(index);
            if (letter == '\\') {
                index += 2;
                continue;
            }
            if (letter == quote) {
                indexEnd = index;
                break;
            }
        }
        if (indexEnd == -1) return null;

        return string.substring(1, indexEnd);
    }*/

    @Nullable
    public static String parseQuotedContent(@NotNull String string) {
        char quote = string.charAt(0);
        if (quote != '\'' && quote != '"') return null;
        if (string.length() < 3) return null;

        StringBuilder builder = new StringBuilder();

        for (int index = 1; index < string.length(); index++) {
            char letter = string.charAt(index);
            if (letter == '\\') {
                //index++;
                continue;
            }
            if (letter == quote) {
                break;
            }
            builder.append(letter);
        }

        return builder.toString();
    }

    @NotNull
    public static <T extends Enum<T>> Optional<T> getEnum(String str, @NotNull Class<T> clazz) {
        try {
            return str == null ? Optional.empty() : Optional.of(Enum.valueOf(clazz, str.toUpperCase()));
        }
        catch (Exception exception) {
            return Optional.empty();
        }
    }

    @NotNull
    public static String inlineEnum(@NotNull Class<? extends Enum<?>> clazz, @NotNull String delimiter) {
        return String.join(delimiter, Lists.getEnums(clazz));
    }

    @NotNull
    public static Color getColor(@NotNull String str) {
        String[] rgb = str.split(",");
        int red = NumberUtil.getInteger(rgb[0], 0);
        if (red < 0) red = Rnd.get(255);

        int green = rgb.length >= 2 ? NumberUtil.getInteger(rgb[1], 0) : 0;
        if (green < 0) green = Rnd.get(255);

        int blue = rgb.length >= 3 ? NumberUtil.getInteger(rgb[2], 0) : 0;
        if (blue < 0) blue = Rnd.get(255);

        return Color.fromRGB(red, green, blue);
    }

    @NotNull
    public static String lowerCaseUnderscore(@NotNull String str) {
        return lowerCaseUnderscore(str, -1);
    }

    @NotNull
    public static String lowerCaseUnderscore(@NotNull String str, int length) {
        return lowerCaseAndClean(str, ID_PATTERN, length);
    }

    @NotNull
    public static String lowerCaseUnderscoreStrict(@NotNull String str) {
        return lowerCaseUnderscoreStrict(str, -1);
    }

    @NotNull
    public static String lowerCaseUnderscoreStrict(@NotNull String str, int length) {
        return lowerCaseAndClean(str, ID_STRICT_PATTERN, length);
    }

    @NotNull
    private static String lowerCaseAndClean(@NotNull String str, @NotNull Pattern pattern, int length) {
        String clean = Colorizer.restrip(str).toLowerCase().replace(" ", "_");
        if (length > 0 && clean.length() > length) {
            clean = clean.substring(0, length);
        }

        TimedMatcher matcher = TimedMatcher.create(pattern, clean, 200);
        //Matcher matcher = RegexUtil.getMatcher(ID_STRICT_PATTERN, clean);
        return matcher.replaceAll("");
    }

    @NotNull
    public static String capitalizeUnderscored(@NotNull String str) {
        return capitalizeFully(str.replace("_", " "));
    }

    @NotNull
    public static String capitalizeFully(@NotNull String str) {
        return capitalize(str.toLowerCase());
    }

    @NotNull
    public static String capitalize(@NotNull String str) {
        if (str.isEmpty()) return str;

        int length = str.length();
        StringBuilder builder = new StringBuilder(length);
        boolean capitalizeNext = true;

        for (int index = 0; index < length; ++index) {
            char letter = str.charAt(index);
            if (Character.isWhitespace(letter)) {
                builder.append(letter);
                capitalizeNext = true;
            }
            else if (capitalizeNext) {
                builder.append(Character.toTitleCase(letter));
                capitalizeNext = false;
            }
            else {
                builder.append(letter);
            }
        }
        return builder.toString();
    }

    @NotNull
    public static String capitalizeFirstLetter(@NotNull String original) {
        if (original.isEmpty()) return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}
