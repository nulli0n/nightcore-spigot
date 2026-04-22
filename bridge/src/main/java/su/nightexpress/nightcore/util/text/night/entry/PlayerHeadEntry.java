package su.nightexpress.nightcore.util.text.night.entry;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.text.contents.NightPlayerHeadObjectContents;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class PlayerHeadEntry extends ChildEntry {

    private final NightPlayerHeadObjectContents contents;

    public PlayerHeadEntry(@NonNull EntryGroup parent, @NonNull NightPlayerHeadObjectContents contents) {
        super(parent);
        this.contents = contents;
    }

    @Override
    public int textLength() {
        return 1;
    }

    @Override
    @NonNull
    public NightComponent toComponent() {
        return NightComponent.object(this.parent.style(), this.contents);
    }
}
