package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

public class PlaceholderTagHandler extends ClassicTagHandler {

    private final String value;

    public PlaceholderTagHandler(@NonNull String value) {
        this.value = value;
    }

    @Override
    protected void onHandleOpen(@NonNull EntryGroup group, @Nullable String tagContent) {
        group.appendTextEntry(this.value);
    }

    @Override
    protected void onHandleClose(@NonNull EntryGroup group) {

    }

    @Override
    public boolean canBeClosed() {
        return false;
    }
}
