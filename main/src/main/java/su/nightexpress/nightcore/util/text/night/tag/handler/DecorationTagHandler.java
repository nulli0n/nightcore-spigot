package su.nightexpress.nightcore.util.text.night.tag.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.text.NightTextDecoration;
import su.nightexpress.nightcore.util.text.night.entry.EntryGroup;

public class DecorationTagHandler extends ClassicTagHandler {

    private final NightTextDecoration decoration;
    private final boolean             state;

    DecorationTagHandler(@NotNull NightTextDecoration decoration, boolean state) {
        this.decoration = decoration;
        this.state = state;
    }

    @NotNull
    public static DecorationTagHandler normal(@NotNull NightTextDecoration decoration, boolean state) {
        return new DecorationTagHandler(decoration, state);
    }

    @Override
    protected void onHandleOpen(@NotNull EntryGroup group, @Nullable String tagContent) {
        group.setStyle(style -> style.decoration(this.decoration, this.state));
    }

    @Override
    protected void onHandleClose(@NotNull EntryGroup group) {

    }

    public boolean isInverted() {
        return !this.state;
    }
}
