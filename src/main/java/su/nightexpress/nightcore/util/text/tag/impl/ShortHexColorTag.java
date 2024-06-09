package su.nightexpress.nightcore.util.text.tag.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.tag.decorator.BaseColorDecorator;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;

import java.awt.*;

public class ShortHexColorTag extends Tag implements ContentTag {

    public static final String NAME = "#";

    public ShortHexColorTag() {
        super(NAME);
    }

    @Override
    @Nullable
    public BaseColorDecorator parse(@NotNull String tagContent) {
        if (tagContent.length() < 7) return null;

        try {
            Color color = Color.decode(tagContent);
            return new BaseColorDecorator(color);
        }
        catch (NumberFormatException exception) {
            return null;
        }
    }
}
