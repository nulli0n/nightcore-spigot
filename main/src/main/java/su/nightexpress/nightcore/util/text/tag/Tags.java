package su.nightexpress.nightcore.util.text.tag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.core.tag.ColorScheme;
import su.nightexpress.nightcore.util.text.tag.api.Tag;
import su.nightexpress.nightcore.util.text.tag.impl.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class Tags {

/*    public static final String FILE_NAME = "colors.yml";
    private static final String COLORS_PATH = "Colors";*/

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

    public static final FontStyleTag BOLD          = new FontStyleTag("b", new String[]{"bold"}, FontStyleTag.Style.BOLD, false);
    public static final FontStyleTag ITALIC        = new FontStyleTag("i", new String[]{"em", "italic"}, FontStyleTag.Style.ITALIC, false);
    public static final FontStyleTag OBFUSCATED    = new FontStyleTag("o", new String[]{"obfuscated", "obf"}, FontStyleTag.Style.OBFUSCATED, false);
    public static final FontStyleTag STRIKETHROUGH = new FontStyleTag("s", new String[]{"strikethrough", "st"}, FontStyleTag.Style.STRIKETHROUGH, false);
    public static final FontStyleTag UNDERLINED    = new FontStyleTag("u", new String[]{"underlined"}, FontStyleTag.Style.UNDERLINED, false);

    public static final FontStyleTag UNBOLD          = BOLD.inverted();
    public static final FontStyleTag UNITALIC        = ITALIC.inverted();
    public static final FontStyleTag UNOBFUSCATED    = OBFUSCATED.inverted();
    public static final FontStyleTag UNSTRIKETHROUGH = STRIKETHROUGH.inverted();
    public static final FontStyleTag UNUNDERLINED    = UNDERLINED.inverted();

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
    public static final ColorTag LIGHT_GREEN  = new ColorTag("lgreen", new String[]{"light_green"}, "#91f251");
    public static final ColorTag LIGHT_YELLOW = new ColorTag("lyellow", new String[]{"light_yellow"}, "#ffeea2");
    public static final ColorTag LIGHT_ORANGE = new ColorTag("lorange", new String[]{"light_orange"}, "#fdba5e");
    public static final ColorTag LIGHT_RED    = new ColorTag("lred", new String[]{"light_red"}, "#fd5e5e");
    public static final ColorTag LIGHT_BLUE   = new ColorTag("lblue", new String[]{"light_blue"}, "#5e9dfd");
    public static final ColorTag LIGHT_CYAN   = new ColorTag("lcyan", new String[]{"light_cyan"}, "#5edefd");
    public static final ColorTag LIGHT_PURPLE = new ColorTag("lpurple", new String[]{"light_purple"}, "#e39fff");
    public static final ColorTag LIGHT_PINK   = new ColorTag("lpink", new String[]{"light_pink"}, "#fd8ddb");

    static {
/*        registerTags(
            Tags.BLACK, Tags.WHITE, Tags.GRAY, Tags.GREEN,
            Tags.YELLOW, Tags.ORANGE, Tags.RED,
            Tags.BLUE, Tags.CYAN, Tags.PURPLE, Tags.PINK,

            Tags.DARK_GRAY, Tags.LIGHT_GRAY, Tags.LIGHT_GREEN,
            Tags.LIGHT_YELLOW, Tags.LIGHT_ORANGE, Tags.LIGHT_RED,
            Tags.LIGHT_BLUE, Tags.LIGHT_CYAN, Tags.LIGHT_PURPLE, Tags.LIGHT_PINK
        );*/

        registerTags(
            Tags.BOLD, Tags.ITALIC, Tags.OBFUSCATED, Tags.STRIKETHROUGH, Tags.UNDERLINED,
            Tags.UNBOLD, Tags.UNITALIC, Tags.UNOBFUSCATED, Tags.UNSTRIKETHROUGH, Tags.UNUNDERLINED
        );

        registerTags(Tags.GRADIENT, Tags.LINE_BREAK, Tags.FONT, Tags.HOVER, Tags.CLICK,
            Tags.RESET, Tags.HEX_COLOR, Tags.HEX_COLOR_SHORT, Tags.TRANSLATE);

/*        registerTag(MinecraftColors.DARK_BLUE);
        registerTag(MinecraftColors.DARK_GREEN);
        registerTag(MinecraftColors.DARK_AQUA);
        registerTag(MinecraftColors.DARK_RED);
        registerTag(MinecraftColors.DARK_PURPLE);
        registerTag(MinecraftColors.GOLD);
        registerTag(MinecraftColors.AQUA);*/
    }

    public static void registerFromScheme(@NotNull ColorScheme scheme) {
        scheme.getColors().forEach(code -> {
            registerTag(new ColorTag(code.name(), code.color()));
        });
    }

/*    @Deprecated
    public static void loadColorsFromFile(@NotNull NightCore core) {
        FileConfig config = FileConfig.loadOrExtract(core, FILE_NAME);

        if (config.getSection(COLORS_PATH).isEmpty()) {
            getTags().forEach(tag -> {
                if (!(tag instanceof ColorTag colorTag)) return;

                for (String alias : colorTag.getAliases()) {
                    config.set(COLORS_PATH + "." + alias, colorTag.getHex());
                }
            });
        }

        config.getSection(COLORS_PATH).forEach(name -> {
            String hex = config.getString(COLORS_PATH + "." + name);
            if (hex == null) return;

            Color color = ParserUtils.colorFromHexString(hex);
            registerTag(new ColorTag(name, color));
        });

        config.saveChanges();
    }*/

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
