package su.nightexpress.nightcore.util.text.tag.impl;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;
import su.nightexpress.nightcore.util.text.tag.decorator.BaseColorDecorator;

import java.awt.*;

@Deprecated
public class ShortHexColorTag extends Tag implements ContentTag {

    public static final String NAME = "#";

    public ShortHexColorTag() {
        super(NAME);
    }

    @Override
    @Nullable
    public BaseColorDecorator parse(@NonNull String tagContent) {
        if (tagContent.length() < 7) return null;

        Color color = ParserUtils.colorFromHexString(tagContent);
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
