package su.nightexpress.nightcore.util.text.night.tag.color;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.tag.TagShortNames;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ColorScheme implements Writeable {

    public static final String DEFAULT = "default";
    public static final String CUSTOM = "custom";

    private final String id;
    private final List<ColorCode> colors;

    public ColorScheme(@NotNull String id, @NotNull List<ColorCode> colors) {
        this.id = id.toLowerCase();
        this.colors = colors;
    }

    @NotNull
    public static ColorScheme read(@NotNull FileConfig config, @NotNull String path, @NotNull String id) {
        List<ColorCode> colors = new ArrayList<>();

        config.getSection(path + ".Colors").forEach(name -> {
            String code = config.getString(path + ".Colors." + name);
            if (code == null || code.length() != 7) return;

            Color color = ParserUtils.colorFromHexString(code);
            colors.add(new ColorCode(name.toLowerCase(), color));
        });

        return new ColorScheme(id, colors);
    }

    @NotNull
    public static List<ColorScheme> getDefaultSchemes() {
        ColorScheme vanilla = new ColorScheme(DEFAULT, Lists.newList(
            new ColorCode(TagShortNames.BLACK, new Color(0, 0, 0)),
            new ColorCode(TagShortNames.WHITE, new Color(255, 255, 255)),

            new ColorCode(TagShortNames.GRAY, new Color(170, 170, 170)),
            new ColorCode(TagShortNames.SOFT_GRAY, new Color(170, 170, 170)),
            new ColorCode(TagShortNames.DARK_GRAY, new Color(70, 70, 70)),

            new ColorCode(TagShortNames.RED, new Color(255, 85, 85)),
            new ColorCode(TagShortNames.SOFT_RED, new Color(255, 85, 85)),
            new ColorCode(TagShortNames.DARK_RED, new Color(170, 0, 0)),

            new ColorCode(TagShortNames.GREEN, new Color(85, 255, 85)),
            new ColorCode(TagShortNames.SOFT_GREEN, new Color(85, 255, 85)),
            new ColorCode(TagShortNames.DARK_GREEN, new Color(0, 170, 0)),

            new ColorCode(TagShortNames.BLUE, new Color(85, 85, 255)),
            new ColorCode(TagShortNames.SOFT_BLUE, new Color(85, 85, 255)),
            new ColorCode(TagShortNames.DARK_BLUE, new Color(0, 0, 170)),

            new ColorCode(TagShortNames.YELLOW, new Color(255, 255, 85)),
            new ColorCode(TagShortNames.SOFT_YELLOW, new Color(255, 255, 85)),
            new ColorCode(TagShortNames.DARK_YELLOW, new Color(180, 180, 50)),

            new ColorCode(TagShortNames.ORANGE, new Color(255, 170, 0)),
            new ColorCode(TagShortNames.SOFT_ORANGE, new Color(230, 170, 50)),
            new ColorCode(TagShortNames.GOLD, new Color(255, 170, 0)),

            new ColorCode(TagShortNames.AQUA, new Color(85, 255, 255)),
            new ColorCode(TagShortNames.SOFT_AQUA, new Color(85, 255, 255)),
            new ColorCode(TagShortNames.DARK_AQUA, new Color(0, 170, 170)),

            new ColorCode(TagShortNames.PURPLE, new Color(255, 85, 255)),
            new ColorCode(TagShortNames.SOFT_PURPLE, new Color(255, 85, 255)),
            new ColorCode(TagShortNames.LIGHT_PURPLE, new Color(255, 85, 255)),
            new ColorCode(TagShortNames.DARK_PURPLE, new Color(170, 0, 170)),

            new ColorCode(TagShortNames.PINK, new Color(230, 50, 120)),
            new ColorCode(TagShortNames.SOFT_PINK, new Color(230, 90, 150))
        ));

        ColorScheme custom = new ColorScheme(CUSTOM, Lists.newList(
            new ColorCode(TagShortNames.BLACK, new Color(0, 0, 0)),
            new ColorCode(TagShortNames.WHITE, new Color(255, 255, 255)),

            new ColorCode(TagShortNames.GRAY, new Color(150, 150, 150)),
            new ColorCode(TagShortNames.SOFT_GRAY, new Color(180, 180, 180)),
            new ColorCode(TagShortNames.DARK_GRAY, new Color(70, 70, 70)),

            new ColorCode(TagShortNames.RED, new Color(230, 50, 50)),
            new ColorCode(TagShortNames.SOFT_RED, new Color(230, 75, 75)),
            new ColorCode(TagShortNames.DARK_RED, new Color(150, 50, 50)),

            new ColorCode(TagShortNames.GREEN, new Color(50, 230, 50)),
            new ColorCode(TagShortNames.SOFT_GREEN, new Color(120, 230, 80)),
            new ColorCode(TagShortNames.DARK_GREEN, new Color(50, 120, 50)),

            new ColorCode(TagShortNames.BLUE, new Color(50, 120, 230)),
            new ColorCode(TagShortNames.SOFT_BLUE, new Color(50, 170, 230)),
            new ColorCode(TagShortNames.DARK_BLUE, new Color(50, 50, 150)),

            new ColorCode(TagShortNames.YELLOW, new Color(230, 230, 50)),
            new ColorCode(TagShortNames.SOFT_YELLOW, new Color(230, 230, 120)),
            new ColorCode(TagShortNames.DARK_YELLOW, new Color(180, 180, 50)),

            new ColorCode(TagShortNames.ORANGE, new Color(230, 120, 50)),
            new ColorCode(TagShortNames.SOFT_ORANGE, new Color(230, 170, 50)),
            new ColorCode(TagShortNames.GOLD, new Color(230, 170, 50)),

            new ColorCode(TagShortNames.AQUA, new Color(50, 230, 230)),
            new ColorCode(TagShortNames.SOFT_AQUA, new Color(150, 230, 230)),
            new ColorCode(TagShortNames.DARK_AQUA, new Color(50, 120, 120)),

            new ColorCode(TagShortNames.PURPLE, new Color(120, 50, 230)),
            new ColorCode(TagShortNames.SOFT_PURPLE, new Color(150, 90, 230)),
            new ColorCode(TagShortNames.LIGHT_PURPLE, new Color(150, 90, 230)),
            new ColorCode(TagShortNames.DARK_PURPLE, new Color(75, 50, 150)),

            new ColorCode(TagShortNames.PINK, new Color(230, 50, 120)),
            new ColorCode(TagShortNames.SOFT_PINK, new Color(230, 90, 150))
        ));

        return Lists.newList(vanilla, custom);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.remove(path + ".Colors");
        this.colors.forEach(code -> config.set(path + ".Colors." + code.name(), ParserUtils.colorToHexString(code.color())));
    }

    @NotNull
    public String getId() {
        return this.id;
    }

    @NotNull
    public List<ColorCode> getColors() {
        return this.colors;
    }
}
