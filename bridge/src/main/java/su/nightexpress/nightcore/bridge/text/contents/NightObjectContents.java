package su.nightexpress.nightcore.bridge.text.contents;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.common.NightKey;
import su.nightexpress.nightcore.bridge.text.adapter.ObjectContentsAdapter;
import su.nightexpress.nightcore.bridge.wrap.NightProfile;

import java.util.Collections;
import java.util.UUID;

public interface NightObjectContents {

    @NotNull <T> T adapt(@NotNull ObjectContentsAdapter<T> adapter);

    @NotNull
    static NightSpriteObjectContents sprite(@NotNull NightKey atlas, @NotNull NightKey sprite) {
        return new NightSpriteObjectContents(atlas, sprite);
    }

    @NotNull
    static NightSpriteObjectContents sprite(@NotNull NightKey sprite) {
        return new NightSpriteObjectContents(NightSpriteObjectContents.DEFAULT_ATLAS, sprite);
    }

    @NotNull
    static NightPlayerHeadObjectContents playerHead(@NotNull String name) {
        return playerHead(name, true);
    }

    @NotNull
    static NightPlayerHeadObjectContents playerHead(@NotNull String name, boolean hat) {
        return new NightPlayerHeadObjectContents(name, null, Collections.emptyList(), hat, null);
    }

    @NotNull
    static NightPlayerHeadObjectContents playerHead(@NotNull UUID id) {
        return playerHead(id, true);
    }

    @NotNull
    static NightPlayerHeadObjectContents playerHead(@NotNull UUID id, boolean hat) {
        return new NightPlayerHeadObjectContents(null, id, Collections.emptyList(), hat, null);
    }

    @NotNull
    static NightPlayerHeadObjectContents playerHead(@NotNull NightKey texture) {
        return playerHead(texture, true);
    }

    @NotNull
    static NightPlayerHeadObjectContents playerHead(@NotNull NightKey texture, boolean hat) {
        return new NightPlayerHeadObjectContents(null, null, Collections.emptyList(), hat, texture);
    }

    @NotNull
    static NightPlayerHeadObjectContents playerHead(@NotNull NightProfile profile) {
        return playerHead(profile, true);
    }

    @NotNull
    static NightPlayerHeadObjectContents playerHead(@NotNull NightProfile profile, boolean hat) {
        return new NightPlayerHeadObjectContents(profile.getName(), profile.getId(), Collections.emptyList(), hat, null);
    }

    @NotNull
    static NightPlayerHeadObjectContents playerHead(@NotNull Player player) {
        return playerHead(player, true);
    }

    @NotNull
    static NightPlayerHeadObjectContents playerHead(@NotNull Player player, boolean hat) {
        return new NightPlayerHeadObjectContents(player.getName(), player.getUniqueId(), Collections.emptyList(), hat, null);
    }
}
