package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

public class LangTagHandler extends ClassicTagHandler {

    // TODO Arguments

    @Override
    protected void onHandleOpen(@NonNull EntryGroup group, @Nullable String tagContent) {
        if (tagContent == null) return;

        String content = fixContent(tagContent);

        int index = ParserUtils.findUnescapedUnquotedChar(content, ParserUtils.DELIMITER, 0);
        String key = ParserUtils.unquoted(index < 0 ? content : content.substring(0, index));
        String fallback = index < 0 || index >= content.length() ? null : ParserUtils.unquoted(content.substring(
            index + 1));

        group.appendLangEntry(key, fallback);
    }

    @NonNull
    private static String fixContent(@NonNull String tagContent) {
        if (tagContent.endsWith("/")) return tagContent.substring(0, tagContent.length() - 1); // MiniMessage#serialize fix.

        return tagContent;
    }

    @Override
    protected void onHandleClose(@NonNull EntryGroup group) {

    }

    @Override
    public boolean canBeClosed() {
        return false;
    }
}
