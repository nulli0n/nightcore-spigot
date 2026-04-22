package su.nightexpress.nightcore.util.text.night.tag;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

public interface TagHandler {

    void handleOpen(@NonNull EntryGroup group, @Nullable String tagContent);

    void handleClose(@NonNull EntryGroup group);

    boolean canBeClosed();
}
