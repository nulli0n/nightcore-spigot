package su.nightexpress.nightcore.util.text.tag.impl;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.decorator.BaseColorDecorator;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

import java.awt.*;

@Deprecated
public class HexColorTag extends Tag implements ContentTag {

    public static final String NAME = "color";

    public HexColorTag() {
        super("c", new String[]{"color", "colour"});
    }

    @NonNull
    @Deprecated
    public String enclose(@NonNull String text, @NonNull String hex) {
        return this.wrap(text, hex);//brackets(this.name + ":" + hex) + text + closedBrackets(this.name);
    }

    @NonNull
    public String open(@NonNull String hex) {
        return TagUtils.brackets(this.name + ParserUtils.DELIMITER + hex);
    }

    @NonNull
    public String wrap(@NonNull String string, @NonNull Color color) {
        return this.wrap(string, ParserUtils.colorToHexString(color));
    }

    @NonNull
    public String wrap(@NonNull String string, @NonNull String hex) {
        return this.open(hex) + string + TagUtils.closedBrackets(this.name);
    }

    @Override
    @Nullable
    public BaseColorDecorator parse(@NonNull String tagContent) {
        Color color = ParserUtils.colorFromSchemeOrHex(tagContent);
        if (color == null) return null;

        return new BaseColorDecorator(color);
    }
}
