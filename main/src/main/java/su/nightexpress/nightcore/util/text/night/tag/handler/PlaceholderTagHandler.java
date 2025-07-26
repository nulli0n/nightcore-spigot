package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

public class PlaceholderTagHandler extends ClassicTagHandler {

    private final String value;

    public PlaceholderTagHandler(@NotNull String value) {
        this.value = value;
    }

    @Override
    protected void onHandleOpen(@NotNull EntryGroup group, @Nullable String tagContent) {
        group.appendTextEntry(this.value);
    }

    @Override
    protected void onHandleClose(@NotNull EntryGroup group) {

    }

    @Override
    public boolean canBeClosed() {
        return false;
    }
}
