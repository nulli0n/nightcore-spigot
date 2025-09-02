package su.nightexpress.nightcore.util;

import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.placeholder.PlaceholderEntry;
import su.nightexpress.nightcore.util.random.Rnd;
import su.nightexpress.nightcore.util.regex.TimedMatcher;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.util.LinkedHashMap;
import java.util.List;
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
    @Deprecated
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

    @NotNull
    public static <T> String replaceEach(@NotNull String text, @NotNull List<PlaceholderEntry<T>> replacements, @NotNull T source) {
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
            tempIndex = text.indexOf(replacements.get(i).getKey());

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
            buf.append(replacements.get(replaceIndex).get(source));

            start = textIndex + replacements.get(replaceIndex).getKey().length();

            textIndex = -1;
            replaceIndex = -1;
            // find the next earliest match
            for (int i = 0; i < searchLength; i++) {
                if (noMoreMatchesForReplIndex[i]) {
                    continue;
                }
                tempIndex = text.indexOf(replacements.get(i).getKey(), start);

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

    @NotNull
    public static <T> String replacePlaceholders(@NotNull String string, @NotNull List<PlaceholderEntry<T>> replacements, @NotNull T source) {
        LinkedHashMap<String, Supplier<String>> placeholders = new LinkedHashMap<>();
        replacements.forEach(entry -> {
            placeholders.put(entry.getKey(), () -> entry.get(source));
        });
        return replacePlaceholders(string, placeholders);
    }

    @NotNull
    public static String replacePlaceholders(@NotNull String string, @NotNull LinkedHashMap<String, Supplier<String>> placeholders) {
        if (string.isBlank()) return string;

        StringBuilder builder = new StringBuilder();

        int length = string.length();
        for (int index = 0; index < length; index++) {
            char letter = string.charAt(index);

            if (letter == '%') {
                // Escaping when using double %%
                if (index + 1 < length && string.charAt(index + 1) == '%') {
                    builder.append('%');
                    index++; // skip next
                    continue;
                }

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
    @Deprecated
    public static <T extends Enum<T>> Optional<T> getEnum(String str, @NotNull Class<T> clazz) {
        return Enums.parse(str, clazz);
//        try {
//            return str == null ? Optional.empty() : Optional.of(Enum.valueOf(clazz, str.toUpperCase()));
//        }
//        catch (Exception exception) {
//            return Optional.empty();
//        }
    }

    @NotNull
    @Deprecated
    public static String inlineEnum(@NotNull Class<? extends Enum<?>> clazz, @NotNull String delimiter) {
        return Enums.inline(clazz, delimiter);
        //return String.join(delimiter, Lists.getEnums(clazz));
    }

    @NotNull
    public static Color getColor(@NotNull String str) {
        String[] rgb = str.split(",");
        int red = NumberUtil.getIntegerAbs(rgb[0]);
        if (red < 0) red = Rnd.get(255);

        int green = rgb.length >= 2 ? NumberUtil.getIntegerAbs(rgb[1]) : 0;
        if (green < 0) green = Rnd.get(255);

        int blue = rgb.length >= 3 ? NumberUtil.getIntegerAbs(rgb[2]) : 0;
        if (blue < 0) blue = Rnd.get(255);

        return Color.fromRGB(red, green, blue);
    }

    @NotNull
    @Deprecated
    public static String transformForID(@NotNull String str) {
        return transformForID(str, -1);
    }

    @NotNull
    @Deprecated
    public static String transformForID(@NotNull String str, int length) {
        return Strings.filterForVariable(str, length);
//        char[] chars = str.toLowerCase().toCharArray();
//
//        StringBuilder builder = new StringBuilder();
//        for (int index = 0; index < chars.length; index++) {
//            if (length > 0 && index >= length) break;
//
//            char letter = chars[index];
//            if (Character.isWhitespace(letter)) {
//                builder.append("_");
//                continue;
//            }
//            if (!isValidIDChar(letter)) {
//                continue;
//            }
//            builder.append(Character.toLowerCase(letter));
//        }
//        return builder.toString();
    }

//    private static boolean isValidIDChar(char c) {
//        return Character.isLetterOrDigit(c) || c == '_' || c == '-';
//        //return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '_' || c == '-';
//    }

    @NotNull
    public static String lowerCaseUnderscore(@NotNull String str) {
        return LowerCase.INTERNAL.apply(str).replace(' ', '_');
        //return lowerCaseUnderscore(str, -1);
    }

    @NotNull
    @Deprecated
    public static String lowerCaseUnderscore(@NotNull String str, int length) {
        return lowerCaseAndClean(str, ID_PATTERN, length);
    }

    @NotNull
    @Deprecated
    public static String lowerCaseUnderscoreStrict(@NotNull String str) {
        return lowerCaseUnderscoreStrict(str, -1);
    }

    @NotNull
    @Deprecated
    public static String lowerCaseUnderscoreStrict(@NotNull String str, int length) {
        return lowerCaseAndClean(str, ID_STRICT_PATTERN, length);
    }

    @NotNull
    @Deprecated
    private static String lowerCaseAndClean(@NotNull String str, @NotNull Pattern pattern, int length) {
        String clean = LowerCase.INTERNAL.apply(NightMessage.stripTags(str)).replace(" ", "_");
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
        return capitalize(LowerCase.USER_LOCALE.apply(str));
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
