package su.nightexpress.nightcore.util.text.tag.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;
import su.nightexpress.nightcore.util.text.tag.decorator.FontDecorator;

public class FontTag extends Tag implements ContentTag {

    public FontTag() {
        super("font");
    }

    @Override
    @Nullable
    public FontDecorator parse(@NotNull String str) {
        return new FontDecorator(str);
    }

    @NotNull
    public String wrap(@NotNull String text, @NotNull String fontName) {
        return TagUtils.wrapContent(this, text, fontName);
    }
}
