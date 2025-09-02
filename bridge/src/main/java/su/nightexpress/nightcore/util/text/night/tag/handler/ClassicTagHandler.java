package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.night.tag.TagHandler;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

public abstract class ClassicTagHandler implements TagHandler {

    public ClassicTagHandler() {

    }

    @Override
    public void handleOpen(@NotNull EntryGroup group, @Nullable String tagContent) {
        this.onHandleOpen(group, tagContent);
    }

    @Override
    public void handleClose(@NotNull EntryGroup group) {
        this.onHandleClose(group);
    }

    protected abstract void onHandleOpen(@NotNull EntryGroup group, @Nullable String tagContent);

    protected abstract void onHandleClose(@NotNull EntryGroup group);

    @Override
    public boolean canBeClosed() {
        return true;
    }
}
