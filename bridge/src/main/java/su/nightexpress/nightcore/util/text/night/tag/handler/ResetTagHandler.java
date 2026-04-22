package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.text.NightStyle;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

public class ResetTagHandler extends ClassicTagHandler {

    @Override
    protected void onHandleOpen(@NonNull EntryGroup group, @Nullable String tagContent) {
        group.setStyle(NightStyle.EMPTY);
        group.setStyleLocked(true);
    }

    @Override
    protected void onHandleClose(@NonNull EntryGroup group) {

    }
}
