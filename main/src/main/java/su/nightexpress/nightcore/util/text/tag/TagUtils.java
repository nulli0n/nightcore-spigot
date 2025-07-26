package su.nightexpress.nightcore.util.text.tag;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.placeholder.Replacer;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.tag.api.Tag;
import su.nightexpress.nightcore.util.text.tag.color.MinecraftColors;

@Deprecated
public class TagUtils {

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
        .replace("&r", Tags.RESET::getBracketsName)
        ;

    @NotNull
    public static String replaceLegacyColors(@NotNull String string) {
        return LEGACY_REPLACER.apply(string);
    }

    @NotNull
    public static String brackets(@NotNull String str) {
        return ParserUtils.OPEN_BRACKET + str + ParserUtils.CLOSE_BRACKET;
    }

    @NotNull
    public static String closedBrackets(@NotNull String str) {
        return brackets(ParserUtils.CLOSE_SLASH + str);
    }

    @NotNull
    public static String wrapContent(@NotNull Tag tag, @NotNull String string, @NotNull String content) {
        String tagOpen = TagUtils.brackets(tag.getName() + ParserUtils.DELIMITER + content);
        String tagClose = tag.getClosingName();

        return tagOpen + string + tagClose;
    }
}
