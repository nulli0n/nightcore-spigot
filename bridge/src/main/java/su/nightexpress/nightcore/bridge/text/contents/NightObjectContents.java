package su.nightexpress.nightcore.bridge.text.contents;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.bridge.text.adapter.ObjectContentsAdapter;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;

import java.util.Collections;
import java.util.UUID;

public interface NightObjectContents {

    @NonNull
    <T> T adapt(@NonNull ObjectContentsAdapter<T> adapter);

    @NonNull
    static NightSpriteObjectContents sprite(@NonNull NightKey atlas, @NonNull NightKey sprite) {
        return new NightSpriteObjectContents(atlas, sprite);
    }

    @NonNull
    static NightSpriteObjectContents sprite(@NonNull NightKey sprite) {
        return new NightSpriteObjectContents(NightSpriteObjectContents.DEFAULT_ATLAS, sprite);
    }

    @NonNull
    static NightPlayerHeadObjectContents playerHead(@NonNull String name) {
        return playerHead(name, true);
    }

    @NonNull
    static NightPlayerHeadObjectContents playerHead(@NonNull String name, boolean hat) {
        return new NightPlayerHeadObjectContents(name, null, Collections.emptyList(), hat, null);
    }

    @NonNull
    static NightPlayerHeadObjectContents playerHead(@NonNull UUID id) {
        return playerHead(id, true);
    }

    @NonNull
    static NightPlayerHeadObjectContents playerHead(@NonNull UUID id, boolean hat) {
        return new NightPlayerHeadObjectContents(null, id, Collections.emptyList(), hat, null);
    }

    @NonNull
    static NightPlayerHeadObjectContents playerHead(@NonNull NightKey texture) {
        return playerHead(texture, true);
    }

    @NonNull
    static NightPlayerHeadObjectContents playerHead(@NonNull NightKey texture, boolean hat) {
        return new NightPlayerHeadObjectContents(null, null, Collections.emptyList(), hat, texture);
    }

    @NonNull
    static NightPlayerHeadObjectContents playerHead(@NonNull NightProfile profile) {
        return playerHead(profile, true);
    }

    @NonNull
    static NightPlayerHeadObjectContents playerHead(@NonNull NightProfile profile, boolean hat) {
        return new NightPlayerHeadObjectContents(profile.getName(), profile.getId(), Collections
            .emptyList(), hat, null);
    }

    @NonNull
    static NightPlayerHeadObjectContents playerHead(@NonNull Player player) {
        return playerHead(player, true);
    }

    @NonNull
    static NightPlayerHeadObjectContents playerHead(@NonNull Player player, boolean hat) {
        return new NightPlayerHeadObjectContents(player.getName(), player.getUniqueId(), Collections
            .emptyList(), hat, null);
    }
}
