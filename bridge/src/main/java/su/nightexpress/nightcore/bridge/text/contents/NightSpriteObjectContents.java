package su.nightexpress.nightcore.bridge.text.contents;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.bridge.text.adapter.ObjectContentsAdapter;

import java.util.Objects;

public class NightSpriteObjectContents implements NightObjectContents {

    public static final NightKey DEFAULT_ATLAS = NightKey.key("minecraft:blocks");

    private final NightKey atlas;
    private final NightKey sprite;

    NightSpriteObjectContents(@NonNull NightKey atlas, @NonNull NightKey sprite) {
        this.atlas = atlas;
        this.sprite = sprite;
    }

    @Override
    @NonNull
    public <T> T adapt(@NonNull ObjectContentsAdapter<T> adapter) {
        return adapter.adaptContents(this);
    }

    @NonNull
    public NightKey atlas() {
        return this.atlas;
    }

    @NonNull
    public NightKey sprite() {
        return this.sprite;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NightSpriteObjectContents that)) return false;
        return Objects.equals(atlas, that.atlas) && Objects.equals(sprite, that.sprite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atlas, sprite);
    }

    @Override
    public String toString() {
        return "NightSpriteObjectContents{" + "atlas=" + atlas + ", sprite=" + sprite + '}';
    }
}
