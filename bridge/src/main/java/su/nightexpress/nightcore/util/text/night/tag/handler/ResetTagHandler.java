package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.text.NightStyle;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

public class ResetTagHandler extends ClassicTagHandler {

    @Override
    protected void onHandleOpen(@NotNull EntryGroup group, @Nullable String tagContent) {
        group.setStyle(NightStyle.EMPTY);
        group.setStyleLocked(true);
    }

    @Override
    protected void onHandleClose(@NotNull EntryGroup group) {

    }
}
