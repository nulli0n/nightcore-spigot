package su.nightexpress.nightcore.util.text.night;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.night.tag.TagContent;
import su.nightexpress.nightcore.util.text.night.tag.TagHandler;
import su.nightexpress.nightcore.util.text.night.tag.TagHandlerRegistry;
import su.nightexpress.nightcore.util.text.night.tag.handler.NamedColorTagHandler;
import su.nightexpress.nightcore.util.text.night.wrapper.SimpleTagWrapper;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParserUtils {

    public static final char ESCAPE        = '\\';
    public static final char OPEN_BRACKET  = '<';
    public static final char CLOSE_BRACKET = '>';
    public static final char CLOSE_SLASH = '/';
    public static final char DELIMITER   = ':';
    public static final char QUOTE       = '\'';
    public static final char DOUBLE_QUOTE  = '"';

    private static final String[] LINE_SPLITTERS = {TagWrappers.BR, TagWrappers.NEWLINE};

    public static String[] breakDownLineSplitters(@NotNull String string) {
        for (String alias : LINE_SPLITTERS) {
            string = string.replace(alias, "\n");
        }
        return string.split("\n");
    }

    @NotNull
    public static List<String> breakDownLineSplitters(@NotNull List<String> list) {
        List<String> segmented = new ArrayList<>();

        list.forEach(line -> {
            Collections.addAll(segmented, breakDownLineSplitters(line));
        });

        return segmented;
    }

    public static int findUnescapedUnquotedChar(String input, char target, int fromIndex) {
        boolean inSingleQuotes = false;
        boolean inDoubleQuotes = false;
        boolean escaped = false;

        for (int index = fromIndex; index < input.length(); index++) {
            char letter = input.charAt(index);

            if (escaped) {
                escaped = false;
                continue;
            }

            if (letter == ESCAPE) {
                escaped = true;
                continue;
            }

            if (!inDoubleQuotes && letter == '\'') {
                inSingleQuotes = !inSingleQuotes;
                continue;
            }

            if (!inSingleQuotes && letter == '\"') {
                inDoubleQuotes = !inDoubleQuotes;
                continue;
            }

            if (!inSingleQuotes && !inDoubleQuotes && letter == target) {
                return index;
            }
        }

        return -1; // Not found
    }

    @NotNull
    public static TagContent parseInnerContent(@NotNull String tagContent) {
        int index = ParserUtils.findUnescapedUnquotedChar(tagContent, ParserUtils.DELIMITER, 0);

        String first = index < 0 ? tagContent : ParserUtils.unquoted(tagContent.substring(0, index));
        String second = index < 0 ? null : index >= tagContent.length() ? null : ParserUtils.unquoted(tagContent.substring(index + 1));

        return new TagContent(first, second);
    }

    @Nullable
    public static Color colorFromSchemeOrHex(@NotNull String string) {
        if (string.startsWith("#") && string.length() == 7) {
            return colorFromHexString(string);
        }

        return findColorFromScheme(string);
    }

    @Nullable
    public static Color findColorFromScheme(@NotNull String name) {
        TagHandler handler = TagHandlerRegistry.create(name);
        if (handler instanceof NamedColorTagHandler colorHandler) {
            return colorHandler.getColor();
        }
        return null;
    }

    @NotNull
    public static Color colorFromHexString(@NotNull String string) {
        return colorFromHexString(string, Color.WHITE);
    }

    @NotNull
    public static Color colorFromHexString(@NotNull String string, @NotNull Color fallback) {
        if (string.charAt(0) != '#') string = "#" + string;

        try {
            return Color.decode(string);
        }
        catch (NumberFormatException exception) {
            exception.printStackTrace();
            return fallback;
        }
    }

    @NotNull
    public static String colorToHexString(@NotNull Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2);
    }

    @NotNull
    public static String quoted(@NotNull String content) {
        return DOUBLE_QUOTE + escapeQuotes(content) + DOUBLE_QUOTE;
    }

    @NotNull
    public static String unquoted(@NotNull String str) {
        String dQuote = String.valueOf(DOUBLE_QUOTE);
        String sQuote = String.valueOf(QUOTE);

        if (str.startsWith(dQuote) || str.startsWith(sQuote)) {
            str = str.substring(1);
        }
        if (str.endsWith(dQuote) || str.endsWith(sQuote)) {
            str = str.substring(0, str.length() - 1);
        }
        return str.replace("\\", "");
    }

    @NotNull
    public static String escapeQuotes(@NotNull String content) {
        return content.replace(String.valueOf(QUOTE), "\\'").replace(String.valueOf(DOUBLE_QUOTE), "\\\"");
    }

    @Nullable
    public static SimpleTagWrapper legacyToNamedWrapper(char colorChar) {
        return switch (colorChar) {
            case '0' -> TagWrappers.BLACK;
            case '1' -> TagWrappers.DARK_BLUE;
            case '2' -> TagWrappers.DARK_GREEN;
            case '3' -> TagWrappers.DARK_AQUA;
            case '4' -> TagWrappers.DARK_RED;
            case '5' -> TagWrappers.DARK_PURPLE;
            case '6' -> TagWrappers.GOLD;
            case '7' -> TagWrappers.GRAY;
            case '8' -> TagWrappers.DARK_GRAY;
            case '9' -> TagWrappers.BLUE;
            case 'a' -> TagWrappers.GREEN;
            case 'b' -> TagWrappers.AQUA;
            case 'c' -> TagWrappers.RED;
            case 'd' -> TagWrappers.LIGHT_PURPLE;
            case 'e' -> TagWrappers.YELLOW;
            case 'f' -> TagWrappers.WHITE;
            case 'k' -> TagWrappers.OBFUSCATED;
            case 'l' -> TagWrappers.BOLD;
            case 'm' -> TagWrappers.STRIKETHROUGH;
            case 'n' -> TagWrappers.UNDERLINED;
            case 'o' -> TagWrappers.ITALIC;
            case 'r' -> TagWrappers.RESET;
            default -> null;
        };
    }

    @NotNull
    public static String wrapHexCodesAsTags(@NotNull String str) {
        StringBuilder builder = new StringBuilder(str);

        int index;
        int lastIndex = 0;

        while ((index = builder.toString().indexOf("#", lastIndex)) >= 0) {
            lastIndex = index + 1;

            int lookup = index + 7;
            if (builder.length() < lookup) break;

            Character prefix = index > 0 ? builder.charAt(index - 1) : null;
            Character postfix = builder.length() > lookup ? builder.charAt(lookup) : null;

            if (prefix != null && prefix == OPEN_BRACKET) continue; // Already wrapped in a tag, ignore.
            if (postfix != null && postfix == CLOSE_BRACKET) continue; // Already wrapped in a tag, ignore.
            if (prefix != null && postfix != null && prefix == DELIMITER && postfix == DELIMITER) continue; // Inside gradient, ignore.

            String sub = builder.substring(index, lookup);
            try {
                Integer.decode(sub);
            }
            catch (NumberFormatException exception) {
                continue;
            }

            builder.insert(index, OPEN_BRACKET);
            builder.insert(index + 8, CLOSE_BRACKET);
        }

        return builder.toString();
    }
}
