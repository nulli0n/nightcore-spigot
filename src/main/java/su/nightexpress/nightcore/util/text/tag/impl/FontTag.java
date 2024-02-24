package su.nightexpress.nightcore.util.text.tag.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.text.decoration.FontDecorator;
import su.nightexpress.nightcore.util.text.decoration.ParsedDecorator;
import su.nightexpress.nightcore.util.text.tag.api.ContentTag;

public class FontTag extends ContentTag {

    public static final String NAME = "font";

    public FontTag() {
        super(NAME);
    }

    @Override
    @Nullable
    public ParsedDecorator onParse(@NotNull String str) {
        String content = StringUtil.parseQuotedContent(str);
        if (content == null) return null;

        int length = content.length();// + 2; // 2 for quotes

        return new ParsedDecorator(new FontDecorator(content), length);
    }

    @Override
    public int getWeight() {
        return 0;
    }
}
