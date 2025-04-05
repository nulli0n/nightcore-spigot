package su.nightexpress.nightcore.util.text.tag.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.decorator.BaseColorDecorator;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

import java.awt.*;

public class HexColorTag extends Tag implements ContentTag {

    public static final String NAME = "color";

    public HexColorTag() {
        super(NAME, new String[]{"colour", "c"});
    }

    @NotNull
    @Deprecated
    public String enclose(@NotNull String text, @NotNull String hex) {
        return this.wrap(text, hex);//brackets(this.name + ":" + hex) + text + closedBrackets(this.name);
    }

    @NotNull
    public String open(@NotNull String hex) {
        return TagUtils.brackets(this.name + TagUtils.SEMICOLON + hex);
    }

    @NotNull
    public String wrap(@NotNull String string, @NotNull Color color) {
        return this.wrap(string, TagUtils.colorToHexString(color));
    }

    @NotNull
    public String wrap(@NotNull String string, @NotNull String hex) {
        return this.open(hex) + string + TagUtils.closedBrackets(this.name);
    }

    @Override
    @Nullable
    public BaseColorDecorator parse(@NotNull String tagContent) {
        if (tagContent.length() < 7) return null;

        Color color = TagUtils.colorFromHexString(tagContent);

        // TODO Named colors support

        return new BaseColorDecorator(color);
    }
}
