package su.nightexpress.nightcore.util.text.tag;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.placeholder.Replacer;
import su.nightexpress.nightcore.util.text.tag.api.Tag;
import su.nightexpress.nightcore.util.text.tag.color.MinecraftColors;

import java.awt.*;

public class TagUtils {

    public static final char OPEN_BRACKET  = '<';
    public static final char CLOSE_BRACKET = '>';
    public static final char CLOSE_SLASH   = '/';
    public static final char SEMICOLON     = ':';
    public static final char QUOTE         = '\'';
    public static final char DOUBLE_QUOTE  = '"';

    private static final Replacer LEGACY_REPLACER = Replacer.create()
        .replace("&0", () -> Tags.HEX_COLOR.open(MinecraftColors.BLACK.getHex()))
        .replace("&1", () -> Tags.HEX_COLOR.open(MinecraftColors.DARK_BLUE.getHex()))
        .replace("&2", () -> Tags.HEX_COLOR.open(MinecraftColors.DARK_GREEN.getHex()))
        .replace("&3", () -> Tags.HEX_COLOR.open(MinecraftColors.DARK_AQUA.getHex()))
        .replace("&4", () -> Tags.HEX_COLOR.open(MinecraftColors.DARK_RED.getHex()))
        .replace("&5", () -> Tags.HEX_COLOR.open(MinecraftColors.DARK_PURPLE.getHex()))
        .replace("&6", () -> Tags.HEX_COLOR.open(MinecraftColors.GOLD.getHex()))
        .replace("&7", () -> Tags.HEX_COLOR.open(MinecraftColors.GRAY.getHex()))
        .replace("&8", () -> Tags.HEX_COLOR.open(MinecraftColors.DARK_GRAY.getHex()))
        .replace("&9", () -> Tags.HEX_COLOR.open(MinecraftColors.BLUE.getHex()))
        .replace("&a", () -> Tags.HEX_COLOR.open(MinecraftColors.GREEN.getHex()))
        .replace("&b", () -> Tags.HEX_COLOR.open(MinecraftColors.AQUA.getHex()))
        .replace("&c", () -> Tags.HEX_COLOR.open(MinecraftColors.RED.getHex()))
        .replace("&d", () -> Tags.HEX_COLOR.open(MinecraftColors.LIGHT_PURPLE.getHex()))
        .replace("&e", () -> Tags.HEX_COLOR.open(MinecraftColors.YELLOW.getHex()))
        .replace("&f", () -> Tags.HEX_COLOR.open(MinecraftColors.WHITE.getHex()))
        .replace("&k", Tags.OBFUSCATED::getBracketsName)
        .replace("&l", Tags.BOLD::getBracketsName)
        .replace("&m", Tags.STRIKETHROUGH::getBracketsName)
        .replace("&n", Tags.UNDERLINED::getBracketsName)
        .replace("&o", Tags.ITALIC::getBracketsName)
        .replace("&r", Tags.RESET::getBracketsName);

    @NotNull
    public static String replaceLegacyColors(@NotNull String string) {
        return LEGACY_REPLACER.apply(string);
    }

    @NotNull
    public static String brackets(@NotNull String str) {
        return OPEN_BRACKET + str + CLOSE_BRACKET;
    }

    @NotNull
    public static String closedBrackets(@NotNull String str) {
        return brackets(CLOSE_SLASH + str);
    }

    @NotNull
    public static String wrapContent(@NotNull Tag tag, @NotNull String string, @NotNull String content) {
        String tagOpen = TagUtils.brackets(tag.getName() + SEMICOLON + content);
        String tagClose = tag.getClosingName();

        return tagOpen + string + tagClose;
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
    public static String tagPlainHex(@NotNull String str) {
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
            if (prefix != null && postfix != null && prefix == SEMICOLON && postfix == SEMICOLON) continue; // Inside gradient, ignore.

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
