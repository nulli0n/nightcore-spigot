package su.nightexpress.nightcore.bridge.text.adapter;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.text.contents.NightObjectContents;
import su.nightexpress.nightcore.bridge.text.contents.NightPlayerHeadObjectContents;
import su.nightexpress.nightcore.bridge.text.contents.NightSpriteObjectContents;

public interface ObjectContentsAdapter<C> {

    @NotNull C adaptContents(@NotNull NightObjectContents contents);

    @NotNull C adaptContents(@NotNull NightSpriteObjectContents contents);

    @NotNull C adaptContents(@NotNull NightPlayerHeadObjectContents contents);
}
