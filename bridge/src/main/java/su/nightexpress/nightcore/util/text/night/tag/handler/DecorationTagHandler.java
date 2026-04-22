package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.text.NightTextDecoration;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

public class DecorationTagHandler extends ClassicTagHandler {

    private final NightTextDecoration decoration;
    private final boolean             state;

    DecorationTagHandler(@NonNull NightTextDecoration decoration, boolean state) {
        this.decoration = decoration;
        this.state = state;
    }

    @NonNull
    public static DecorationTagHandler normal(@NonNull NightTextDecoration decoration, boolean state) {
        return new DecorationTagHandler(decoration, state);
    }

    @Override
    protected void onHandleOpen(@NonNull EntryGroup group, @Nullable String tagContent) {
        group.setStyle(style -> style.decoration(this.decoration, this.state));
    }

    @Override
    protected void onHandleClose(@NonNull EntryGroup group) {

    }

    public boolean isInverted() {
        return !this.state;
    }
}
