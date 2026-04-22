package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

public class ShadowOffTagHandler extends ClassicTagHandler {

    @Override
    protected void onHandleOpen(@NonNull EntryGroup group, @Nullable String tagContent) {

    }

    @Override
    protected void onHandleClose(@NonNull EntryGroup group) {
        group.setStyle(nightStyle -> nightStyle.shadowColor(0, 0, 0, 0));
    }
}
