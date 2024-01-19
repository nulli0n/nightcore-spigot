package su.nightexpress.nightcore.util.text.tag;

import su.nightexpress.nightcore.util.text.tag.impl.*;

public class Tags {

    public static final ClickTag     CLICK      = new ClickTag();
    public static final FontTag      FONT       = new FontTag();
    public static final GradientTag  GRADIENT   = new GradientTag();
    public static final HexColorTag  HEX_COLOR  = new HexColorTag();
    public static final HoverTag     HOVER      = new HoverTag();
    public static final LineBreakTag LINE_BREAK = new LineBreakTag();
    public static final ResetTag     RESET      = new ResetTag();

    public static final FontStyleTag BOLD          = new FontStyleTag("b", FontStyleTag.Style.BOLD);
    public static final FontStyleTag ITALIC        = new FontStyleTag("i", FontStyleTag.Style.ITALIC);
    public static final FontStyleTag OBFUSCATED    = new FontStyleTag("o", FontStyleTag.Style.OBFUSCATED);
    public static final FontStyleTag STRIKETHROUGH = new FontStyleTag("s", FontStyleTag.Style.STRIKETHROUGH);
    public static final FontStyleTag UNDERLINED    = new FontStyleTag("u", FontStyleTag.Style.UNDERLINED);

    public static final ColorTag BLACK  = new ColorTag("black", "#000000");
    public static final ColorTag WHITE  = new ColorTag("white", "#e8f2f2");
    public static final ColorTag GRAY   = new ColorTag("gray", "#aaa8a8");
    public static final ColorTag GREEN  = new ColorTag("green", "#74ea31");
    public static final ColorTag YELLOW = new ColorTag("yellow", "#ead931");
    public static final ColorTag ORANGE = new ColorTag("orange", "#ea9631");
    public static final ColorTag RED    = new ColorTag("red", "#ea3131");
    public static final ColorTag BLUE   = new ColorTag("blue", "#3196ea");
    public static final ColorTag CYAN   = new ColorTag("cyan", "#31eace");
    public static final ColorTag PURPLE = new ColorTag("purple", "#bd31ea");
    public static final ColorTag PINK   = new ColorTag("pink", "#ea31b2");

    public static final ColorTag DARK_GRAY    = new ColorTag("dark_gray", "#6c6c62");
    public static final ColorTag LIGHT_GRAY   = new ColorTag("light_gray", "#d4d9d8");
    public static final ColorTag LIGHT_GREEN  = new ColorTag("light_green", "#aefd5e");
    public static final ColorTag LIGHT_YELLOW = new ColorTag("light_yellow", "#ffeea2");
    public static final ColorTag LIGHT_ORANGE = new ColorTag("light_orange", "#fdba5e");
    public static final ColorTag LIGHT_RED    = new ColorTag("light_red", "#fd5e5e");
    public static final ColorTag LIGHT_BLUE   = new ColorTag("light_blue", "#5e9dfd");
    public static final ColorTag LIGHT_CYAN   = new ColorTag("light_cyan", "#5edefd");
    public static final ColorTag LIGHT_PURPLE = new ColorTag("light_purple", "#e39fff");
    public static final ColorTag LIGHT_PINK   = new ColorTag("light_pink", "#fd8ddb");
}
