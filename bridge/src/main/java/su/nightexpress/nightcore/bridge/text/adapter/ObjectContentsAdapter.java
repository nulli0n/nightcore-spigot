package su.nightexpress.nightcore.bridge.text.adapter;

import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.text.contents.NightObjectContents;
import su.nightexpress.nightcore.bridge.text.contents.NightPlayerHeadObjectContents;
import su.nightexpress.nightcore.bridge.text.contents.NightSpriteObjectContents;

public interface ObjectContentsAdapter<C> {

    @NonNull
    C adaptContents(@NonNull NightObjectContents contents);

    @NonNull
    C adaptContents(@NonNull NightSpriteObjectContents contents);

    @NonNull
    C adaptContents(@NonNull NightPlayerHeadObjectContents contents);
}
