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

    @Override
    @Nullable
    public BaseColorDecorator parse(@NotNull String tagContent) {
        if (tagContent.length() < 7) return null;

        Color color = TagUtils.colorFromHexString(tagContent);
        return new BaseColorDecorator(color);

//        try {
//            Color color = Color.decode(tagContent);
//            return new BaseColorDecorator(color);
//        }
//        catch (NumberFormatException exception) {
//            return null;
//        }
    }
}
