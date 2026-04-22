package su.nightexpress.nightcore.util.text.night.entry;

import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.bridge.text.contents.NightObjectContents;
import su.nightexpress.nightcore.bridge.text.contents.NightSpriteObjectContents;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;

public class SpriteEntry extends ChildEntry {

    private final NightKey atlas;
    private final NightKey sprite;

    public SpriteEntry(@NonNull EntryGroup parent, @Nullable NightKey atlas, @NonNull NightKey sprite) {
        super(parent);
        this.atlas = atlas;
        this.sprite = sprite;
    }

    @Override
    public int textLength() {
        return 1;
    }

    @Override
    @NonNull
    public NightComponent toComponent() {
        NightSpriteObjectContents contents = this.atlas == null ? NightObjectContents.sprite(
            this.sprite) : NightObjectContents.sprite(this.atlas, this.sprite);
        return NightComponent.object(this.parent.style(), contents);
    }
}
