package su.nightexpress.nightcore.util.text.night.tag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

public interface TagHandler {

    void handleOpen(@NotNull EntryGroup group, @Nullable String tagContent);

    void handleClose(@NotNull EntryGroup group);

    boolean canBeClosed();
}
