package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

public class FontTagHandler extends ClassicTagHandler {

    @Override
    protected void onHandleOpen(@NonNull EntryGroup group, @Nullable String tagContent) {
        if (tagContent == null) return;

        NightKey font = NightKey.key(tagContent);
        group.setStyle(style -> style.font(font));
    }

    @Override
    protected void onHandleClose(@NonNull EntryGroup group) {

    }
}
