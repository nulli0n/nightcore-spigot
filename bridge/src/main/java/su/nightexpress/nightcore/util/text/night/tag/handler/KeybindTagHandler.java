package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

public class KeybindTagHandler extends ClassicTagHandler {

    @Override
    protected void onHandleOpen(@NotNull EntryGroup group, @Nullable String tagContent) {
        if (tagContent == null) return;

        String key = ParserUtils.unquoted(tagContent);
        group.appendKeybindEntry(key);
    }

    @Override
    protected void onHandleClose(@NotNull EntryGroup group) {

    }

    @Override
    public boolean canBeClosed() {
        return false;
    }
}
