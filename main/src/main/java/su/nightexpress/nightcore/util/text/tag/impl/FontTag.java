package su.nightexpress.nightcore.util.text.tag.impl;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import su.nightexpress.nightcore.util.text.tag.TagUtils;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;
import su.nightexpress.nightcore.util.text.tag.api.Tag;
import su.nightexpress.nightcore.util.text.tag.decorator.FontDecorator;

@Deprecated
public class FontTag extends Tag implements ContentTag {

    public FontTag() {
        super("font");
    }

    @Override
    @Nullable
    public FontDecorator parse(@NonNull String str) {
        return new FontDecorator(str);
    }

    @NonNull
    public String wrap(@NonNull String text, @NonNull String fontName) {
        return TagUtils.wrapContent(this, text, fontName);
    }
}
