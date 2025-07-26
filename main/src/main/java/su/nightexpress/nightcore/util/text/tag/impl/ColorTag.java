package su.nightexpress.nightcore.util.text.tag.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.api.SimpleTag;
import su.nightexpress.nightcore.util.text.tag.decorator.ColorDecorator;

import java.awt.*;

@Deprecated
public class ColorTag extends SimpleTag implements ColorDecorator {

    private final Color color;

    public ColorTag(@NotNull String name, @NotNull String hex) {
        this(name, new String[0], hex);
    }

    public ColorTag(@NotNull String name, @NotNull String[] aliases, @NotNull String hex) {
        this(name, aliases, ParserUtils.colorFromHexString(hex));
    }

    @Deprecated
    public ColorTag(@NotNull Color color) {
        this(color, new String[0]);
    }

    @Deprecated
    public ColorTag(@NotNull Color color, @NotNull String[] aliases) {
        this(ParserUtils.colorToHexString(color).substring(1), aliases, color);
    }

    public ColorTag(@NotNull String name, @NotNull Color color) {
        this(name, new String[0], color);
    }

    public ColorTag(@NotNull String name, @NotNull String[] aliases, @NotNull Color color) {
        super(name, aliases);
        this.color = color;
    }

    @NotNull
    public Color getColor() {
        return this.color;
    }

    @NotNull
    public String getHex() {
        return ParserUtils.colorToHexString(this.color);
    }

    @NotNull
    @Deprecated
    public String wrapHex(@NotNull String string) {
        String hex = this.getHex();

        return TagUtils.brackets(hex) + string + TagUtils.closedBrackets(hex);
    }

    @Override
    @NotNull
    public NightComponent decorate(@NotNull NightComponent component) {
        return component.color(this.color);
    }

    @NotNull
    @Deprecated
    public String encloseHex(@NotNull String string) {
        return this.wrapHex(string);
    }

    @NotNull
    @Deprecated
    public String toHexString() {
        return this.getHex();//TagUtils.colorToHexString(this.color);
    }
}
