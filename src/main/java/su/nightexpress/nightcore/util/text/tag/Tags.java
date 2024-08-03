package su.nightexpress.nightcore.util.text.tag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.tag.api.Tag;
import su.nightexpress.nightcore.util.text.tag.impl.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Tags {

    private static final Map<String, Tag> REGISTRY = new HashMap<>();

    public static final GradientTag      GRADIENT        = new GradientTag();
    public static final LineBreakTag     LINE_BREAK      = new LineBreakTag();
    public static final FontTag          FONT            = new FontTag();
    public static final HoverTag         HOVER           = new HoverTag();
    public static final ClickTag         CLICK           = new ClickTag();
    public static final ResetTag         RESET           = new ResetTag();
    public static final HexColorTag      HEX_COLOR       = new HexColorTag();
    public static final ShortHexColorTag HEX_COLOR_SHORT = new ShortHexColorTag();
    public static final TranslationTag   TRANSLATE       = new TranslationTag();

    public static final FontStyleTag BOLD          = new FontStyleTag("b", FontStyleTag.Style.BOLD);
    public static final FontStyleTag ITALIC        = new FontStyleTag("i", FontStyleTag.Style.ITALIC);
    public static final FontStyleTag OBFUSCATED    = new FontStyleTag("o", FontStyleTag.Style.OBFUSCATED);
    public static final FontStyleTag STRIKETHROUGH = new FontStyleTag("s", FontStyleTag.Style.STRIKETHROUGH);
    public static final FontStyleTag UNDERLINED    = new FontStyleTag("u", FontStyleTag.Style.UNDERLINED);

    public static final ColorTag BLACK  = new ColorTag("black", "#000000");
    public static final ColorTag WHITE  = new ColorTag("white", "#ffffff");
    public static final ColorTag GRAY   = new ColorTag("gray", "#aaa8a8");
    public static final ColorTag GREEN  = new ColorTag("green", "#74ea31");
    public static final ColorTag YELLOW = new ColorTag("yellow", "#ead931");
    public static final ColorTag ORANGE = new ColorTag("orange", "#ea9631");
    public static final ColorTag RED    = new ColorTag("red", "#ea3131");
    public static final ColorTag BLUE   = new ColorTag("blue", "#3196ea");
    public static final ColorTag CYAN   = new ColorTag("cyan", "#31eace");
    public static final ColorTag PURPLE = new ColorTag("purple", "#bd31ea");
    public static final ColorTag PINK   = new ColorTag("pink", "#ea31b2");

    public static final ColorTag DARK_GRAY    = new ColorTag("dgray", new String[]{"dark_gray"}, "#6c6c62");
    public static final ColorTag LIGHT_GRAY   = new ColorTag("lgray", new String[]{"light_gray"}, "#d4d9d8");
    public static final ColorTag LIGHT_GREEN  = new ColorTag("lgreen", new String[]{"light_green"}, "#aefd5e");
    public static final ColorTag LIGHT_YELLOW = new ColorTag("lyellow", new String[]{"light_yellow"}, "#ffeea2");
    public static final ColorTag LIGHT_ORANGE = new ColorTag("lorange", new String[]{"light_orange"}, "#fdba5e");
    public static final ColorTag LIGHT_RED    = new ColorTag("lred", new String[]{"light_red"}, "#fd5e5e");
    public static final ColorTag LIGHT_BLUE   = new ColorTag("lblue", new String[]{"light_blue"}, "#5e9dfd");
    public static final ColorTag LIGHT_CYAN   = new ColorTag("lcyan", new String[]{"light_cyan"}, "#5edefd");
    public static final ColorTag LIGHT_PURPLE = new ColorTag("lpurple", new String[]{"light_purple"}, "#e39fff");
    public static final ColorTag LIGHT_PINK   = new ColorTag("lpink", new String[]{"light_pink"}, "#fd8ddb");

    static {
        registerTags(
            Tags.BLACK, Tags.WHITE, Tags.GRAY, Tags.GREEN,
            Tags.YELLOW, Tags.ORANGE, Tags.RED,
            Tags.BLUE, Tags.CYAN, Tags.PURPLE, Tags.PINK,

            Tags.DARK_GRAY, Tags.LIGHT_GRAY, Tags.LIGHT_GREEN,
            Tags.LIGHT_YELLOW, Tags.LIGHT_ORANGE, Tags.LIGHT_RED,
            Tags.LIGHT_BLUE, Tags.LIGHT_CYAN, Tags.LIGHT_PURPLE, Tags.LIGHT_PINK
        );

        registerTags(Tags.BOLD, Tags.ITALIC, Tags.OBFUSCATED, Tags.STRIKETHROUGH, Tags.UNDERLINED);

        registerTags(Tags.GRADIENT, Tags.LINE_BREAK, Tags.FONT, Tags.HOVER, Tags.CLICK,
            Tags.RESET, Tags.HEX_COLOR, Tags.HEX_COLOR_SHORT, Tags.TRANSLATE);
    }

    @NotNull
    public static Collection<Tag> getTags() {
        return REGISTRY.values();
    }

    public static void registerTags(@NotNull Tag... tags) {
        for (Tag tag : tags) {
            registerTag(tag);
        }
    }

    public static void registerTag(@NotNull Tag tag) {
        for (String alias : tag.getAliases()) {
            REGISTRY.put(alias, tag);
        }
    }

    @Nullable
    public static Tag getTag(@NotNull String name) {
        return REGISTRY.get(name.toLowerCase());
    }
}
