package su.nightexpress.nightcore.util.text.night.entry;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.text.contents.NightPlayerHeadObjectContents;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class PlayerHeadEntry extends ChildEntry {

    private final NightPlayerHeadObjectContents contents;

    public PlayerHeadEntry(@NotNull EntryGroup parent, @NotNull NightPlayerHeadObjectContents contents) {
        super(parent);
        this.contents = contents;
    }

    @Override
    public int textLength() {
        return 1;
    }

    @Override
    @NotNull
    public NightComponent toComponent() {
        return NightComponent.object(this.parent.style(), this.contents);
    }
}
