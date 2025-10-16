package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.text.night.ParserUtils;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;
import su.nightexpress.nightcore.util.text.night.entry.SpriteEntry;
import su.nightexpress.nightcore.util.text.night.tag.TagContent;

public class SpriteTagHandler extends ClassicTagHandler {

    @Override
    protected void onHandleOpen(@NotNull EntryGroup group, @Nullable String tagContent) {
        if (tagContent == null) return;
        if (!Version.withCopperAge()) return;

        TagContent content = ParserUtils.parseInnerContent(tagContent);

        String atlas = null;
        String sprite;

        if (content.hasBoth()) {
            atlas = content.first();
            sprite = content.second();
        }
        else {
            sprite = ParserUtils.unquoted(content.first());
        }

        try {
            NightKey atlasKey = atlas == null ? null : NightKey.key(atlas);

            group.appendEntry(new SpriteEntry(group, atlasKey, NightKey.key(sprite)));
        }
        catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    protected void onHandleClose(@NotNull EntryGroup group) {

    }

    @Override
    public boolean canBeClosed() {
        return false;
    }
}
